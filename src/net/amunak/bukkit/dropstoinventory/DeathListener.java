package net.amunak.bukkit.dropstoinventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Creator: Revolution
 * Date: 7/11/14.
 * Project: DropsToInventory
 * Usage: Listens for entity deaths
 */
public class DeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        List<ItemStack> drops = event.getDrops();
        Player killer = event.getEntity().getKiller();
        for(ItemStack drop : drops) {
            if(!BreakListener.playersTurnedOff.contains(killer.getUniqueId().toString()) &&
                    !BreakListener.playeresOptedOut.contains(killer.getUniqueId().toString()) &&
                    !BreakListener.isFull(killer, drop.getType())) {
                killer.getInventory().addItem(drop);
            }
        }
    }
}