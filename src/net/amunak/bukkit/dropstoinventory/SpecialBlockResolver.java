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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Revolution
 * Project: DropsToInv
 * Date: 6/16/2014, 10:22 AM
 * Usage: Resolves the other part of the "bed" item in minecraft as well as other multiblock structures such as doors
 */
public class SpecialBlockResolver {

    public static Location getBed(Location l) {
        return checkForMaterial(l, Material.BED_BLOCK);
    }

    public static Location getDoor(Location l) {
        Location returnV = checkForMaterial(l, Material.IRON_DOOR_BLOCK);
        if(returnV == null)
            returnV = checkForMaterial(l, Material.WOODEN_DOOR);
        return returnV;
    }

    public static Location getPiston(Location l) {
        Location returnV = checkForMaterial(l, Material.PISTON_BASE);
        if(returnV == null) {
            returnV = checkForMaterial(l, Material.PISTON_EXTENSION);
            if(returnV == null) {
                returnV = checkForMaterial(l, Material.PISTON_MOVING_PIECE);
                if(returnV == null) {
                    returnV = checkForMaterial(l, Material.PISTON_STICKY_BASE);
                    return returnV;
                }else {
                    return returnV;
                }
            }else {
                return returnV;
            }
        }else {
            return returnV;
        }
    }

    private static Location checkForMaterial(Location l, Material mat) {
        Location returnV = null;
        List<Location> locs = new ArrayList<Location>();
        locs.add(new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ()));
        locs.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() + 1));
        locs.add(new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ() + 1));
        locs.add(new Location(l.getWorld(), l.getX() - 1, l.getY(), l.getZ()));
        locs.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() - 1));
        locs.add(new Location(l.getWorld(), l.getX() - 1, l.getY(), l.getZ() -1));
        locs.add(new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ() - 1));
        locs.add(new Location(l.getWorld(), l.getX() - 1, l.getY(), l.getZ() + 1));
        locs.add(new Location(l.getWorld(), l.getX() + 1, l.getY() + 1, l.getZ()));
        locs.add(new Location(l.getWorld(), l.getX(), l.getY() + 1, l.getZ() + 1));
        locs.add(new Location(l.getWorld(), l.getX() + 1, l.getY() + 1, l.getZ() + 1));
        locs.add(new Location(l.getWorld(), l.getX() - 1, l.getY() + 1, l.getZ()));
        locs.add(new Location(l.getWorld(), l.getX(), l.getY() + 1, l.getZ() - 1));
        locs.add(new Location(l.getWorld(), l.getX() - 1, l.getY() + 1, l.getZ() -1));
        locs.add(new Location(l.getWorld(), l.getX() + 1, l.getY() + 1, l.getZ() - 1));
        locs.add(new Location(l.getWorld(), l.getX() - 1, l.getY() + 1, l.getZ() + 1));
        locs.add(new Location(l.getWorld(), l.getX() + 1, l.getY() - 1, l.getZ()));
        locs.add(new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ() + 1));
        locs.add(new Location(l.getWorld(), l.getX() + 1, l.getY() - 1, l.getZ() + 1));
        locs.add(new Location(l.getWorld(), l.getX() - 1, l.getY() - 1, l.getZ()));
        locs.add(new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ() - 1));
        locs.add(new Location(l.getWorld(), l.getX() - 1, l.getY() - 1, l.getZ() -1));
        locs.add(new Location(l.getWorld(), l.getX() + 1, l.getY() - 1, l.getZ() - 1));
        locs.add(new Location(l.getWorld(), l.getX() - 1, l.getY() - 1, l.getZ() + 1));
        for(Location ll : locs) {
            Block b = ll.getBlock();
            if(b.getType() == mat) {
                returnV = ll;
                break;
            }
        }
        return returnV;
    }
}