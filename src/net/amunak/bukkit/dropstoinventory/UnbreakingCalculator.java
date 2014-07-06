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

import java.util.Random;

/**
 * Creator: Revolution
 * Project: DropsToInv
 * Date: 6/16/2014, 1:10 PM
 * Usage: Calculates Unbreaking Damage
 */
public class UnbreakingCalculator {

    /**
     * This is the official formula from minecraft.gamepedia.com/Enchanting
     * @param level of unbreaking
     * @return block damage
     */
    public static int calculateDamage(int level) {
        int returnValue = 0;
        int percentage = (100/(level + 1));
        Random rand = new Random();
        int chance = rand.nextInt(100);
        if(percentage > chance) {
            return 0;
        }else {
            return 1;
        }
    }
}