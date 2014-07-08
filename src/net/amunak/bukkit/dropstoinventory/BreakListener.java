package net.amunak.bukkit.dropstoinventory;

/**
 * Copyright 2014 Eric Coan
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

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creator: Revolution
 * Project: DropsToInv
 * Date: 6/16/2014, 12:10 PM
 * Usage: Does the block resolution and stuff.
 */
public class BreakListener implements Listener {

    public static List<String> blackListedBlocks;
    public static List<Player> playersTurnedOff;
    private static boolean blackList = false;

    public void getIntialList() {
        blackListedBlocks = DropsToInventory.getInstance().getConfig().getStringList("blacklistedBlocks");
        playersTurnedOff = new ArrayList<Player>();
        if(!(blackListedBlocks == null)) {
            blackList = true;
        }
    }

    //Set to lowest priority so worldguard and things run first
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (blackList) {
            Material mat = event.getBlock().getType();
            if (blackListedBlocks.contains(mat.name())) {
                return;
            }
        }
        if (isFull(event.getPlayer(), event.getBlock().getType())) {
            return;
        }
        if (playersTurnedOff.contains(event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
        Player p = event.getPlayer();
        ItemStack i = p.getItemInHand();
        Block b = event.getBlock();
        int expToDrop = event.getExpToDrop();
        Material mat = b.getType();
        byte data = b.getData();
        boolean silk = i.containsEnchantment(Enchantment.SILK_TOUCH);
        if (!(shouldAdd(mat, i, p))) {
            return;
        }
        if (p.getGameMode() == GameMode.CREATIVE) {
            b.setType(Material.AIR);
            return;
        }
        int damage = 1;
        if (i.containsEnchantment(Enchantment.DURABILITY))
            damage = UnbreakingCalculator.calculateDamage(i.getEnchantmentLevel(Enchantment.DURABILITY));

        b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
        if (isSpecialBlock(mat)) {
            if (mat == Material.PISTON_BASE || mat == Material.PISTON_MOVING_PIECE || mat == Material.PISTON_EXTENSION || mat == Material.PISTON_STICKY_BASE) {
                Location l = SpecialBlockResolver.getPiston(b.getLocation());
                if (l == null) {
                    if(mat == Material.PISTON_STICKY_BASE) {
                        i.setDurability((short) (i.getDurability() - damage));
                        p.getInventory().addItem(new ItemStack(29, 1));
                    }else {
                        i.setDurability((short) (i.getDurability() - damage));
                        p.getInventory().addItem(new ItemStack(33, 1));
                    }
                    b.setType(Material.AIR);
                } else {
                    if (l.getWorld().getBlockAt(l).getType() == Material.PISTON_STICKY_BASE || b.getWorld().getBlockAt(b.getLocation()).getType() == Material.PISTON_STICKY_BASE) {
                        Block bb = l.getWorld().getBlockAt(l);
                        bb.getDrops().clear();
                        b.getDrops().clear();
                        bb.setType(Material.AIR);
                        b.setType(Material.AIR);
                        i.setDurability((short) (i.getDurability() - damage));
                        p.getInventory().addItem(new ItemStack(29, 1));
                    } else {
                        Block bb = l.getWorld().getBlockAt(l);
                        bb.getDrops().clear();
                        b.getDrops().clear();
                        bb.setType(Material.AIR);
                        b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);

                        i.setDurability((short) (i.getDurability() - damage));
                        p.getInventory().addItem(new ItemStack(33, 1));
                    }
                }
            }else if (mat == Material.IRON_DOOR_BLOCK || mat == Material.WOODEN_DOOR || mat == Material.IRON_DOOR) {
                Location l = SpecialBlockResolver.getDoor(b.getLocation());
                if (l == null) {
                    if (mat == Material.IRON_DOOR_BLOCK || mat == Material.IRON_DOOR) {
                        b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
                        i.setDurability((short) (i.getDurability() - damage));
                        p.getInventory().addItem(new ItemStack(330, 1));
                    } else {
                        b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
                        i.setDurability((short) (i.getDurability() - damage));
                        p.getInventory().addItem(new ItemStack(324, 1));
                    }
                } else {
                    if (mat == Material.IRON_DOOR_BLOCK || mat == Material.IRON_DOOR) {
                        l.getWorld().getBlockAt(l).setType(Material.AIR);
                        b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
                        i.setDurability((short) (i.getDurability() - damage));
                        p.getInventory().addItem(new ItemStack(330, 1));
                    } else {
                        l.getWorld().getBlockAt(l).setType(Material.AIR);
                        b.getWorld().getBlockAt(b.getLocation()).setType(Material.AIR);
                        i.setDurability((short) (i.getDurability() - damage));
                        p.getInventory().addItem(new ItemStack(324, 1));
                    }
                }
            }else if (mat == Material.BED_BLOCK) {
                Location l = SpecialBlockResolver.getBed(b.getLocation());
                if (l == null) {
                    b.setType(Material.AIR);
                    i.setDurability((short) (i.getDurability() - damage));
                    p.getInventory().addItem(new ItemStack(355, 1));
                } else {
                    Block bb = l.getWorld().getBlockAt(l);
                    bb.getDrops().clear();
                    b.getDrops().clear();
                    bb.setType(Material.AIR);
                    b.setType(Material.AIR);
                    i.setDurability((short) (i.getDurability() - damage));
                    p.getInventory().addItem(new ItemStack(355, 1));
                }
            }else if(mat == Material.VINE) {
                if(i.getType() == Material.SHEARS) {
                    p.getInventory().addItem(new ItemStack(mat, 1));
                }
            }
        }
        if (mat == Material.IRON_ORE || mat == Material.COAL_ORE || mat == Material.DIAMOND_ORE || mat == Material.EMERALD_ORE || mat == Material.GOLD_ORE || mat == Material.GLOWING_REDSTONE_ORE || mat == Material.LAPIS_ORE || mat == Material.QUARTZ_ORE || mat == Material.REDSTONE_ORE) {
            int returnAmount = 1;

            if(i.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))
                returnAmount = FortuneCalculator.numDroppedFromFortune(i.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS), mat, returnAmount);

