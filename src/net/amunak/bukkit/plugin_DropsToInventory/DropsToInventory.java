package net.amunak.bukkit.plugin_DropsToInventory;

/**
 *
 * @author Amunak
 */
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
 
public final class DropsToInventory extends JavaPlugin {
    
    protected static Log log;
    protected FileConfiguration config;
 
    @Override
    public void onEnable(){
        log = new Log(this);
        log.info("Plugin enabled");
        
        this.saveDefaultConfig();
        config = this.getConfig();
        
        if(config.getBoolean("options.checkVersion"))
        {
            CheckVersion.check(this);
        }
    }
 
    @Override
    public void onDisable() {
        log.info("Plugin disabled");
    }
}