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
    public Integer filterMode;
    public Boolean useSafeBlocks;
    public Boolean fixEnchantmentBug;

    public BlockBreakEventListener(DropsToInventory p) {
        plugin = p;
        filterMode = BlockFilter.fromString(plugin.config.getString("options.blocks.filterMode"));
        useSafeBlocks = plugin.config.getBoolean("options.blocks.useOnlySafeBlocks");
        fixEnchantmentBug = !plugin.config.getBoolean("options.blocks.ignoreEnchantmentBug");

        if (!filterMode.equals(BlockFilter.NONE)) {
            blockFilter = plugin.config.getStringList("lists.blockFilter");
        }
        if (useSafeBlocks) {
            safeBlocks = plugin.config.getStringList("lists.safeBlocks");
        }
        Common.fixEnumLists(blockFilter, safeBlocks);
    }

    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (!plugin.config.getBoolean("options.general.useOnlySafeBlocks") || safeBlocks.contains(event.getBlock().getType().toString())) {
            if (plugin.config.getBoolean("options.blocks.ignoreEnchantmentBug") || !enchantBugPresent(event)) {
                if (BlockFilter.isEligible(event.getBlock().getType(), blockFilter, filterMode)) {
                    if (!blockFilter.contains(event.getBlock().getType().toString())) {
                        this.moveBlockDropToInventory(event);
                    }
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

    /**
     * Holds filter modes and methods to retreive them
     */
    private static class BlockFilter {

        public static final Integer NONE = 0;
        public static final Integer BLACKLIST = 1;
        public static final Integer WHITELIST = 2;

        public static String toString(Integer i) {
            if (i.equals(BLACKLIST)) {
                return "BLACKLIST";
            } else if (i.equals(WHITELIST)) {
                return "WHITELIST";
            } else {
                return "NONE";
            }
        }

        public static Integer fromString(String s) {
            if (s.equalsIgnoreCase("BLACKLIST")) {
                return BLACKLIST;
            } else if (s.equalsIgnoreCase("WHITELIST")) {
                return WHITELIST;
            } else {
                return NONE;
            }
        }

        /**
         * checks whether a block is eligible to be used according to a filter
         * mode
         *
         * @param isInList is block in list
         * @param mode filter mode to check against
         * @return true if elegible, else otherwise
         */
        public static Boolean isEligible(boolean isInList, Integer mode) {
            return mode.equals(NONE) || (mode.equals(BLACKLIST) && !isInList) || (mode.equals(WHITELIST) && isInList);
        }

        /**
         * checks a block material against list and if it is eligible to be used
         * according to a filter mode
         *
         * @param mat the material
         * @param list list of materials (strings)
         * @param mode filter mode to check against
         * @return
         */
        public static Boolean isEligible(Material mat, List list, Integer mode) {
            return isEligible(list.contains(mat.toString()), mode);
        }
    }
}
