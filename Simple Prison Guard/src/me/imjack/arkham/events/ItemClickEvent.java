package me.imjack.arkham.events;

import me.imjack.arkham.GuardManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class ItemClickEvent implements Listener {

	@EventHandler
	public void Invclick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();

			if (!player.hasPermission("Jail.duty.command")) {
				return;
			}

			if (GuardManager.getManager().getGuardData(player.getUniqueId()) == null) {
				return;
			}

			if (GuardManager.getManager().getGuardData(player.getUniqueId()).isOnDuty()) {
				if (event.getAction().equals(InventoryAction.DROP_ALL_CURSOR)
						|| event.getAction().equals(InventoryAction.DROP_ALL_SLOT)
						|| event.getAction().equals(InventoryAction.DROP_ONE_CURSOR)
						|| event.getAction().equals(InventoryAction.DROP_ONE_SLOT)
						|| event.getAction().equals(InventoryAction.UNKNOWN)
						|| event.getView().getType() != InventoryType.CRAFTING) {
					event.setCancelled(true);
				}
			}
		}
	}
}
