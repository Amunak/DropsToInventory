package net.amunak.bukkit.plugin_DropsToInventory;

/**
 *
 * @author Amunak
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class DropsToInventory extends JavaPlugin implements Listener {

    protected static Log log;
    protected FileConfiguration config;
    public List<String> filter;

    @Override
    public void onEnable() {
        log = new Log(this);
        log.info("Plugin enabled");

        this.saveDefaultConfig();
        config = this.getConfig();
        filter = config.getStringList("filter");
        for (String string : filter) {
            string = string.toUpperCase();
        }

        if (config.getBoolean("options.checkVersion")) {
            CheckVersion.check(this);
        }

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((JavaPlugin) this);
        log.info("Plugin disabled");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        // If something (other plugin) cancelled the event, we want to ignore it
        if (!event.isCancelled()) {
            if (config.get("options.filterMode").equals("blacklist")) {
                if (!filter.contains(event.getBlock().getType().toString())) {
                    this.moveToInventory(event);
                }
            } else if (config.get("options.filterMode").equals("whitelist")) {
                if (filter.contains(event.getBlock().getType().toString())) {
                    this.moveToInventory(event);
                }
            }
        }
    }

    private void moveToInventory(BlockBreakEvent event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}