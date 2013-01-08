package net.amunak.bukkit.plugin_DropsToInventory;

import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Log class is used to log events to console.
 *
 * @author Amunak
 */
public final class Log {

    private static Logger log;
    public String consolePrefix;
    public String playerPrefix;

    /**
     * This method initializes log line prefixes
     *
     * @param plugin the plugin
     */
    Log(JavaPlugin plugin) {
        log = Logger.getLogger("Minecraft");
        consolePrefix = "[" + plugin.getDescription().getName() + "]: ";
        playerPrefix = ChatColor.RESET + "[" + ChatColor.GOLD + plugin.getDescription().getName() + ChatColor.RESET + "]: ";
    }

    public void info(String message) {
        this.info(null, message);
    }

    public void warning(String message) {
        this.warning(null, message);
    }

    public void info(Player player, String message) {
        if (player == null) {
            log.info(consolePrefix + message);
        } else {
            player.sendMessage(playerPrefix + message);
        }
    }

    public void warning(Player player, String message) {
        if (player == null) {
            log.warning(consolePrefix + message);
        } else {
            player.sendMessage(playerPrefix + ChatColor.DARK_RED + message);
        }
    }
}
