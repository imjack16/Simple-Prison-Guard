package me.imjack.arkham;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import me.imjack.arkham.commands.DutyCommand;
import me.imjack.arkham.commands.GuardsCommand;
import me.imjack.arkham.events.ItemClickEvent;
import me.imjack.arkham.events.ItemDropEvent;
import me.imjack.arkham.events.OnBlockPlace;
import me.imjack.arkham.events.OnDeathEvent;
import me.imjack.arkham.events.OnPlayerInteractEvent;
import me.imjack.arkham.events.OnRespawnEvent;
import me.imjack.arkham.events.CommandRunEvent;
import me.imjack.arkham.events.OnBlockBrake;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class JailDuty extends JavaPlugin {

	public Logger logger = getLogger();
	public HashMap<Runnable, Integer> titles = new HashMap<Runnable, Integer>();
	public List<String> guaranteedRewards = new ArrayList<String>();
	public List<String> bannedCommands = new ArrayList<String>();
	public List<String> guardOnlyCommands = new ArrayList<String>();
	public Multimap<Double, String> randomRewards = ArrayListMultimap.create();
	public int maxRandomRewards;
	public String kit;
	public String wepTitle;
	public String wepSub;
	public String potTitle;
	public String potSub;
	public int titleTime;

	public void onEnable() {
		PluginManager manager = this.getServer().getPluginManager();
		getConfig().options().copyDefaults(true);
		saveConfig();
		setupConfig();
		loadGuards();
		manager.registerEvents(new ItemClickEvent(), this);
		manager.registerEvents(new ItemDropEvent(), this);
		manager.registerEvents(new OnDeathEvent(this), this);
		manager.registerEvents(new OnPlayerInteractEvent(), this);
		manager.registerEvents(new OnRespawnEvent(this), this);
		manager.registerEvents(new CommandRunEvent(this), this);
		manager.registerEvents(new OnBlockBrake(this), this);
		manager.registerEvents(new OnBlockPlace(this), this);
		getCommand("duty").setExecutor(new DutyCommand(this));
		getCommand("guards").setExecutor(new GuardsCommand());
	}

	public void onDisable() {
	}

	public void setupConfig() {
		File folder = new File(getDataFolder(), "Guard Data");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		kit = getConfig().getString("Kit");
		bannedCommands = getConfig().getStringList("Blocked-Commands");
		guardOnlyCommands = getConfig().getStringList("Guard-Only-Commands");
		wepTitle = getConfig().getString(ChatColor.translateAlternateColorCodes('&', "Weapon-Message-Title"));
		wepSub = getConfig().getString(ChatColor.translateAlternateColorCodes('&', "Weapon-Message-SubTitle"));
		potTitle = getConfig().getString(ChatColor.translateAlternateColorCodes('&', "Potion-Message-Title"));
		potSub = getConfig().getString(ChatColor.translateAlternateColorCodes('&', "Potion-Message-SubTitle"));
		titleTime = getConfig().getInt("Title-Time");
	}

	public void loadGuards() {
		File[] files = new File(getDataFolder(), "Guard Data").listFiles();
		for (File file : files) {
			String name = file.getName().substring(0, file.getName().length() - 4);
			if (name.length() == 36) {
				try {
					GuardManager.getManager().loadGuard(file);
				} catch (IllegalArgumentException e) {
					logger.info(getName() + " The file " + file.getName()
							+ " looked like UUID named file but it isn't!");
				}
			}
		}
	}

}