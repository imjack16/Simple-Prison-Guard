package me.imjack.arkham.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.imjack.arkham.JailDuty;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.evilmidget38.UUIDFetcher;
import com.kill3rtaco.tacoserialization.InventorySerialization;

public class GuardCommandExecutor implements CommandExecutor {
	static JailDuty plugin;

	public GuardCommandExecutor(JailDuty instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("duty")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("help")) {
						player.sendMessage(ChatColor.YELLOW + "-------------Help------------");
						player.sendMessage(ChatColor.GOLD + "/Duty" + ChatColor.WHITE + " - Switchs between duty mode.");
						player.sendMessage(ChatColor.GOLD + "/Duty help" + ChatColor.WHITE + " - Shows the help menu.");
						player.sendMessage(ChatColor.GOLD + "/Duty Convert" + ChatColor.WHITE + " - Converts all configs to UUIDS. Make sure to back up guards data and only use on 1.7.8 +");
						player.sendMessage(ChatColor.GOLD + "This plugin was created by imjack16 with help from Kill3rtaco's API.");
						return true;
					} else if (args[0].equalsIgnoreCase("convert")) {
						if (player.hasPermission("Jail.Duty.Convert")) {
							player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "] " + ChatColor.RED + "Converting the configs to players UUIDS this might cause lag.");

							final File folder = new File(plugin.getDataFolder(), "Guard Data");
							final File[] PlayerData = folder.listFiles();
							final List<String> playerList = new ArrayList<>();
							for (int i = 0; i < PlayerData.length; i++) {
 								if (PlayerData[i].isFile()) {
									final String[] playerName = PlayerData[i].getName().split("\\.");
									playerList.add(playerName[0]);
								}
							}
							Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
								@Override
								public void run() {
									for (int i = 0; i < playerList.size(); i++) {
										if (PlayerData[i].isFile()) {
											playerList.get(i);
											File guardInv = new File(folder, PlayerData[i].getName());
											YamlConfiguration guardconfig = YamlConfiguration.loadConfiguration(guardInv);
											String oldInv = guardconfig.getString(playerList.get(i) + " Inventory");
											boolean oldOnGuard = guardconfig.getBoolean(playerList.get(i) + " OnGuard");
											UUID playerUUID = getUUID(playerList).get(playerList.get(i));
											guardconfig.set("Inventory", oldInv);
											guardconfig.set("onGuard", oldOnGuard);
											guardconfig.set(playerList.get(i) + " Inventory", null);
											guardconfig.set(playerList.get(i) + " OnGuard", null);
											try {
												guardconfig.save(guardInv);
											} catch (IOException e) {
												e.printStackTrace();
											}
											File newfile = new File(folder, playerUUID + ".yml");
											PlayerData[i].renameTo(newfile);
											System.out.println("The UserData + " + playerList.get(i) + " has been converted to " + playerUUID);
										}
									}
								}
							});
						} else {
							player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "] " + ChatColor.RED + "You don't have permission to use this command.");
							return true;
						}
						return true;
					} else {
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "] " + ChatColor.RED + "This is not a vaild command try /duty help");
						return true;
					}
				} else if (player.hasPermission("Jail.Duty.Command")) {
					File folder = new File(plugin.getDataFolder() + "/", "Guard Data");
					File GuardInv = new File(folder, player.getUniqueId().toString() + ".yml");
					YamlConfiguration Guardconfig = YamlConfiguration.loadConfiguration(GuardInv);
					PlayerInventory inv = player.getInventory();
					String invAsString = InventorySerialization.serializePlayerInventoryAsString(inv);
					if (!GuardInv.exists()) {
						try {
							GuardInv.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}

						Guardconfig.set("Inventory", invAsString);
						Guardconfig.set("onGuard", true);
						try {
							Guardconfig.save(GuardInv);
						} catch (IOException e) {
							e.printStackTrace();
						}
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "]" + ChatColor.RESET + plugin.getConfig().getString("Duty-On-Message").replace("%p", player.getName())));
						player.getInventory().clear();
						player.getInventory().setArmorContents(null);
						player.getInventory().setHelmet(GetHelm());
						player.getInventory().setChestplate(GetChest());
						player.getInventory().setLeggings(GetLegs());
						player.getInventory().setBoots(GetBoot());
						player.getInventory().addItem(GetSword());
						player.getInventory().addItem(getbow());
						player.getInventory().addItem(GetStick());
						player.getInventory().addItem(getFood());
						player.getInventory().addItem(getApples());
						player.getInventory().addItem(getArrows());
						return true;
					} else if (Guardconfig.getBoolean("onGuard") == true) {
						// Takes them off guard
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "]" + ChatColor.RESET + plugin.getConfig().getString("Duty-Off-Message").replace("%p", player.getName())));
						player.getInventory().clear();
						player.getInventory().setArmorContents(null);
						InventorySerialization.setPlayerInventory(player, Guardconfig.getString("Inventory"));
						player.getActivePotionEffects().clear();
						Guardconfig.set("onGuard", false);
						try {
							Guardconfig.save(GuardInv);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return true;
					} else if (Guardconfig.getBoolean("onGuard") == false) {
						// Put them on guard
						Guardconfig.set("Inventory", invAsString);
						Guardconfig.set("onGuard", true);
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "]" + ChatColor.RESET + plugin.getConfig().getString("Duty-On-Message").replace("%p", player.getName())));
						try {
							Guardconfig.save(GuardInv);
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.getInventory().clear();
						player.getInventory().setArmorContents(null);
						player.getInventory().setHelmet(GetHelm());
						player.getInventory().setChestplate(GetChest());
						player.getInventory().setLeggings(GetLegs());
						player.getInventory().setBoots(GetBoot());
						player.getInventory().addItem(GetSword());
						player.getInventory().addItem(getbow());
						player.getInventory().addItem(GetStick());
						player.getInventory().addItem(getFood());
						player.getInventory().addItem(getApples());
						player.getInventory().addItem(getArrows());
						return true;
					}
				} else {
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + plugin.getConfig().getString("Server-Name") + ChatColor.GRAY + "] " + ChatColor.RED + "You don't have permission to use this command.");
					return true;
				}
			}
		}

		return false;

	}

	public Map<String, UUID> getUUID(List<String> names) {
		UUIDFetcher fetcher = new UUIDFetcher(names);
		Map<String, UUID> response = null;
		try {
			response = fetcher.call();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return response;
	}

	public static ItemStack GetSword() {
		ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD, 1);
		itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, plugin.getConfig().getInt("Sword-Sharp"));
		itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, plugin.getConfig().getInt("Sword-Knock"));
		itemStack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, plugin.getConfig().getInt("Sword-Fire"));
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Sword-Name")));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack GetStick() {
		ItemStack itemStack = new ItemStack(Material.STICK, 1);
		itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, plugin.getConfig().getInt("Stick-Knock"));
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Stick-Name")));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack GetHelm() {
		ItemStack itemStack = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Armour-Helm").toUpperCase()), 1);
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, plugin.getConfig().getInt("Armour-Protection"));
		itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, plugin.getConfig().getInt("Armour-Unbreaking"));
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, plugin.getConfig().getInt("Armour-Fire"));
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, plugin.getConfig().getInt("Armour-Projectile"));
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Helmet-Name")));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack GetChest() {
		ItemStack itemStack = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Armour-Chest").toUpperCase()), 1);
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, plugin.getConfig().getInt("Armour-Protection"));
		itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, plugin.getConfig().getInt("Armour-Unbreaking"));
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, plugin.getConfig().getInt("Armour-Fire"));
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, plugin.getConfig().getInt("Armour-Projectile"));
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Chestplate-Name")));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack GetLegs() {
		ItemStack itemStack = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Armour-Chest").toUpperCase()), 1);
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, plugin.getConfig().getInt("Armour-Protection"));
		itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, plugin.getConfig().getInt("Armour-Unbreaking"));
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, plugin.getConfig().getInt("Armour-Fire"));
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, plugin.getConfig().getInt("Armour-Projectile"));
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Chestplate-Name")));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack GetBoot() {
		ItemStack itemStack = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Armour-Boots").toUpperCase()), 1);
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, plugin.getConfig().getInt("Armour-Protection"));
		itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, plugin.getConfig().getInt("Armour-Unbreaking"));
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, plugin.getConfig().getInt("Armour-Fire"));
		itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, plugin.getConfig().getInt("Armour-Projectile"));
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Boots-Name")));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack getbow() {
		ItemStack itemStack = new ItemStack(Material.BOW, 1);
		itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, plugin.getConfig().getInt("Bow-Power"));
		itemStack.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, plugin.getConfig().getInt("Bow-Knock"));
		itemStack.addUnsafeEnchantment(Enchantment.ARROW_FIRE, plugin.getConfig().getInt("Bow-Fire"));
		itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Bow-Name")));
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack getFood() {
		ItemStack itemStack = new ItemStack(Material.COOKED_BEEF, plugin.getConfig().getInt("Pork-Ammount"));
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack getApples() {
		ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, plugin.getConfig().getInt("Apple-Ammount"), (short) 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public static ItemStack getArrows() {
		ItemStack itemStack = new ItemStack(Material.ARROW, 1);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
}
