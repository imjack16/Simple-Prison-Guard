package me.imjack.arkham.events;

import java.io.File;
import java.io.IOException;

import me.imjack.arkham.GuardManager;
import me.imjack.arkham.JailDuty;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.kill3rtaco.tacoserialization.InventorySerialization;

public class OnRespawnEvent implements Listener {

	JailDuty plugin;

	public OnRespawnEvent(JailDuty instance) {
		plugin = instance;
	}

	@EventHandler
	public void guardReSpawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		File file = new File(plugin.getDataFolder() + File.separator + "Guard Data", player.getUniqueId() + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

		if (!player.hasPermission("Jail.duty.command")) {
			return;
		}

		if(GuardManager.getManager().getGuardData(player.getUniqueId()) == null){
			return;
		}

		if (GuardManager.getManager().getGuardData(player.getUniqueId()).isOnDuty()) {
			config.set("onGuard", false);

			try {
				config.save(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			GuardManager.getManager().getGuardData(player.getUniqueId()).setOnDuty(false);
			InventorySerialization.setPlayerInventory(player,
					GuardManager.getManager().getGuardData(player.getUniqueId()).getInventory());
		}
	}
}
