package me.imjack.arkham.events;

import java.io.File;

import me.imjack.arkham.JailDuty;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ItemClickEvent implements Listener {
	static JailDuty plugin;

	public ItemClickEvent(JailDuty instance) {
		plugin = instance;
	}

	@EventHandler
	public void Invclick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			File folder = new File(plugin.getDataFolder() + "/", "Guard Data");
			File GuardInv = new File(folder, player.getUniqueId().toString() + ".yml");
			YamlConfiguration Guardconfig = YamlConfiguration.loadConfiguration(GuardInv);
			if (GuardInv.exists()) {
				if (Guardconfig.getBoolean("onGuard") == true) {
					event.setCancelled(true);
				}
			}
		}
	}
}
