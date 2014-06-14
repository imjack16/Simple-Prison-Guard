package me.imjack.arkham;

import java.io.File;

import me.imjack.arkham.commands.GuardCommandExecutor;
import me.imjack.arkham.events.ItemClickEvent;
import me.imjack.arkham.events.ItemDropEvent;
import me.imjack.arkham.events.OnDeathEvent;
import me.imjack.arkham.events.OnPlayerLoginEvent;
import me.imjack.arkham.events.OnRespawnEvent;
import me.imjack.arkham.events.PreProcessCommandEvent;
import me.imjack.arkham.events.onPlayerInteractEvent;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class JailDuty extends JavaPlugin{
	public static JailDuty plugin;	
	public void onEnable() {
		
		PluginManager manager = this.getServer().getPluginManager();
		File folder = new File(this.getDataFolder(), "Guard Data");
		getConfig().options().copyDefaults(true);
		saveConfig();
		if(!folder.exists()){
				folder.mkdir();
		}
		getCommand("duty").setExecutor(new GuardCommandExecutor(this));
		manager.registerEvents(new ItemClickEvent(this), this);
		manager.registerEvents(new ItemDropEvent(this), this);
		manager.registerEvents(new OnDeathEvent(this), this);
		manager.registerEvents(new OnRespawnEvent(this), this);
		manager.registerEvents(new PreProcessCommandEvent(this), this);
		manager.registerEvents(new OnPlayerLoginEvent(this), this);
		manager.registerEvents(new onPlayerInteractEvent(this), this);
	}

	public void onDisable() {

	}
}