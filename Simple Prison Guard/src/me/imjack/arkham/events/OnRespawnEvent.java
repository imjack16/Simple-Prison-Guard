package me.imjack.arkham.events;

import java.io.File;
import java.io.IOException;

import me.imjack.arkham.JailDuty;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.kill3rtaco.tacoserialization.InventorySerialization;

public class OnRespawnEvent implements Listener {
	static JailDuty plugin;

	public OnRespawnEvent(JailDuty instance) {
		plugin = instance;
	}

	@EventHandler
	public void guardReSpawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		File folder = new File(plugin.getDataFolder(), "Guard Data");
		File GuardInv = new File(folder, player.getUniqueId().toString() + ".yml");
		YamlConfiguration Guardconfig = YamlConfiguration.loadConfiguration(GuardInv);
		if (GuardInv.exists()) {
			if (Guardconfig.getBoolean("onGuard") == true) {
				Guardconfig.set("onGuard", false);
				InventorySerialization.setPlayerInventory(player, Guardconfig.getString("Inventory"));
					try {
						Guardconfig.save(GuardInv);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
}
