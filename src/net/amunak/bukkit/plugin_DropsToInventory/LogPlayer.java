package net.amunak.bukkit.plugin_DropsToInventory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The LogPlayer class is used to send information (like command output) to the
 * player.
 *
 * @author Amunak
 */
public final class LogPlayer {

    private String logPrefix;

    public LogPlayer(JavaPlugin plugin) {
        logPrefix = ChatColor.RESET + "[" + ChatColor.GOLD + plugin.getDescription().getName() + ChatColor.RESET + "]: ";
    }

    public void info(Player player, String message) {
        player.sendMessage(logPrefix + message);
    }

    public void warning(Player player, String message) {
        player.sendMessage(logPrefix + ChatColor.DARK_RED + message);
    }
}
