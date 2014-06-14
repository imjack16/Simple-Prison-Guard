package me.imjack.arkham.events;

import java.io.File;
import java.util.List;

import me.imjack.arkham.JailDuty;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnDeathEvent implements Listener {
	static JailDuty plugin;

	public OnDeathEvent(JailDuty instance) {
		plugin = instance;
	}

	@EventHandler
	public void GuardDeath(PlayerDeathEvent event) {
		Player player = event.getEntity().getPlayer();
		File folder = new File(plugin.getDataFolder() + "/", "Guard Data");
		File GuardInv = new File(folder, player.getUniqueId().toString() + ".yml");
		YamlConfiguration Guardconfig = YamlConfiguration
				.loadConfiguration(GuardInv);
		if (GuardInv.exists()) {
			if (Guardconfig.getBoolean("onGuard") == true) {
				List<String> reward = plugin.getConfig().getStringList("Rewards");
				event.getDrops().clear();
				if (event.getEntity().getKiller() instanceof Player) {
					Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',  ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "]" + ChatColor.RESET + plugin.getConfig().getString("Guard-Death-Message").replace("%p", player.getName())));
			            for (String string : reward){
			                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replace("%p", event.getEntity().getKiller().getName()));
			            }
						return;
				}
				Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',  ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "]" + ChatColor.RESET + plugin.getConfig().getString("Duty-dead-Message").replace("%p", player.getName())));
				return;
			}
		}
	}
}
