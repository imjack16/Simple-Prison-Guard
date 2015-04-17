package me.imjack.arkham.events;

import me.imjack.arkham.GuardManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropEvent implements Listener {

	@EventHandler
	public void guardDrop(PlayerDropItemEvent event) {
		Player player = (Player) event.getPlayer();
		if (!player.hasPermission("Jail.duty.command")) {
			return;
		}
		
		if(GuardManager.getManager().getGuardData(player.getUniqueId()) == null){
			return;
		}
		
		if (GuardManager.getManager().getGuardData(player.getUniqueId()).isOnDuty()) {
			event.setCancelled(true);
		}
	}
}
