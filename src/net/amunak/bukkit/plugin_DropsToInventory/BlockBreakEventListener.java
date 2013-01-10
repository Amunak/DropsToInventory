package net.amunak.bukkit.plugin_DropsToInventory;

/**
 * Copyright 2013 Jiří Barouš
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
import java.util.Collection;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author Amunak
 */
public final class BlockBreakEventListener implements Listener {
    public List<String> blockFilter;
    public List<String> safeBlocks;
    public DropsToInventory plugin;

    public BlockBreakEventListener(DropsToInventory p) {
        plugin = p;
        blockFilter = plugin.config.getStringList("lists.blockFilter");
        safeBlocks = plugin.config.getStringList("lists.safeBlocks");
        Commons.fixEnumLists(blockFilter, safeBlocks);
    }

    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (!plugin.config.getBoolean("options.general.useOnlySafeBlocks") || safeBlocks.contains(event.getBlock().getType().toString())) {
            if (plugin.config.getBoolean("options.blocks.ignoreEnchantmentBug") || !enchantBugPresent(event)) {
                if (plugin.config.get("options.blocks.filterMode").equals("blacklist")) {
                    if (!blockFilter.contains(event.getBlock().getType().toString())) {
                        this.moveBlockDropToInventory(event);
                    }
                } else if (plugin.config.get("options.blocks.filterMode").equals("whitelist")) {
                    if (blockFilter.contains(event.getBlock().getType().toString())) {
                        this.moveBlockDropToInventory(event);
                    }
                } else {
                    this.moveBlockDropToInventory(event);
                }
            }
        }
    }

    private boolean enchantBugPresent(BlockBreakEvent event) {
        Collection<Enchantment> buggedEnchants = null;
        buggedEnchants.add(Enchantment.LOOT_BONUS_BLOCKS);
        buggedEnchants.add(Enchantment.SILK_TOUCH);
        for (Enchantment enchantment : buggedEnchants) {
            if (event.getPlayer().getInventory().getItemInHand().getEnchantmentLevel(enchantment) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * extracts drop info from block break event and passes it further, managing
     * block break necessities
     *
     * @param event block break event
     */
    private void moveBlockDropToInventory(BlockBreakEvent event) {
        plugin.moveDropToInventory(event.getPlayer(), event.getBlock().getDrops(event.getPlayer().getItemInHand()), event.getExpToDrop(), event.getBlock().getLocation());
        event.setCancelled(true);
        event.getBlock().setTypeId(Material.AIR.getId());
    }

}
