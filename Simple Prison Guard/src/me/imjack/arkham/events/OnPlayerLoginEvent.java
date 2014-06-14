package me.imjack.arkham.events;

import me.imjack.arkham.JailDuty;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerLoginEvent implements Listener {
	static JailDuty plugin;

	public OnPlayerLoginEvent(JailDuty instance) {
		plugin = instance;
	}

	@EventHandler
	public void playerSpawn(PlayerJoinEvent event) {
		Player player = (Player)event.getPlayer();
		if(plugin.getConfig().getBoolean("MOTD-On") == true){
			player.sendMessage(ChatColor.GRAY + "This server uses imjack16's Plugin for the Guards!");
		}
		
	}
}
