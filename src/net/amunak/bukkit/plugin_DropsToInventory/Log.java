package net.amunak.bukkit.plugin_DropsToInventory;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The Log class is used to log events to console.
 *
 * @author Amunak
 */
public final class Log {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static String logPrefix;

    public Log(JavaPlugin plugin) {
        logPrefix = "[" + plugin.getDescription().getName() + "]: ";
    }

    public void info(String msg) {
        log.info(logPrefix + msg);
    }

    public void warning(String msg) {
        log.warning(logPrefix + msg);
    }
}
