package me.imjack.arkham.events;

import java.util.Random;
import java.util.Map.Entry;

import me.imjack.arkham.GuardManager;
import me.imjack.arkham.JailDuty;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnDeathEvent implements Listener {
	JailDuty plugin;

	public OnDeathEvent(JailDuty instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void GuardDeath(PlayerDeathEvent event) {
		Player player = event.getEntity().getPlayer();// person who died
		if (!player.hasPermission("Jail.duty.command")) {
			return;
		}

		if (GuardManager.getManager().getGuardData(player.getUniqueId()) == null) {
			return;
		}
		if (player.getKiller() instanceof Player) {
			if (GuardManager.getManager().getGuardData(player.getUniqueId()).isOnDuty()) {
				event.getDrops().clear();
				event.setDroppedExp(0);
				for (String reward : plugin.guaranteedRewards) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							reward.replace("%p", event.getEntity().getKiller().getName()));
				}
				Bukkit.getServer().broadcastMessage(
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Guard-Death-Message")
								.replace("%p", player.getName()).replace("%k", player.getKiller().getName())));
				Random random = new Random();
				int timesWon = 0;
				double rand;
				for (Entry<Double, String> entry : plugin.randomRewards.entries()) {
					rand = random.nextDouble() * 100;
					if (entry.getKey() > rand) {
						if (timesWon != plugin.maxRandomRewards) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									entry.getValue().replace("%p", event.getEntity().getKiller().getName()));
							timesWon++;
						}
					}
				}
			}
		} else {
			event.getDrops().clear();
			event.setDroppedExp(0);
		}
	}
}
