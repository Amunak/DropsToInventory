package net.amunak.bukkit.dropstoinventory;

import org.bukkit.plugin.java.JavaPlugin;

public class DropsToInventory extends JavaPlugin {
	@Override
	public void onEnable() {
		super.onEnable();

		getServer().getPluginManager().registerEvents(new DropEventListener(this), this);
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