            if (mat == Material.IRON_ORE) {
                p.getInventory().addItem(new ItemStack(Material.IRON_ORE, 1));
            }else if(mat == Material.COAL_ORE) {
                if(silk)
                    p.getInventory().addItem(new ItemStack(Material.COAL_ORE, returnAmount));
                else
                    p.getInventory().addItem(new ItemStack(Material.COAL, returnAmount));
            }else if(mat == Material.DIAMOND_ORE) {
                if(silk)
                    p.getInventory().addItem(new ItemStack(Material.DIAMOND_ORE, returnAmount));
                else
                    p.getInventory().addItem(new ItemStack(Material.DIAMOND, returnAmount));
            }else if(mat == Material.EMERALD_ORE) {
                if(silk)
                    p.getInventory().addItem(new ItemStack(Material.EMERALD_ORE, returnAmount));
                else
                    p.getInventory().addItem(new ItemStack(Material.EMERALD, returnAmount));
            }else if(mat == Material.GOLD_ORE) {
                p.getInventory().addItem(new ItemStack(Material.GOLD_ORE, 1));
            }else if(mat == Material.GLOWING_REDSTONE_ORE || mat == Material.REDSTONE_ORE) {
                if(silk)
                    p.getInventory().addItem(new ItemStack(Material.REDSTONE_ORE, returnAmount));
                else
                    p.getInventory().addItem(new ItemStack(Material.REDSTONE, returnAmount));
            }else if(mat == Material.LAPIS_ORE) {
                if(silk)
                    p.getInventory().addItem(new ItemStack(Material.LAPIS_ORE, returnAmount));
                else
                    p.getInventory().addItem(new ItemStack(351, returnAmount, (byte) 4));
            }else if(mat == Material.QUARTZ_ORE) {
                if(silk)
                    p.getInventory().addItem(new ItemStack(Material.QUARTZ_ORE, returnAmount));
                else
                    p.getInventory().addItem(new ItemStack(Material.QUARTZ, returnAmount));
            }
            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat == Material.GRAVEL) {
            Material gravOrFlint = null;
            if(i.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                gravOrFlint = FortuneCalculator.specialGravelDrop(i.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS));
            }else {
                Random rand = new Random();
                int chance = 10;
                int finals = rand.nextInt(100);
                if(chance > finals) {
                    gravOrFlint = Material.FLINT;
                }else {
                    gravOrFlint = Material.GRAVEL;
                }
            }

            if (gravOrFlint == null)
                p.getInventory().addItem(new ItemStack(Material.GRAVEL, 1));
            else
                p.getInventory().addItem(new ItemStack(gravOrFlint, 1));

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat == Material.CAKE_BLOCK) {
            p.getInventory().addItem(new ItemStack(Material.CAKE, 1));
            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat.getId() == 18) {
            //Leaves
            if (silk)
                p.getInventory().addItem(new ItemStack(18, 1, data));

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat.getId() == 20) {
            if (silk)
                p.getInventory().addItem(new ItemStack(20, 1));

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat == Material.WEB) {
            if (i.getType() == Material.SHEARS || silk)
                p.getInventory().addItem(new ItemStack(Material.WEB, 1));

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat.getId() == 79) {
            //Ice Block
            if (silk)
                p.getInventory().addItem(new ItemStack(79, 1));

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat.getId() == 95) {
            //Stained glass
            if (silk)
                p.getInventory().addItem(new ItemStack(95, 1, data));

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat.getId() == 102) {
            //Glass pane
            if (silk)
                p.getInventory().addItem(new ItemStack(102, 1));

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat == Material.MELON_BLOCK) {
            int returnAmount = 4;

            if (i.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))
                returnAmount = FortuneCalculator.numDroppedFromFortune(i.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS), mat, returnAmount);

            p.getInventory().addItem(new ItemStack(Material.MELON, returnAmount));
            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat.getId() == 160) {
            //Stained glass pane
            if (silk)
                p.getInventory().addItem(new ItemStack(160, 1, data));

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat.getId() == 161) {
            //MOAR Leaves
            if (silk)
                p.getInventory().addItem(new ItemStack(161, 1, data));

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat.getId() == 174) {
            //Packed ice
            if (silk)
                p.getInventory().addItem(new ItemStack(174, 1));
            else
                b.setType(Material.WATER);

            i.setDurability((short) (i.getDurability() - damage));
        }else if(mat == Material.GRASS || mat == Material.STONE) {
            if(silk) {
                p.getInventory().addItem(new ItemStack(mat, 1));
            }else {
                if(mat == Material.GRASS) {
                    p.getInventory().addItem(new ItemStack(Material.DIRT, 1));
                }else {
                    p.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 1));
                }
                i.setDurability((short) (i.getDurability() - damage));
            }
        }else {
            p.getInventory().addItem(new ItemStack(mat, 1, data));
            i.setDurability((short) (i.getDurability() - damage));
        }
        p.setTotalExperience((p.getTotalExperience() + expToDrop));
        if(i.getDurability() == 0) {
            i.setType(Material.AIR);
            p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
            if(expToDrop > 0) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            }
        }
    }

    private static boolean isSpecialBlock(Material mat) {
        if(mat == Material.PISTON_BASE || mat == Material.PISTON_MOVING_PIECE || mat == Material.PISTON_STICKY_BASE || mat == Material.PISTON_BASE || mat == Material.IRON_DOOR_BLOCK || mat == Material.WOODEN_DOOR || mat == Material.BED_BLOCK || mat == Material.IRON_DOOR || mat == Material.VINE)
            return true;
        else
            return false;
    }

    public static boolean isFull(Player p, Material mat) {
        int count = 0;
        for (int i=0; i<= p.getInventory().getSize() - 1; i++) {
            try {
                if(p.getInventory().getContents()[i].getType() == Material.AIR) {}else {
                    if(p.getInventory().getContents()[i].getType() == mat && p.getInventory().getContents()[i]
                            .getAmount() < p.getInventory().getContents()[i].getMaxStackSize()) {}else {
                        count++;
                    }
                }
            }catch(NullPointerException e) {}
        }
        if(count == p.getInventory().getSize()) {
            return true;
        }else {
            return false;
        }
    }

    private static boolean shouldAdd(Material mat, ItemStack is, Player p) {
        //Tool providings done by: minecraft.gamepedia.com/Digging#Blocks_by_hardness
        if(mat == Material.BEDROCK || mat == Material.COMMAND || mat == Material.ENDER_PORTAL || mat == Material.ENDER_PORTAL_FRAME || mat == Material.LAVA || mat == Material.STATIONARY_LAVA || mat == Material.WATER || mat == Material.STATIONARY_WATER) {
            return false;
        }else if(mat == Material.OBSIDIAN) {
            if(is.getType() == Material.DIAMOND_PICKAXE)
                return true;
            else
                return false;
        }else if(mat == Material.ENDER_CHEST || mat == Material.ANVIL || mat == Material.COAL_BLOCK
                || mat == Material.REDSTONE_BLOCK || mat == Material.ENCHANTMENT_TABLE ||
                mat == Material.IRON_BARDING || mat == Material.IRON_DOOR_BLOCK || mat == Material.MOB_SPAWNER ||
                mat == Material.DISPENSER || mat == Material.DROPPER || mat == Material.COAL_ORE ||
                mat == Material.ENDER_STONE || mat == Material.HOPPER || mat == Material.QUARTZ_ORE ||
                mat == Material.BRICK_STAIRS || mat == Material.BRICK || mat == Material.CAULDRON ||
                mat == Material.COBBLESTONE || mat == Material.COBBLESTONE_STAIRS || mat == Material.COBBLE_WALL ||
                mat == Material.MOSSY_COBBLESTONE || mat == Material.NETHER_BRICK || mat == Material.NETHER_BRICK_STAIRS
                || mat == Material.STONE_PLATE || (mat.getId() == 43 || mat.getId() == 44) || mat == Material.STONE ||
                (mat.getId() == 94 || mat.getId() == 109) || (mat.getId() == 159 || mat.getId() == 172) ||
                mat == Material.QUARTZ_BLOCK || mat == Material.SANDSTONE || mat == Material.SANDSTONE_STAIRS ||
                mat == Material.NETHERRACK) {
                    if(is.getType() == Material.WOOD_PICKAXE || is.getType() == Material.STONE_PICKAXE ||
                            is.getType() == Material.IRON_PICKAXE || is.getType() == Material.DIAMOND_PICKAXE)
                        return true;
                    else
                        return false;
        }else if(mat == Material.IRON_BLOCK || mat == Material.IRON_ORE || mat == Material.LAPIS_BLOCK || mat ==
                Material.LAPIS_ORE) {
            if(is.getType() == Material.STONE_PICKAXE || is.getType() == Material.IRON_PICKAXE || is.getType() ==
                    Material.DIAMOND_PICKAXE)
                return true;
            else
                return false;
        }else if(mat == Material.DIAMOND_ORE || mat == Material.EMERALD_ORE || mat == Material.GOLD_ORE || mat ==
                Material.REDSTONE_ORE || mat == Material.GLOWING_REDSTONE_ORE) {
                if(is.getType() == Material.IRON_PICKAXE || is.getType() == Material.DIAMOND_PICKAXE)
                    return true;
                else
                    return false;
        }else {
            return true;
        }
    }
}
