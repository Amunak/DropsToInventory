package net.amunak.bukkit.dropstoinventory;

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

import java.util.List;

/**
 * contains common static methods
 *
 * @author Amunak
 */
public final class Common {

    /**
     * fixes bukkit enumeration lists for easier user input
     *
     * @param lists array of List<String>
     */
    public static void fixEnumLists(List<String>... lists) {
        for (List<String> list : lists) {
            for (String string : list) {
                string = string.toUpperCase();
                string = string.replace(" ", "_");
            }
        }
    }
}
