package net.amunak.bukkit.plugin_DropsToInventory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class checks for version
 *
 * @author Amunak
 */
public final class CheckVersion {

    protected static Log log;

    /**
     * Checks version, returns output to player (or console if no player)
     *
     * @param player the player (null for console)
     * @param plugin the plugin
     */
    public static void check(Player player, JavaPlugin plugin) {
        URL u;
        InputStream is;
        String s;
        Integer version;
        
        log = new Log(plugin);
        version = (int) (Integer.parseInt(plugin.getDescription().getVersion().replace(".", "")));

        log.info(player, "Checking for updates...");

        try {
            u = new URL("http://mcplugins.amunak.net/query.php?v=" + version + "&p=" + plugin.getDescription().getName());
            is = u.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            while ((s = br.readLine()) != null) {
                log.info(player, s);
            }
            is.close();
        } catch (Exception e) {
            log.info(player, "No response");
        }
    }

    /**
     * Checks version, logs to console
     *
     * @param plugin the plugin
     */
    public static void check(JavaPlugin plugin) {
        check(null, plugin);
    }
}
