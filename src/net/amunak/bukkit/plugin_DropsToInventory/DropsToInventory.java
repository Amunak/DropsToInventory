package net.amunak.bukkit.plugin_DropsToInventory;

/**
 *
 * @author Amunak
 */
import org.bukkit.plugin.java.JavaPlugin;
 
public final class DropsToInventory extends JavaPlugin {
    
    protected static Log log;
 
    @Override
    public void onEnable(){
        log = new Log(this);
        log.info("Plugin enabled");
    }
 
    @Override
    public void onDisable() {
        log.info("Plugin disabled");
    }
}