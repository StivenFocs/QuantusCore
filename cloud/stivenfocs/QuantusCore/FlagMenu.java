package cloud.stivenfocs.QuantusCore;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FlagMenu {

    public static HashMap<UUID, Inventory> flags_gui = new HashMap<>();

    public static Inventory getPlayerFlagGUI(Player p) {
        if (!flags_gui.containsKey(p.getUniqueId())) {
            if (Vars.debug) Vars.plugin.getLogger().info("No flagMenu for player " + p.getName() + ", generating one!");
            Inventory new_flag_gui = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', Vars.flag_gui_displayname));
            flags_gui.put(p.getUniqueId(), new_flag_gui);
        }

        tryRefreshPlayerFlagGUI(p);

        return flags_gui.get(p.getUniqueId());
    }

    public static void tryRefreshPlayerFlagGUI(Player p) {
        if (flags_gui.containsKey(p.getUniqueId())) {
            flags_gui.get(p.getUniqueId()).setItem(1, getPlayerFlagUpgradeItem(p));
            flags_gui.get(p.getUniqueId()).setItem(3, Vars.getItem("shop_button", p));
        } else if (Vars.debug) Vars.plugin.getLogger().warning("Tried to refresh " + p.getName() + "'s Flag GUI when the gui doesn't exists!");
    }

    public static Boolean isFlagMenu(Inventory gui) {
        for (Inventory gui_ : flags_gui.values()) {
            if (gui_.equals(gui)) return true;
        }

        return false;
    }

    public static void deletePlayerFlagMenu(OfflinePlayer p) {
        if (flags_gui.containsKey(p.getUniqueId())) {
            for (HumanEntity viewer : new ArrayList<>(flags_gui.get(p.getUniqueId()).getViewers())) {
                viewer.closeInventory();
            }
        }
        flags_gui.remove(p.getUniqueId());
    }

    public static ItemStack getPlayerFlagUpgradeItem(Player player) {
        Boolean debug = Vars.debug;
        JavaPlugin plugin = Vars.plugin;
        String itemName = "flag_upgrade";
        PlayerData p_data = PlayerData.getPlayerData(player.getUniqueId());

        if (debug) plugin.getLogger().info("Getting the item: " + itemName);
        String path = "items." + itemName + ".";
        if (debug) plugin.getLogger().info("path: " + path);
        if (plugin.getConfig().get("items." + itemName) == null) {
            throw new NullPointerException("Item not found: " + itemName);
        }

        try {
            ItemStack i;
            if (Vars.debug) Vars.plugin.getLogger().info("Creating ItemStack");
            if (plugin.getConfig().getString(path + "material").equalsIgnoreCase("%flag_material%")) {
                i = new ItemStack(p_data.getFlagBlock().getType());
            } else {
                try {
                    i = new ItemStack(Material.valueOf(plugin.getConfig().getString(path + "material").toUpperCase()));
                } catch (Exception ex) {
                    i = new ItemStack(Material.BEDROCK, 0);
                    Vars.plugin.getLogger().warning("Couldn't find the material for the item " + itemName + " item...");
                }
            }
            if (Vars.debug) Vars.plugin.getLogger().info("Setting durability");
            i.setDurability((short) plugin.getConfig().getInt(path + "durability"));
            if (Vars.debug) Vars.plugin.getLogger().info("Getting itemMeta");
            for (String enchantment_string : plugin.getConfig().getStringList(path + "enchantments")) {
                try {
                    String[] enchantment = enchantment_string.replaceAll(" ", "").split(",");
                    i.addUnsafeEnchantment(Enchantment.getByName(enchantment[0]), Integer.parseInt(enchantment[1]));
                } catch (Exception ex) {
                    Vars.plugin.getLogger().warning("Unable to parse the enchantment " + enchantment_string + " for the item " + itemName + ". Reason: " + ex.getMessage());
                }
            }
            ItemMeta iMeta = i.getItemMeta();
            if (p_data.getFlagBlock().getState() instanceof Banner && plugin.getConfig().getString(path + "material").equalsIgnoreCase("%flag_material%")) {
                Banner banner_block = (Banner) p_data.getFlagBlock().getState();
                BannerMeta banner_meta = (BannerMeta) iMeta;
                banner_meta.setBaseColor(banner_block.getBaseColor());
                banner_meta.setPatterns(banner_block.getPatterns());
                i.setItemMeta(banner_meta);
            }
            iMeta = i.getItemMeta();
            if (Vars.debug) Vars.plugin.getLogger().info("Applying displayname");
            String displayname = plugin.getConfig().getString(path + "displayname");
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) displayname = PlaceholderAPI.setPlaceholders(player, displayname);
            iMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Vars.setPlaceholders(displayname, player)));
            if (Vars.debug) Vars.plugin.getLogger().info("Applying lore");
            List<String> colored_list = new ArrayList<>();
            for (String line : plugin.getConfig().getStringList(path + "lore")) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) line = PlaceholderAPI.setPlaceholders(player, line);
                colored_list.add(ChatColor.translateAlternateColorCodes('&', Vars.setPlaceholders(line, player)));
            }
            iMeta.setLore(colored_list);
            for (String flag_string : plugin.getConfig().getStringList(path + "flags")) {
                try {
                    iMeta.addItemFlags(ItemFlag.valueOf(flag_string));
                } catch (Exception ex) {
                    Vars.plugin.getLogger().warning("Unable to parse the enchantment " + flag_string + " for the item " + itemName + ". Reason: " + ex.getMessage());
                }
            }
            if (plugin.getConfig().getBoolean(path + "unbreakable")) iMeta.setUnbreakable(true);
            try {
                if (plugin.getConfig().get(path + "model_data") != null) {
                    iMeta.setCustomModelData(plugin.getConfig().getInt(path + "model_data"));
                }
            } catch (Exception ex) {
                Vars.plugin.getLogger().warning("Unable to set model data to the item " + itemName + " Reason: " + ex.getMessage());
            }
            iMeta.getCustomTagContainer().setCustomTag(new NamespacedKey(Vars.plugin, "itemName"), ItemTagType.STRING, itemName);
            if (Vars.debug) Vars.plugin.getLogger().info("Applying new item meta: " + iMeta);
            if (plugin.getConfig().get("custom_model_data") != null) iMeta.setCustomModelData(plugin.getConfig().getInt("custom_model_data"));
            if (Vars.debug) Vars.plugin.getLogger().info("Applying new item meta: " + iMeta);
            i.setItemMeta(iMeta);
            if (i.getType().equals(Material.PLAYER_HEAD)) {
                String skull_skin = Vars.plugin.getConfig().getString(path + "skull_skin_meta");
                if (skull_skin != null) {
                    SkullMeta skullMeta = (SkullMeta) i.getItemMeta();

                    if (skull_skin.length() > 16) {
                        GameProfile profile = new GameProfile(UUID.randomUUID(), skull_skin);

                        profile.getProperties().put("textures", new Property("value", skull_skin));

                        Field profileField;
                        try {
                            profileField = skullMeta.getClass().getDeclaredField("profile");
                            profileField.setAccessible(true);
                            profileField.set(skullMeta, profile);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        GameProfile profile = new GameProfile(null, skull_skin);

                        Field profileField;
                        try {
                            profileField = skullMeta.getClass().getDeclaredField("profile");
                            profileField.setAccessible(true);
                            profileField.set(skullMeta, profile);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    i.setItemMeta(skullMeta);
                }
            }
            return i;
        } catch (Exception ex) {
            plugin.getLogger().severe("An exception occurred while trying to generate the " + itemName + " item, check the configuration...");
            plugin.getLogger().severe(ex.getMessage());
            ex.printStackTrace();
            throw new NullPointerException("Couldn't get a valid item from " + itemName + " for " + player.getName());
        }
    }

}
