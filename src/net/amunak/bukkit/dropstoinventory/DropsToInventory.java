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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Creator: Revolution
 * Project: DropsToInv
 * Date: 6/16/2014, 10:14 AM
 * Usage: Main plugin class.
 */
public class DropsToInventory extends JavaPlugin {

    private static DropsToInventory instance = null;
    private static BreakListener bl = null;

    @Override
    public void onEnable() {
        instance = this;
        bl = new BreakListener();
        bl.getIntialList();
        Bukkit.getPluginManager().registerEvents(bl, instance);
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(instance);
    }

    public static DropsToInventory getInstance() {return instance;}

    public static void sendMessage(UUID uuid, String message) {sendMessage(Bukkit.getPlayer(uuid.toString()), message);}

    public static void sendMessage(Player p, String message) {
        p.sendMessage(YamlStringUtils.getStringFromConfig("ChatPrefix") + message);
    }
}