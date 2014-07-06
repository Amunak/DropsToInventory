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

import org.bukkit.ChatColor;

/**
 * Creator: Revolution
 * Project: DropsToInv
 * Date: 6/16/2014, 11:36 AM
 * Usage:
 */
public class YamlStringUtils {

    public static String getStringFromConfig(String target) {
        String s = DropsToInventory.getInstance().getConfig().getString(target);
        s = parseString(s);
        return s;
    }

    public static String parseString(String s) {
        String ss = s;
        ss = ss.replace("$Gold$", ChatColor.GOLD.toString());
        ss = ss.replace("$Aqua$", ChatColor.AQUA.toString());
        ss = ss.replace("$Black$", ChatColor.BLACK.toString());
        ss = ss.replace("$Blue$", ChatColor.BLUE.toString());
        ss = ss.replace("$Bold$", ChatColor.BOLD.toString());
        ss = ss.replace("$DarkAqua$", ChatColor.DARK_AQUA.toString());
        ss = ss.replace("$DarkBlue$", ChatColor.DARK_BLUE.toString());
        ss = ss.replace("$DarkGrey$", ChatColor.DARK_GRAY.toString());
        ss = ss.replace("$DarkGreen$", ChatColor.DARK_GREEN.toString());
        ss = ss.replace("$Purple$", ChatColor.DARK_PURPLE.toString());
        ss = ss.replace("$DarkRed$", ChatColor.DARK_RED.toString());
        ss = ss.replace("$Gray$", ChatColor.GRAY.toString());
        ss = ss.replace("$Green$", ChatColor.GREEN.toString());
        ss = ss.replace("$Italic$", ChatColor.ITALIC.toString());
        ss = ss.replace("$LightPurple", ChatColor.LIGHT_PURPLE.toString());
        ss = ss.replace("$Magic$", ChatColor.MAGIC.toString());
        ss = ss.replace("$Red$", ChatColor.RED.toString());
        ss = ss.replace("$Reset$", ChatColor.RESET.toString());
        ss = ss.replace("$Strikethrough$", ChatColor.STRIKETHROUGH.toString());
        ss = ss.replace("$Underline$", ChatColor.UNDERLINE.toString());
        ss = ss.replace("$White$", ChatColor.WHITE.toString());
        ss = ss.replace("$Yellow$", ChatColor.YELLOW.toString());
        ss = ss.replace("$NumberSign$", "#");
        ss = ss.replace("$Colon$", ":");
        return ss;
    }
}