package me.imjack.arkham.events;

import java.io.File;
import java.util.List;

import me.imjack.arkham.JailDuty;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreProcessCommandEvent implements Listener {
	static JailDuty plugin;

	public PreProcessCommandEvent(JailDuty instance) {
		plugin = instance;
	}

	@EventHandler
	public void ProcessCommand(PlayerCommandPreprocessEvent event) {
		Player player = (Player) event.getPlayer();
		File folder = new File(plugin.getDataFolder() + "/", "Guard Data");
		File GuardInv = new File(folder, player.getUniqueId().toString() + ".yml");
		YamlConfiguration Guardconfig = YamlConfiguration
				.loadConfiguration(GuardInv);
		if (Guardconfig.getBoolean("onGuard") == true) {
			List<String> Commands = plugin.getConfig().getStringList("Banned-Commands");
			for (String string : Commands) {
				if (event.getMessage().toLowerCase().startsWith("/" + string.toLowerCase())) {
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "] " + ChatColor.RED + "You can't use the command " + event.getMessage().toLowerCase() + " whilst on duty!");
					event.setCancelled(true);
				}
			}
		}
	}
}
