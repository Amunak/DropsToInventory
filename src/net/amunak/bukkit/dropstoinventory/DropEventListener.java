package net.amunak.bukkit.dropstoinventory;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.loot.Lootable;
import org.bukkit.plugin.PluginLogger;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class DropEventListener implements Listener {
	Map<UUID, LastDamageDealer> damageDealerMap;

	public DropEventListener(DropsToInventory plugin) {
		damageDealerMap = new HashMap<>();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockDropItemEvent(@NotNull BlockDropItemEvent event) {
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();

		ListIterator<Item> i = event.getItems().listIterator();
		while (i.hasNext()) {
			Item item = i.next();
			HashMap<Integer, ItemStack> rest = inventory.addItem(item.getItemStack());

			ItemStack restIS = rest.get(0);
			if (restIS != null) {
				item.getItemStack().setAmount(restIS.getAmount());
			}

			if (restIS == null || restIS.getAmount() == 0) {
				i.remove();
			}
		}
	}

	static class LastDamageDealer {
		private final Player damager;
		private final Entity entity;
		private Instant time;

		public LastDamageDealer(Player damager, Entity entity) {
			this.damager = damager;
			this.entity = entity;
			this.resetTime();
		}

		public Player getDamager() {
			return damager;
		}

		public Entity getEntity() {
			return entity;
		}

		public Instant getTime() {
			return time;
		}

		public void resetTime() {
			this.time =  Instant.now();
		}
	}


	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageByEntityEvent(@NotNull EntityDamageByEntityEvent event) {
		// ignore low damage
		if (event.getFinalDamage() < 0.1) {
			return;
		}

		// skip things that don't drop anything
		Entity entity = event.getEntity();
		if (!(entity instanceof Lootable) && !(entity instanceof InventoryHolder)) {
			return;
		}

		Player damager = null;
		if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
			damager = (Player) ((Projectile) event.getDamager()).getShooter();
		}

		if (event.getDamager() instanceof Player) {
			damager = (Player) event.getDamager();
		}

		// can't tell who did damage
		if (damager == null) {
			return;
		}

		LastDamageDealer record = this.damageDealerMap.get(entity.getUniqueId());
		if (record != null && record.getDamager() == damager) {
			// only update the record
			record.resetTime();

			return;
		}

		// TODO in addition to regular removals also add some kind of scheduled garbage collector
		//  that clears old entries every once in a while
		this.damageDealerMap.put(entity.getUniqueId(), new LastDamageDealer(damager, entity));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDeathEvent(@NotNull EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		LastDamageDealer record = this.damageDealerMap.get(entity.getUniqueId());
		if (record == null) {
			return;
		}

		List<ItemStack> items = event.getDrops();
		Player player = record.getDamager();
		if (items.isEmpty() || !player.isOnline() || player.isDead()) {
			this.damageDealerMap.remove(entity.getUniqueId());
			return;
		}

		// if it took too much time for the entity to die (this still allows  for, like, fire damage or something)
		if (Duration.between(record.getTime(), Instant.now()).getSeconds() > 5) {
			return;
		}

		player.giveExp(event.getDroppedExp());
		event.setDroppedExp(0);

		PlayerInventory inventory = player.getInventory();
		ListIterator<ItemStack> i = items.listIterator();
		while (i.hasNext()) {
			ItemStack item = i.next();

			HashMap<Integer, ItemStack> rest = inventory.addItem(item);

			ItemStack restIS = rest.get(0);
			if (restIS != null) {
				item.setAmount(restIS.getAmount());
			}

			if (restIS == null || restIS.getAmount() == 0) {
				i.remove();
			}
		}

		this.damageDealerMap.remove(entity.getUniqueId());
	}

	/**
	 * Tries to put given ItemStack into Player's Inventory, returning bool when the original ItemStack is supposed to be deleted.
	 */
	protected boolean tryPutInInventory(Player player, ItemStack itemStack) {
		EntityPickupItemEvent entityEvent = new EntityPickupItemEvent(player, itemStack, remaining);
		entityEvent.setCancelled(!entityEvent.getEntity().getCanPickupItems());
		this.level.getCraftServer().getPluginManager().callEvent(entityEvent);
		if (entityEvent.isCancelled()) {
			itemstack.setCount(i); // SPIGOT-5294 - restore count
			return;
		}

		HashMap<Integer, ItemStack> rest = player.getInventory().addItem(itemStack);

		ItemStack restItemStack = rest.get(0);
		if (restItemStack == null || restItemStack.getAmount() == 0) {
			itemStack.setAmount(0);

			return true;
		}

		itemStack.setAmount(restItemStack.getAmount());

		return false;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreakEvent(@NotNull BlockBreakEvent event) {
		PluginLogger.getGlobal().info(String.format("BREAK %s %s :: %s", event.getPlayer().getName(), event.getBlock().getClass().getSimpleName(), event.isDropItems()));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDropItemEvent(@NotNull EntityDropItemEvent event) {
		PluginLogger.getGlobal().info(String.format("DROPITEM %s %s :: %s", event.getEntity().getName(), event.getEntity().getClass().getSimpleName(), event.getItemDrop().getName()));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onHangingBreakByEntityEvent(@NotNull HangingBreakByEntityEvent event) {
		if (event.getRemover() == null) {
			PluginLogger.getGlobal().info(String.format("HANGING %s %s :: %s", event.getEntity().getName(), event.getEntity().getClass().getSimpleName(), "NOREMOVER"));
			return;
		}

		if (event.getEntity().getType() == EntityType.ITEM_FRAME) {

		}

		PluginLogger.getGlobal().info(String.format("HANGING %s %s :: %s", event.getEntity().getName(), event.getEntity().getClass().getSimpleName(), event.getRemover().getName()));
	}
}
