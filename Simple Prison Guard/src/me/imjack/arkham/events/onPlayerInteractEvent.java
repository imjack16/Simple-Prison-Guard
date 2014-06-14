package me.imjack.arkham.events;

import java.io.File;
import me.imjack.arkham.JailDuty;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class onPlayerInteractEvent implements Listener {
	static JailDuty plugin;

	public onPlayerInteractEvent(JailDuty instance) {
		plugin = instance;
	}

	@EventHandler
	public void guardReSpawn(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		File folder = new File(plugin.getDataFolder(), "Guard Data");
		File GuardInv = new File(folder, player.getUniqueId().toString() + ".yml");
		YamlConfiguration Guardconfig = YamlConfiguration.loadConfiguration(GuardInv);
		if (GuardInv.exists()) {
			if (Guardconfig.getBoolean("onGuard") == true) {
				if (event.getRightClicked() instanceof ItemFrame) {
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "] " + ChatColor.RED + "You Can't use item frames on duty!");
					event.setCancelled(true);
				}
			}
		}
	}
}
