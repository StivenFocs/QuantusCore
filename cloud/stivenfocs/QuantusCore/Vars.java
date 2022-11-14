package cloud.stivenfocs.QuantusCore;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class Vars {

    public static Loader plugin;
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public File dataFile = new File(plugin.getDataFolder() + "/data.yml");
    public FileConfiguration dataConfig;

    private static Vars vars;
    public static Vars getVars() {
        if (vars == null) vars = new Vars();
        return vars;
    }

    public static Economy econ = null;

    ////////////////////////////////////////////

    public static Boolean debug = false;
    public static List<String> disabled_worlds = new ArrayList<>();
    public static List<?> flag_items = new ArrayList<>();
    public static List<String> join_event = new ArrayList<>();
    public static List<String> killed_event = new ArrayList<>();
    public static List<String> kill_event = new ArrayList<>();
    public static List<String> flag_place_event = new ArrayList<>();
    public static List<String> your_flag_broken_event = new ArrayList<>();
    public static List<String> you_broken_flag_event = new ArrayList<>();
    public static List<String> earn_event = new ArrayList<>();
    public static List<String> respawn_cooldown_event = new ArrayList<>();
    public static List<String> respawn_event = new ArrayList<>();
    public static List<String> teleport_cooldown_event = new ArrayList<>();
    public static List<String> levelup_event = new ArrayList<>();
    public static List<String> killstreak_offset_reach_event = new ArrayList<>();
    public static List<String> shop_button_event = new ArrayList<>();
    public static List<String> other_flag_interaction_event = new ArrayList<>();
    public static List<String> player_ct_logout_event = new ArrayList<>();
    public static List<String> enemy_ct_logout_event = new ArrayList<>();
    public static Integer player_teleport_cooldown = 5;
    public static Integer player_respawn_cooldown = 5;
    public static Double per_earn_seconds = 1.0;
    public static String earn_amount = "%player_level%";
    public static String flag_broken_loss = "%vault_eco_balance%*(50/100)";
    public static String player_killed_loss = "%vault_eco_balance%*(10/100)";
    public static String player_ct_logout_loss = "%vault_eco_balance%*(10/100)";
    public static Integer hookfeather_use_cooldown = 10;
    public static Integer killstreak_offset_count = 10;
    public static String levelup_cost = "250*(1.1)^%player_level%";
    public static Boolean respawn_cooldown = true;
    public static Boolean respawn_keeps_player_location = true;
    public static String spawn_location = "";
    public static String respawn_location = "";
    public static String spectator_gamemode = "SPECTATOR";
    public static String respawn_gamemode = "SURVIVAL";
    public static String flag_gui_displayname = "";
    public static Integer player_flag_breaking_countdown = 15;
    public static Double breaking_hologram_height = 0.5;
    public static List<String> breaking_hologram_text = new ArrayList<>();
    public static Double breaking_hologram_lines_height = 0.26;
    public static String hookfeather_material = "FEATHER";
    public static Integer hookfeather_jump_boost = 2;
    public static Double hookfeather_jump_forward = 0.3;
    public static String hookfeather_actionbar_text = "";
    public static List<String> allowed_blocks_break = new ArrayList<>();
    public static Integer afk_time = 1800;
    public static List<String> afk_event = new ArrayList<>();
    public static Boolean use_expbar_to_display_level = true;
    public static List<String> no_earn_regions = new ArrayList<>();
    public static Boolean teleport_to_spawn_on_quit = true;
    public static List<String> flag_place_block_blacklist = new ArrayList<>();
    public static Integer data_save_delay = 60;
    public static Integer top_refresh_delay = 600;

    public static String prefix = "";
    public static String configuration_reloaded = "";
    public static String an_error_occurred = "";
    public static String no_permission = "";
    public static String incomplete_command = "";
    public static String only_players = "";
    public static String unknown_subcommand = "";
    public static String spawn_location_set = "";
    public static String no_spawn_set = "";
    public static String respawn_location_set = "";
    public static String teleporting_you = "";
    public static String player_not_found = "";
    public static String not_enough_money = "";
    public static String hold_an_item = "";
    public static String flag_item_added = "";
    public static String only_placeable_blocks = "";
    public static String teleporting_player = "";
    public static String flag_not_placed = "";
    public static String flag_broken = "";
    public static String hookfeather_cooldown_message = "";
    public static String you_moved = "";
    public static String unknown_statistic = "";
    public static String unknown_action = "";
    public static String statistic_reset = "";
    public static String statistic_set = "";
    public static String an_integer_needed = "";
    public static String nobody = "";
    public static String cant_go_to_spawn = "";
    public static String banner_placed_placeholder = "";
    public static String banner_not_placed_placeholder = "";
    public static String pos1_set = "";
    public static String pos2_set = "";
    public static String invalid_selection = "";
    public static String added_no_earn_region = "";
    public static String cant_place_there = "";
    public static List<String> stats = new ArrayList<>();
    public static List<String> stats_others = new ArrayList<>();
    public static List<String> help_user = new ArrayList<>();
    public static List<String> help_admin = new ArrayList<>();
    public static List<String> top_levels = new ArrayList<>();

    ////////////////////////////////////////////

    public boolean reloadVars() {
        try {
            plugin.reloadConfig();

            getConfig().options().header("Developed with LOV by StivenFocs");
            getConfig().options().copyDefaults(true);

            getConfig().addDefault("options.disabled_worlds", new ArrayList<>());
            getConfig().addDefault("options.flag_items", new ArrayList<>());
            List<String> new_join_event = new ArrayList<>();
            new_join_event.add("setmaxhealth:40");
            new_join_event.add("sethealth:40");
            getConfig().addDefault("options.join_event", new_join_event);
            List<String> new_killed_event = new ArrayList<>();
            new_killed_event.add("tell:&f%killer_name% &7picked &a$%amount% &7from you.");
            getConfig().addDefault("options.killed_event", new_killed_event);
            List<String> new_kill_event = new ArrayList<>();
            new_kill_event.add("title: ,&7You picked &a$%amount% &7from &f%player_name%,5,50,5");
            new_kill_event.add("effect:regeneration,520,2,true,false,false");
            getConfig().addDefault("options.kill_event", new_kill_event);
            List<String> new_flag_place_event = new ArrayList<>();
            new_flag_place_event.add("title:&e&lFLAG PLACED,&7You're earning %player_earned_amount%,5,50,5");
            getConfig().addDefault("options.flag_place_event", new_flag_place_event);
            List<String> new_your_flag_broken_event = new ArrayList<>();
            new_your_flag_broken_event.add("title:&cYour Flag Broken,&f%his_name% &7broke your flag! You lost &a$%amount%,5,50,5");
            new_your_flag_broken_event.add("particle:FLAME,%player_flag_world%,%player_flag_x%,%player_flag_y%,%player_flag_z%,100,1,1,1,0");
            getConfig().addDefault("options.your_flag_broken_event", new_your_flag_broken_event);
            List<String> new_you_broken_flag_event = new ArrayList<>();
            new_you_broken_flag_event.add("title:&cFlag Broken,&7You broke the flag of &f%his_name%&7. You got &a$%amount%,5,50,5");
            getConfig().addDefault("options.you_broken_flag_event", new_you_broken_flag_event);
            List<String> new_earn_event = new ArrayList<>();
            new_earn_event.add("money:give,%player_earned_amount%");
            getConfig().addDefault("options.earn_event", new_earn_event);
            List<String> new_respawn_cooldown_event = new ArrayList<>();
            new_respawn_cooldown_event.add("title:&cYou died!,&7Respawning in &f%remaining_seconds% &7seconds,5,10,5");
            getConfig().addDefault("options.respawn_cooldown_event", new_respawn_cooldown_event);
            List<String> new_respawn_event = new ArrayList<>();
            new_respawn_event.add("title: ,&bRespawned,5,10,5");
            new_respawn_event.add("setmaxhealth:40");
            new_respawn_event.add("sethealth:40");
            getConfig().addDefault("options.respawn_event", new_respawn_event);
            List<String> new_teleport_cooldown_event = new ArrayList<>();
            new_teleport_cooldown_event.add("title: ,&7Teleport in &f%remaining_seconds% &7seconds!,5,10,5");
            getConfig().addDefault("options.teleport_cooldown_event", new_teleport_cooldown_event);
            List<String> new_levelup_event = new ArrayList<>();
            new_levelup_event.add("close_inventory");
            new_levelup_event.add("tell:&eYour Flag level is now &f%player_level%");
            new_levelup_event.add("tell:&7you spent &a$%money_spent%");
            getConfig().addDefault("options.levelup_event", new_levelup_event);
            List<String> new_killstreak_offset_reach = new ArrayList<>();
            new_killstreak_offset_reach.add("broadcast:&f%player_name% &7reached &f%player_killstreak% kills in a row!");
            getConfig().addDefault("options.killstreak_offset_reach_event", new_killstreak_offset_reach);
            getConfig().addDefault("options.shop_button_event", new ArrayList<>());
            List<String> new_other_flag_interaction_event = new ArrayList<>();
            new_other_flag_interaction_event.add("title:,&e&lFLAG OF &f%his_name%,0,30,5");
            getConfig().addDefault("options.other_flag_interaction_event", new_other_flag_interaction_event);
            List<String> new_player_ct_logout_event = new ArrayList<>();
            getConfig().addDefault("options.player_ct_logout_event", new_player_ct_logout_event);
            List<String> new_enemy_ct_logout_event = new ArrayList<>();
            getConfig().addDefault("options.enemy_ct_logout_event", new_enemy_ct_logout_event);
            getConfig().addDefault("options.player_teleport_cooldown", 5);
            getConfig().addDefault("options.player_respawn_cooldown", 5);
            getConfig().addDefault("options.hookfeather_use_cooldown", 10);
            getConfig().addDefault("options.per_earn_seconds", 1.0);
            getConfig().addDefault("options.earn_amount", "%player_level%*%player_multiplier%");
            getConfig().addDefault("options.flag_broken_loss", "%vault_eco_balance%*(50/100)");
            getConfig().addDefault("options.player_killed_loss", "%vault_eco_balance%*(10/100)");
            getConfig().addDefault("options.player_ct_logout_loss", "%vault_eco_balance%*(10/100)");
            getConfig().addDefault("options.killstreak_offset_count", 10);
            getConfig().addDefault("options.levelup_cost", "250*(1.1)^%player_level%");
            getConfig().addDefault("options.respawn_cooldown", true);
            getConfig().addDefault("options.respawn_keeps_player_location", true);
            getConfig().addDefault("options.spawn_location", "");
            getConfig().addDefault("options.respawn_location", "");
            getConfig().addDefault("options.spectator_gamemode", "SPECTATOR");
            getConfig().addDefault("options.respawn_gamemode", "SURVIVAL");
            getConfig().addDefault("options.flag_gui_displayname", "            &lYour  Flag");
            getConfig().addDefault("options.player_flag_breaking_countdown", 15);
            getConfig().addDefault("options.breaking_hologram.height", 0.5);
            List<String> new_breaking_hologram_text = new ArrayList<>();
            new_breaking_hologram_text.add("&8&m=======================");
            new_breaking_hologram_text.add("&f%name%&7's Flag");
            new_breaking_hologram_text.add("&7Removing in %flag_breaking_countdown%s");
            new_breaking_hologram_text.add("&8&m=======================");
            getConfig().addDefault("options.breaking_hologram.text", new_breaking_hologram_text);
            getConfig().addDefault("options.breaking_hologram.lines_height", 0.26);
            getConfig().addDefault("options.hookfeather.material", "FEATHER");
            getConfig().addDefault("options.hookfeather.jump_boost", 2);
            getConfig().addDefault("options.hookfeather.jump_forward", 0.3);
            getConfig().addDefault("options.hookfeather.actionbar_text", "%remaining_seconds%");
            List<String> new_allowed_blocks_break = new ArrayList<>();
            new_allowed_blocks_break.add("COBBLESTONE");
            getConfig().addDefault("options.allowed_blocks_break", new_allowed_blocks_break);
            getConfig().addDefault("options.afk_time", 1800);
            List<String> new_afk_event = new ArrayList<>();
            new_afk_event.add("title:&c&lYOU ARE AFK,&7You're not earning money!,0,30,5");
            getConfig().addDefault("options.afk_event", new_afk_event);
            getConfig().addDefault("options.use_expbar_to_display_level", true);
            getConfig().addDefault("options.no_earn_regions", new ArrayList<>());
            getConfig().addDefault("options.teleport_to_spawn_on_quit", true);
            getConfig().addDefault("options.flag_place_block_blacklist", new ArrayList<>());
            getConfig().addDefault("options.data_save_delay", 60);
            getConfig().addDefault("options.top_refresh_delay", 600);

            getConfig().addDefault("items.flag_upgrade.displayname", "&d&lUpgrade your flag");
            getConfig().addDefault("items.flag_upgrade.material", "%flag_material%");
            getConfig().addDefault("items.flag_upgrade.durability", 0);
            List<String> new_flag_upgrade_lore = new ArrayList<>();
            new_flag_upgrade_lore.add("");
            new_flag_upgrade_lore.add("&f&lUpgrade your Flag to level &e&l%player_next_level%");
            new_flag_upgrade_lore.add("&f&lfor &a&l$%player_levelup_cost%");
            new_flag_upgrade_lore.add("");
            new_flag_upgrade_lore.add("&7You have &a$%vault_eco_balance_commas%");
            getConfig().addDefault("items.flag_upgrade.lore", new_flag_upgrade_lore);
            getConfig().addDefault("items.flag_upgrade.enchantments", new ArrayList<>());
            List<String> new_flag_upgrade_flags = new ArrayList<>();
            new_flag_upgrade_flags.add("HIDE_POTION_EFFECTS");
            getConfig().addDefault("items.flag_upgrade.flags", new_flag_upgrade_flags);

            getConfig().addDefault("items.shop_button.displayname", "&6&lShop");
            getConfig().addDefault("items.shop_button.material", "player_head");
            getConfig().addDefault("items.shop_button.durability", 0);
            getConfig().addDefault("items.shop_button.lore", new ArrayList<>());
            getConfig().addDefault("items.shop_button.enchantments", new ArrayList<>());
            getConfig().addDefault("items.shop_button.flags", new ArrayList<>());
            if (getConfig().getString("items.shop_button.material").equalsIgnoreCase("player_head")) getConfig().addDefault("items.shop_button.skull_skin_meta", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI2NWY5NmY1NGI3ODg4NWM0NmU3ZDJmODZiMWMxZGJmZTY0M2M2MDYwZmM3ZmNjOTgzNGMzZTNmZDU5NTEzNSJ9fX0=");

            getConfig().addDefault("sounds.hookfeather_jump.name", "ENTITY_FIREWORK_ROCKET_LAUNCH");
            getConfig().addDefault("sounds.hookfeather_jump.volume", 1);
            getConfig().addDefault("sounds.hookfeather_jump.pitch", 1);

            getConfig().addDefault("messages.prefix", "");
            getConfig().addDefault("messages.configuration_reloaded", "&aConfiguration successfully reloaded");
            getConfig().addDefault("messages.an_error_occurred", "&cAn error occurred while doing this task...");
            getConfig().addDefault("messages.no_permission", "&cYou're not permitted to do this.");
            getConfig().addDefault("messages.incomplete_command", "&cIncomplete command! use the help command for the args list.");
            getConfig().addDefault("messages.only_players", "&cOnly players can do this.");
            getConfig().addDefault("messages.spawn_location_set", "&aSpawn location set!");
            getConfig().addDefault("messages.respawn_location_set", "&aRespawning location set!");
            getConfig().addDefault("messages.no_spawn_set", "&cNo spawn location set!");
            getConfig().addDefault("messages.teleporting_you", "&bTeleporting you in &f%remaining_seconds% &bseconds! &7please don't move!");
            getConfig().addDefault("messages.player_not_found", "&cPlayer not found");
            getConfig().addDefault("messages.not_enough_money", "&c&cYou don''t have enough money to do that!");
            getConfig().addDefault("messages.hold_an_item", "&cYou have to hold an item!");
            getConfig().addDefault("messages.flag_item_added", "&aFlag item added to the list");
            getConfig().addDefault("messages.only_placeable_blocks", "&cYou can do it only with placeable blocks!");
            getConfig().addDefault("messages.teleporting_player", "&7Teleporting &f%player_name%");
            getConfig().addDefault("messages.flag_not_placed", "&cNo flag has been placed");
            getConfig().addDefault("messages.flag_broken", "&cthe &f%player%&c's flag has been broken.");
            getConfig().addDefault("messages.hookfeather_cooldown_message", "&cYou have to wait %cooldown_seconds% seconds to use it again.");
            getConfig().addDefault("messages.you_moved", "&cYou moved! teleportation canceled.");
            getConfig().addDefault("messages.unknown_statistic", "&cUnknown statistic name &f%statistic%");
            getConfig().addDefault("messages.unknown_action", "&cUnknown action name &f%action%&c. You can use RESET, SET, ADD, REMOVE");
            getConfig().addDefault("messages.statistic_reset", "&eThis &f%player_name%&e's statistic has been reset.");
            getConfig().addDefault("messages.statistic_set", "&eThis &f%player_name%&e's statistic has been set to &f%amount%");
            getConfig().addDefault("messages.an_integer_needed", "&cAn integer number is needed!");
            getConfig().addDefault("messages.nobody", "Nobody");
            getConfig().addDefault("messages.cant_go_to_spawn", "&cYou can't go to spawn when you have a placed flag.");
            getConfig().addDefault("messages.banner_placed_placeholder", "Flag placed");
            getConfig().addDefault("messages.banner_not_placed_placeholder", "Flag not placed");
            getConfig().addDefault("messages.pos1_set", "&bPosition 1 set to &f%x%,%y%,%z%,%world%");
            getConfig().addDefault("messages.pos2_set", "&bPosition 2 set to &f%x%,%y%,%z%,%world%");
            getConfig().addDefault("messages.invalid_selection", "&cInvalid selection! try again by setting the pos1 and the pos2.");
            getConfig().addDefault("messages.cant_place_there", "&cYou're not allowed to place your flag there.");getConfig().addDefault("messages.added_no_earn_region", "&aNo Earn Region added to the list.");

            List<String> new_stats = new ArrayList<>();
            new_stats.add("&8&m=======================");
            new_stats.add("&8* &eYour stats");
            new_stats.add("");
            new_stats.add("&8* &f%player_kills% &eKills");
            new_stats.add("&8* &f%player_deaths% &eDeaths");
            new_stats.add("&8* &f%player_broken_flags% &eBroken flags");
            new_stats.add("&8* &eLevel &f%player_level%");
            new_stats.add("&8* &fx%player_multiplier% &eMultiplier");
            new_stats.add("");
            new_stats.add("&8&m=======================");
            getConfig().addDefault("messages.stats", new_stats);
            List<String> new_stats_others = new ArrayList<>();
            new_stats_others.add("&8&m=======================");
            new_stats_others.add("&8* &eStats of &f%other_player_name%");
            new_stats_others.add("");
            new_stats_others.add("&8* &f%other_player_kills% &eKills");
            new_stats_others.add("&8* &f%other_player_deaths% &eDeaths");
            new_stats_others.add("&8* &f%other_player_broken_flags% &eBroken flags");
            new_stats_others.add("&8* &eLevel &f%other_player_level%");
            new_stats_others.add("&8* &fx%other_player_multiplier% &eMultiplier");
            new_stats_others.add("");
            new_stats_others.add("&8&m=======================");
            getConfig().addDefault("messages.stats_others", new_stats_others);
            List<String> new_help_user = new ArrayList<>();
            new_help_user.add("&8&m============================");
            new_help_user.add("&8* &e&lQuantusCore &7%version%");
            new_help_user.add("");
            new_help_user.add("&8* &7/stats &8&m|&7 Shows your stats");
            new_help_user.add("&8* &7/top &8&m|&7 See the player's top levels");
            new_help_user.add("&8* &7/tutorial &8&m|&7 Quicktutorial");
            new_help_user.add("");
            new_help_user.add("&8&m============================");
            getConfig().addDefault("messages.help_user", new_help_user);
            List<String> new_help_admin = new ArrayList<>();
            new_help_admin.add("&8&m============================");
            new_help_admin.add("&8* &e&lQuantusCore &7%version%");
            new_help_admin.add("");
            new_help_admin.add("&8* &7/quantus reload &8&m|&7 Reload the whole configuration");
            new_help_admin.add("&8* &7/quantus setspawn &8&m|&7 Set the main spawn");
            new_help_admin.add("&8* &7/quantus setspectatorspawn &8&m|&7 Set the respawning spawn");
            new_help_admin.add("&8* &7/quantus setplayerstat <player> <stat> <action> [amount] &8&m|&7 edit a player statistic");
            new_help_admin.add("&8* &7/quantus addflagitem &8&m|&7 Add a custom flag item to the list");
            new_help_admin.add("&8* &7/quantus gotoflag <player> &8&m|&7 Go to a player flag");
            new_help_admin.add("&8* &7/quantus breakflag <player> &8&m|&7 Break a player flag");
            new_help_admin.add("&8* &7/quantus pos1 &8&m|&7 Set the Position 1");
            new_help_admin.add("&8* &7/quantus pos2 &8&m|&7 Set the Position 2");
            new_help_admin.add("&8* &7/quantus addnoearnregion &8&m|&7 Add a No Earn Region");
            new_help_admin.add("");
            new_help_admin.add("&8&m============================");
            getConfig().addDefault("messages.help_admin", new_help_admin);
            List<String> new_top_levels = new ArrayList<>();
            new_top_levels.add("&8&m=============================");
            new_top_levels.add("&8* &e&lTOP Player's Levels");
            new_top_levels.add("");
            new_top_levels.add("&8* &e&l1. &f%top_level_1_name% &e%top_level_1_amount%");
            new_top_levels.add("&8* &e&l2. &f%top_level_2_name% &e%top_level_2_amount%");
            new_top_levels.add("&8* &e&l3. &f%top_level_3_name% &e%top_level_3_amount%");
            new_top_levels.add("&8* &e&l4. &f%top_level_4_name% &e%top_level_4_amount%");
            new_top_levels.add("&8* &e&l5. &f%top_level_5_name% &e%top_level_5_amount%");
            new_top_levels.add("");
            new_top_levels.add("&8&m=============================");
            getConfig().addDefault("messages.top_levels", new_top_levels);

            plugin.saveConfig();
            plugin.reloadConfig();

            debug = getConfig().getBoolean("options.debug", false);
            disabled_worlds = getConfig().getStringList("options.disabled_worlds");
            flag_items = getConfig().getList("options.flag_items");
            join_event = getConfig().getStringList("options.join_event");
            killed_event = getConfig().getStringList("options.killed_event");
            kill_event = getConfig().getStringList("options.kill_event");
            flag_place_event = getConfig().getStringList("options.flag_place_event");
            your_flag_broken_event = getConfig().getStringList("options.your_flag_broken_event");
            you_broken_flag_event = getConfig().getStringList("options.you_broken_flag_event");
            earn_event = getConfig().getStringList("options.earn_event");
            respawn_cooldown_event = getConfig().getStringList("options.respawn_cooldown_event");
            respawn_event = getConfig().getStringList("options.respawn_event");
            teleport_cooldown_event = getConfig().getStringList("options.teleport_cooldown_event");
            levelup_event = getConfig().getStringList("options.levelup_event");
            killstreak_offset_reach_event = getConfig().getStringList("options.killstreak_offset_reach_event");
            shop_button_event = getConfig().getStringList("options.shop_button_event");
            other_flag_interaction_event = getConfig().getStringList("options.other_flag_interaction_event");
            player_ct_logout_event = getConfig().getStringList("options.player_ct_logout_event");
            enemy_ct_logout_event = getConfig().getStringList("options.enemy_ct_logout_event");
            player_teleport_cooldown = getConfig().getInt("options.player_teleport_cooldown", 5);
            player_respawn_cooldown = getConfig().getInt("options.player_respawn_cooldown", 5);
            hookfeather_use_cooldown = getConfig().getInt("options.hookfeather_use_cooldown", 10);
            per_earn_seconds = getConfig().getDouble("options.per_earn_seconds", 1.0);
            earn_amount = getConfig().getString("options.earn_amount", "%player_level%");
            flag_broken_loss = getConfig().getString("options.flag_broken_loss", "%vault_eco_balance%*(50/100)");
            player_killed_loss = getConfig().getString("options.player_killed_loss", "%vault_eco_balance%*(10/100)");
            player_ct_logout_loss = getConfig().getString("options.player_ct_logout_loss", "%vault_eco_balance%*(10/100)");
            killstreak_offset_count = getConfig().getInt("options.killstreak_offset_count", 10);
            levelup_cost = getConfig().getString("options.levelup_cost", "250*(1.1)^%player_level%");
            respawn_cooldown = getConfig().getBoolean("options.respawn_cooldown", true);
            respawn_keeps_player_location = getConfig().getBoolean("options.respawn_keeps_player_location", true);
            spawn_location = getConfig().getString("options.spawn_location", "");
            respawn_location = getConfig().getString("options.respawn_location", "");
            spectator_gamemode = getConfig().getString("options.spectator_gamemode", "SPECTATOR");
            respawn_gamemode = getConfig().getString("options.respawn_gamemode", "SURVIVAL");
            flag_gui_displayname = getConfig().getString("options.flag_gui_displayname", "&lYour Flag");
            player_flag_breaking_countdown = getConfig().getInt("options.player_flag_breaking_countdown", 15);
            breaking_hologram_height = getConfig().getDouble("options.breaking_hologram.height", 0.5);
            breaking_hologram_text = getConfig().getStringList("options.breaking_hologram.text");
            breaking_hologram_lines_height = getConfig().getDouble("options.breaking_hologram.lines_height", 0.26);
            hookfeather_material = getConfig().getString("options.hookfeather.material", "FEATHER");
            hookfeather_jump_boost = getConfig().getInt("options.hookfeather.jump_boost", 2);
            hookfeather_jump_forward = getConfig().getDouble("options.hookfeather.jump_forward", 0.3);
            hookfeather_actionbar_text = getConfig().getString("options.hookfeather.actionbar_text", "%remaining_seconds%");
            allowed_blocks_break = getConfig().getStringList("options.allowed_blocks_break");
            afk_time = getConfig().getInt("options.afk_time", 1800);
            afk_event = getConfig().getStringList("options.afk_event");
            use_expbar_to_display_level = getConfig().getBoolean("options.use_expbar_to_display_level", true);
            no_earn_regions = getConfig().getStringList("options.no_earn_regions");
            teleport_to_spawn_on_quit = getConfig().getBoolean("options.teleport_to_spawn_on_quit", true);
            flag_place_block_blacklist = getConfig().getStringList("options.flag_place_block_blacklist");
            data_save_delay = getConfig().getInt("options.data_save_delay", 60);
            top_refresh_delay = getConfig().getInt("options.top_refresh_delay", 600);

            prefix = getConfig().getString("messages.prefix", "");
            configuration_reloaded = getConfig().getString("messages.configuration_reloaded", "&aConfiguration successfully reloaded");
            an_error_occurred = getConfig().getString("messages.an_error_occurred", "&cAn error occurred while doing this task...");
            no_permission = getConfig().getString("messages.no_permission", "&cYou're not permitted to do this.");
            incomplete_command = getConfig().getString("messages.incomplete_command", "&cIncomplete command! use the help command for the args list.");
            only_players = getConfig().getString("messages.only_players", "&cOnly players can do this.");
            unknown_subcommand = getConfig().getString("messages.unknown_subcommand", "&cUnknown subcommand, use the help command for the args list.");
            spawn_location_set = getConfig().getString("messages.spawn_location_set", "&aSpawn location set!");
            respawn_location_set = getConfig().getString("messages.respawn_location_set", "&aRespawning location set!");
            no_spawn_set = getConfig().getString("messages.no_spawn_set", "&cNo spawn location set!");
            teleporting_you = getConfig().getString("messages.teleporting_you", "&bTeleporting you in &f%seconds% &bseconds! &7please don't move!");
            player_not_found = getConfig().getString("messages.player_not_found", "&cPlayer not found");
            not_enough_money = getConfig().getString("messages.not_enough_money", "&c&cYou don't have enough money to do that!");
            hold_an_item = getConfig().getString("messages.hold_an_item", "&cYou have to hold an item!");
            flag_item_added = getConfig().getString("messages.flag_item_added", "&aFlag item added to the list");
            only_placeable_blocks = getConfig().getString("messages.only_placeable_blocks", "&cYou can do it only with placeable blocks!");
            teleporting_player = getConfig().getString("messages.teleporting_player", "&7Teleporting &f%player_name%");
            flag_not_placed = getConfig().getString("messages.flag_not_placed", "&cNo flag has been placed");
            flag_broken = getConfig().getString("messages.flag_broken", "&cthe &f%player%&c's flag has been broken.");
            hookfeather_cooldown_message = getConfig().getString("messages.hookfeather_cooldown_message", "&cYou have to wait %cooldown_seconds% seconds to use it again.");
            you_moved = getConfig().getString("messages.you_moved", "&cYou moved! teleportation canceled.");
            unknown_statistic = getConfig().getString("messages.unknown_statistic", "&cUnknown statistic name &f%statistic%&c. You can use LEVEL, KILLS, DEATHS, BROKEN_FLAGS");
            unknown_action = getConfig().getString("messages.unknown_action", "&cUnknown action name &f%action%&c. You can use RESET, SET, ADD, REMOVE");
            statistic_reset = getConfig().getString("messages.statistic_reset", "&eThis &f%player_name%&e's statistic has been reset.");
            statistic_set = getConfig().getString("messages.statistic_set", "&eThis &f%player_name%&e's statistic has been set to &f%amount%");
            an_integer_needed = getConfig().getString("messages.an_integer_needed", "&cAn integer number is needed!");
            nobody = getConfig().getString("messages.nobody", "Nobody");
            cant_go_to_spawn = getConfig().getString("messages.cant_go_to_spawn", "&cYou can't go to spawn when you have a placed flag.");
            banner_placed_placeholder = getConfig().getString("messages.banner_placed_placeholder", "Flag placed");
            banner_not_placed_placeholder = getConfig().getString("messages.banner_not_placed_placeholder", "Flag not placed");
            pos1_set = getConfig().getString("messages.pos1_set", "&bPosition 1 set to &f%x%,%y%,%z%,%world%");
            pos2_set = getConfig().getString("messages.pos2_set", "&bPosition 2 set to &f%x%,%y%,%z%,%world%");
            invalid_selection = getConfig().getString("messages.invalid_selection", "&cInvalid selection! try again by setting the pos1 and the pos2.");
            added_no_earn_region = getConfig().getString("messages.added_no_earn_region", "&aNo Earn Region added to the list.");
            cant_place_there = getConfig().getString("messages.cant_place_there", "&cYou're not allowed to place your flag there.");
            stats = getConfig().getStringList("messages.stats");
            stats_others = getConfig().getStringList("messages.stats_others");
            help_user = getConfig().getStringList("messages.help_user");
            help_admin = getConfig().getStringList("messages.help_admin");
            top_levels = getConfig().getStringList("messages.top_levels");

            if (dataConfig != null) saveDataConfig();
            reloadDataConfig();

            dataConfig.options().header("Developed with LOV by StivenFocs");
            dataConfig.options().copyDefaults(true);

            saveDataConfig();
            reloadDataConfig();

            for (Inventory gui : new ArrayList<>(FlagMenu.flags_gui.values())) {
                for (HumanEntity viewer : new ArrayList<>(gui.getViewers())) {
                    viewer.closeInventory();
                }
            }

            Vars.plugin.runMainTask();

            plugin.getLogger().info("Configuration successfully reloaded");
            return true;
        } catch (Exception ex) {
            plugin.getLogger().severe("An exception occurred while reload the whole configuration!! disabling plugin...");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
    }

    ////////////////////////////////////////////

    public void saveDataConfig() {
        if (!dataFile.exists()) {
            try {
                if (!dataFile.createNewFile()) {
                    plugin.getLogger().severe("Unable to generate the data configuration!!");
                }
            } catch (Exception ex) {
                plugin.getLogger().severe("Unable to generate the data configuration!!");
                ex.printStackTrace();
            }
            dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        }

        try {
            dataConfig.save(dataFile);
        } catch (Exception ex) {
            plugin.getLogger().severe("Unable to save the data configuration!!");
            ex.printStackTrace();
        }
    }

    public void reloadDataConfig() {
        if (!dataFile.exists()) {
            try {
                if (!dataFile.createNewFile()) {
                    plugin.getLogger().severe("Unable to generate the data configuration!!");
                }
            } catch (Exception ex) {
                plugin.getLogger().severe("Unable to generate the data configuration!!");
                ex.printStackTrace();
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    ///////////////////////////////////////////////

    public static boolean hasUserPermission(String permission, CommandSender user) {
        return user.hasPermission("quantus.user." + permission);
    }

    public static boolean hasAdminPermission(String permission, CommandSender user) {
        return user.hasPermission("quantus.admin." + permission);
    }

    public static String setPlaceholders(String text, CommandSender user) {
        text = text.replace("%prefix%", Vars.prefix);
        text = text.replace("%author%", plugin.getDescription().getAuthors().get(0));
        text = text.replace("%version%", plugin.getDescription().getVersion());

        HashMap<String, Integer> top_levels = PlayerData.getTopLevels();
        for (int i = 0; i < top_levels.size(); i++) {
            String name = new ArrayList<>(top_levels.keySet()).get(i);
            text = text.replace("%top_level_" + (i + 1) + "_name%", name);
            text = text.replace("%top_level_" + (i + 1) + "_amount%", String.valueOf(top_levels.get(name)));
        }
        for (int i = top_levels.size(); i < top_levels.size() + 51; i++) {
            text = text.replace("%top_level_" + i + "_name%", Vars.nobody).replace("%top_level_" + i + "_amount%", "0");
        }

        if (user != null) {
            text = text.replaceAll("%name%", user.getName());
            if (user instanceof Player) {
                Player p = (Player) user;
                PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

                text = text.replace("%displayname%", p.getDisplayName());
                text = text.replace("%uuid%", String.valueOf(p.getUniqueId()));

                text = text.replace("%x%", String.valueOf(p.getLocation().getBlockX()));
                text = text.replace("%y%", String.valueOf(p.getLocation().getBlockY()));
                text = text.replace("%z%", String.valueOf(p.getLocation().getBlockZ()));
                text = text.replace("%world%", p.getLocation().getWorld().getName());

                if (GeneralEvents.player_breaking_his_flag.containsKey(p.getUniqueId())) {
                    text = text.replace("%flag_breaking_countdown%", String.valueOf(GeneralEvents.player_breaking_his_flag.get(p.getUniqueId())));
                }

                if (p_data.hasPlacedFlag()) {
                    text = text.replace("%player_flag_x%", String.valueOf(p_data.getFlagLocation().getBlockX()));
                    text = text.replace("%player_flag_y%", String.valueOf(p_data.getFlagLocation().getBlockY()));
                    text = text.replace("%player_flag_z%", String.valueOf(p_data.getFlagLocation().getBlockZ()));
                    text = text.replace("%player_flag_world%", p_data.getFlagLocation().getWorld().getName());
                } else {
                    text = text.replace("%player_flag_x%", "0");
                    text = text.replace("%player_flag_y%", "0");
                    text = text.replace("%player_flag_z%", "0");
                    text = text.replace("%player_flag_world%", Bukkit.getWorlds().get(0).getName());
                }
                text = text.replace("%player_kills%", String.valueOf(p_data.getKills()));
                text = text.replace("%player_deaths%", String.valueOf(p_data.getDeaths()));
                text = text.replace("%player_broken_flags%", String.valueOf(p_data.getBrokenFlags()));
                text = text.replace("%player_next_level%", String.valueOf(p_data.getLevel() + 1));
                text = text.replace("%player_level%", String.valueOf(p_data.getLevel()));
                text = text.replace("%player_multiplier%", String.valueOf(p_data.getMultiplier()));
                text = text.replace("%player_killstreak%", String.valueOf(Vars.alternativeIfNull(GeneralEvents.player_killstreaks.get(p_data.getPlayerUniqueId()), 0)));
                text = text.replace("%player_levelup_cost%", String.valueOf(p_data.getLevelupCost()));
                text = text.replace("%player_levelup_cost_format%", moneyFormat(String.valueOf(p_data.getLevelupCost())));
                text = text.replace("%player_earned_amount%", String.valueOf(p_data.getEarnAmount()));
                text = text.replace("%player_earned_amount_format%", moneyFormat(String.valueOf(p_data.getEarnAmount())));
                text = text.replace("%player_next_earned_amount%", String.valueOf(p_data.getNextEarnAmount()));
                text = text.replace("%player_next_earned_amount_format%", moneyFormat(String.valueOf(p_data.getNextEarnAmount())));
            }
        }

        return text;
    }

    public static String setOfflinePlayerPlaceholders(String text, OfflinePlayer user) {
        text = text.replace("%prefix%", Vars.prefix);
        text = text.replace("%author%", plugin.getDescription().getAuthors().get(0));
        text = text.replace("%version%", plugin.getDescription().getVersion());

        HashMap<String, Integer> top_levels = PlayerData.getTopLevels();
        for (int i = 0; i < top_levels.size(); i++) {
            String name = new ArrayList<>(top_levels.keySet()).get(i);
            text = text.replace("%top_level_" + (i + 1) + "_name%", name);
            text = text.replace("%top_level_" + (i + 1) + "_amount%", String.valueOf(top_levels.get(name)));
        }
        for (int i = top_levels.size(); i < top_levels.size() + 51; i++) {
            text = text.replace("%top_level_" + i + "_name%", Vars.nobody).replace("%top_level_" + i + "_amount%", "0");
        }

        if (user != null) {
            text = text.replaceAll("%name%", user.getName());
            if (user instanceof OfflinePlayer) {
                OfflinePlayer p = user;
                PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

                text = text.replace("%uuid%", String.valueOf(p.getUniqueId()));

                if (GeneralEvents.player_breaking_his_flag.containsKey(p.getUniqueId())) {
                    text = text.replace("%flag_breaking_countdown%", String.valueOf(GeneralEvents.player_breaking_his_flag.get(p.getUniqueId())));
                }

                if (p_data.hasPlacedFlag()) {
                    text = text.replace("%player_flag_x%", String.valueOf(p_data.getFlagLocation().getBlockX()));
                    text = text.replace("%player_flag_y%", String.valueOf(p_data.getFlagLocation().getBlockY()));
                    text = text.replace("%player_flag_z%", String.valueOf(p_data.getFlagLocation().getBlockZ()));
                    text = text.replace("%player_flag_world%", p_data.getFlagLocation().getWorld().getName());
                } else {
                    text = text.replace("%player_flag_x%", "0");
                    text = text.replace("%player_flag_y%", "0");
                    text = text.replace("%player_flag_z%", "0");
                    text = text.replace("%player_flag_world%", Bukkit.getWorlds().get(0).getName());
                }
                text = text.replace("%player_kills%", String.valueOf(p_data.getKills()));
                text = text.replace("%player_deaths%", String.valueOf(p_data.getDeaths()));
                text = text.replace("%player_broken_flags%", String.valueOf(p_data.getBrokenFlags()));
                text = text.replace("%player_next_level%", String.valueOf(p_data.getLevel() + 1));
                text = text.replace("%player_level%", String.valueOf(p_data.getLevel()));
                text = text.replace("%player_multiplier%", String.valueOf(p_data.getMultiplier()));
                text = text.replace("%player_killstreak%", String.valueOf(Vars.alternativeIfNull(GeneralEvents.player_killstreaks.get(p_data.getPlayerUniqueId()), 0)));
                text = text.replace("%player_levelup_cost%", String.valueOf(p_data.getLevelupCost()));
                text = text.replace("%player_levelup_cost_format%", moneyFormat(String.valueOf(p_data.getLevelupCost())));
                text = text.replace("%player_earned_amount%", String.valueOf(p_data.getEarnAmount()));
                text = text.replace("%player_earned_amount_format%", moneyFormat(String.valueOf(p_data.getEarnAmount())));
                text = text.replace("%player_next_earned_amount%", String.valueOf(p_data.getNextEarnAmount()));
                text = text.replace("%player_next_earned_amount_format%", moneyFormat(String.valueOf(p_data.getNextEarnAmount())));
            }
        }

        return text;
    }

    public static String setPlaceholdersForLevelupCost(String text, OfflinePlayer user) {
        text = text.replace("%author%", plugin.getDescription().getAuthors().get(0));
        text = text.replace("%version%", plugin.getDescription().getVersion());

        HashMap<String, Integer> top_levels = PlayerData.getTopLevels();
        for (int i = 0; i < top_levels.size(); i++) {
            String name = new ArrayList<>(top_levels.keySet()).get(i);
            text = text.replace("%top_level_" + (i + 1) + "_name%", name);
            text = text.replace("%top_level_" + (i + 1) + "_amount%", String.valueOf(top_levels.get(name)));
        }
        for (int i = top_levels.size(); i < top_levels.size() + 51; i++) {
            text = text.replace("%top_level_" + i + "_name%", Vars.nobody).replace("%top_level_" + i + "_amount%", "0");
        }

        if (user != null) {
            text = text.replaceAll("%name%", user.getName());
            if (user instanceof OfflinePlayer) {
                OfflinePlayer p = user;
                PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

                text = text.replace("%prefix%", Vars.prefix);

                text = text.replace("%uuid%", String.valueOf(p.getUniqueId()));

                if (GeneralEvents.player_breaking_his_flag.containsKey(p.getUniqueId())) {
                    text = text.replace("%flag_breaking_countdown%", String.valueOf(GeneralEvents.player_breaking_his_flag.get(p.getUniqueId())));
                }

                if (p_data.hasPlacedFlag()) {
                    text = text.replace("%flag_x%", String.valueOf(p_data.getFlagLocation().getBlockX()));
                    text = text.replace("%flag_y%", String.valueOf(p_data.getFlagLocation().getBlockY()));
                    text = text.replace("%flag_z%", String.valueOf(p_data.getFlagLocation().getBlockZ()));
                    text = text.replace("%flag_world%", p_data.getFlagLocation().getWorld().getName());
                } else {
                    text = text.replace("%flag_x%", "0");
                    text = text.replace("%flag_y%", "0");
                    text = text.replace("%flag_z%", "0");
                    text = text.replace("%flag_world%", Bukkit.getWorlds().get(0).getName());
                }
                text = text.replace("%player_kills%", String.valueOf(p_data.getKills()));
                text = text.replace("%player_deaths%", String.valueOf(p_data.getDeaths()));
                text = text.replace("%player_broken_flags%", String.valueOf(p_data.getBrokenFlags()));
                text = text.replace("%player_next_level%", String.valueOf(p_data.getLevel() + 1));
                text = text.replace("%player_level%", String.valueOf(p_data.getLevel()));
                text = text.replace("%player_multiplier%", String.valueOf(p_data.getMultiplier()));
                text = text.replace("%player_killstreak%", String.valueOf(Vars.alternativeIfNull(GeneralEvents.player_killstreaks.get(p_data.getPlayerUniqueId()), 0)));
                //text = text.replace("%player_earned_amount%", String.valueOf(p_data.getEarnAmount()));
                //text = text.replace("%player_next_earned_amount%", String.valueOf(p_data.getNextEarnAmount()));
            }
        }

        return text;
    }

    public static String setPlaceholdersForEarn(String text, OfflinePlayer user) {
        text = text.replace("%author%", plugin.getDescription().getAuthors().get(0));
        text = text.replace("%version%", plugin.getDescription().getVersion());

        HashMap<String, Integer> top_levels = PlayerData.getTopLevels();
        for (int i = 0; i < top_levels.size(); i++) {
            String name = new ArrayList<>(top_levels.keySet()).get(i);
            text = text.replace("%top_level_" + (i + 1) + "_name%", name);
            text = text.replace("%top_level_" + (i + 1) + "_amount%", String.valueOf(top_levels.get(name)));
        }
        for (int i = top_levels.size(); i < top_levels.size() + 51; i++) {
            text = text.replace("%top_level_" + i + "_name%", Vars.nobody).replace("%top_level_" + i + "_amount%", "0");
        }

        if (user != null) {
            text = text.replaceAll("%name%", user.getName());
            if (user instanceof OfflinePlayer) {
                OfflinePlayer p = (OfflinePlayer) user;
                PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

                text = text.replace("%prefix%", Vars.prefix);

                text = text.replace("%uuid%", String.valueOf(p.getUniqueId()));

                if (GeneralEvents.player_breaking_his_flag.containsKey(p.getUniqueId())) {
                    text = text.replace("%flag_breaking_countdown%", String.valueOf(GeneralEvents.player_breaking_his_flag.get(p.getUniqueId())));
                }

                if (p_data.hasPlacedFlag()) {
                    text = text.replace("%flag_x%", String.valueOf(p_data.getFlagLocation().getBlockX()));
                    text = text.replace("%flag_y%", String.valueOf(p_data.getFlagLocation().getBlockY()));
                    text = text.replace("%flag_z%", String.valueOf(p_data.getFlagLocation().getBlockZ()));
                    text = text.replace("%flag_world%", p_data.getFlagLocation().getWorld().getName());
                } else {
                    text = text.replace("%flag_x%", "0");
                    text = text.replace("%flag_y%", "0");
                    text = text.replace("%flag_z%", "0");
                    text = text.replace("%flag_world%", Bukkit.getWorlds().get(0).getName());
                }
                text = text.replace("%player_kills%", String.valueOf(p_data.getKills()));
                text = text.replace("%player_deaths%", String.valueOf(p_data.getDeaths()));
                text = text.replace("%player_broken_flags%", String.valueOf(p_data.getBrokenFlags()));
                text = text.replace("%player_next_level%", String.valueOf(p_data.getLevel() + 1));
                text = text.replace("%player_level%", String.valueOf(p_data.getLevel()));
                text = text.replace("%player_multiplier%", String.valueOf(p_data.getMultiplier()));
                text = text.replace("%player_killstreak%", String.valueOf(Vars.alternativeIfNull(GeneralEvents.player_killstreaks.get(p_data.getPlayerUniqueId()), 0)));
                //text = text.replace("%player_levelup_cost%", String.valueOf(p_data.getLevelUpNeeded()));
            }
        }

        return text;
    }

    ///////////////////////////////////////////////

    public static void sendString(String text, CommandSender user) {
        if (text.length() > 0) {
            if (Vars.plugin.getConfig().getString(text) != null) text = Vars.plugin.getConfig().getString(text);
            for (String line : text.split("/n")) {
                line = prefix + setPlaceholders(line, user);
                if (user instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                    line = PlaceholderAPI.setPlaceholders((Player) user, line);

                if (isValidJson(line) && user instanceof Player) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + user.getName() + " " + line);
                }

                user.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
    }

    public static void sendStringList(List<String> string_list, CommandSender user) {
        for (String line_ : string_list) {
            if (line_.length() > 0 && Vars.plugin.getConfig().getString(line_) != null) line_ = Vars.plugin.getConfig().getString(line_);
            for (String line : line_.split("/n")) {
                line = prefix + setPlaceholders(line, user);
                if (user instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                    line = PlaceholderAPI.setPlaceholders((Player) user, line);

                if (isValidJson(line) && user instanceof Player) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + user.getName() + " " + line);
                }

                user.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
    }

    public static List<String> coloredList(List<String> uncolored_list) {
        List<String> colored_list = new ArrayList<>();
        for (String line : uncolored_list) {
            colored_list.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return colored_list;
    }

    public static ItemStack createItem(String displayname, Material material, Short durability, String[] lore, ItemFlag[] itemFlags) {
        ItemStack i = new ItemStack(material);
        i.setDurability(durability);
        ItemMeta iMeta = i.getItemMeta();
        iMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));
        List<String> lore_ = new ArrayList<>();
        for(String line : lore) {
            lore_.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        iMeta.setLore(lore_);
        iMeta.addItemFlags(itemFlags);
        i.setItemMeta(iMeta);
        return i;
    }

    public static ItemStack getItem(String itemName, Player player) {
        if (debug) plugin.getLogger().info("Getting the item: " + itemName);
        String path = "items." + itemName + ".";
        if (debug) plugin.getLogger().info("path: " + path);
        if (plugin.getConfig().get("items." + itemName) == null) {
            throw new NullPointerException("Item not found: " + itemName);
        }

        try {
            ItemStack i;
            try {
                if (Vars.debug) Vars.plugin.getLogger().info("Creating ItemStack");
                i = new ItemStack(Material.valueOf(plugin.getConfig().getString(path + "material").toUpperCase()));
            } catch (Exception ex) {
                i = new ItemStack(Material.BEDROCK, 0);
                Vars.plugin.getLogger().warning("Couldn't find the material for the item " + itemName + " item...");
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
            if (Vars.debug) Vars.plugin.getLogger().info("Applying displayname");
            String displayname = plugin.getConfig().getString(path + "displayname");
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) displayname = PlaceholderAPI.setPlaceholders(player, displayname);
            iMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', setPlaceholders(displayname, player)));
            if (Vars.debug) Vars.plugin.getLogger().info("Applying lore");
            List<String> colored_list = new ArrayList<>();
            for (String line : plugin.getConfig().getStringList(path + "lore")) {
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) line = PlaceholderAPI.setPlaceholders(player, line);
                colored_list.add(ChatColor.translateAlternateColorCodes('&', setPlaceholders(line, player)));
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
            ex.printStackTrace();
            throw new NullPointerException("Couldn't get a valid item from " + itemName);
        }
    }

    public static String locationToString(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }
    public static String locationBlockToString(Location loc, Boolean include_world) {
        if (include_world) {
            return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
        }
        return loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    public static Location stringToLocation(String strloc) {
        try {
            String[] strloc_ = strloc.split(",");
            if (strloc_.length > 5) {
                return new Location(Bukkit.getWorld(strloc_[0]), Double.parseDouble(strloc_[1]), Double.parseDouble(strloc_[2]), Double.parseDouble(strloc_[3]), Float.parseFloat(strloc_[4]), Float.parseFloat(strloc_[5]));
            } else {
                return new Location(Bukkit.getWorld(strloc_[0]), Double.parseDouble(strloc_[1]), Double.parseDouble(strloc_[2]), Double.parseDouble(strloc_[3]));
            }
        } catch (Exception ex) {
            Vars.plugin.getLogger().info("An exception occurred while trying to load a location from string: " + strloc);
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean isValidJson(String json) {
        try {
            return new JsonParser().parse(json).getAsJsonObject() != null;
        } catch (Throwable ignored) {}

        try {
            return new JsonParser().parse(json).getAsJsonArray() != null;
        } catch (Throwable ignored) {}

        return false;
    }

    public static boolean isDigit(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isDoubleDigit(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static void playWorldSound(String sound_name, Location loc) {
        if (debug) plugin.getLogger().info("Getting the sound: " + sound_name);
        String path = "sounds." + sound_name + ".";
        if (debug) plugin.getLogger().info("path: " + path);

        if (plugin.getConfig().getString(path + "name").equalsIgnoreCase("none")) return;


        try {
            Sound sound;
            try {
                sound = Sound.valueOf(plugin.getConfig().getString(path + "name"));
            } catch (Exception ex) {
                plugin.getLogger().severe("Couldn't find the minecraft sound named " + plugin.getConfig().getString(path + "name") + " in the configuration sound: " + sound_name);
                return;
            }

            loc.getWorld().playSound(loc, sound, plugin.getConfig().getInt(path + "volume"), plugin.getConfig().getInt(path + "pitch"));
        } catch (Exception ex) {
            plugin.getLogger().warning("An exception occurred while trying to retrieve a sound from the configuration (" + sound_name + ")");
            ex.printStackTrace();
        }
    }

    private static Field bukkitCommandMap = null;

    static {
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
    }

    public static boolean isBukkitCommand(String paramString) {
        paramString = paramString.split(" ")[0];
        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) bukkitCommandMap.get(Bukkit.getServer());
            for (Command command : simpleCommandMap.getCommands()) {
                if (command.getName().equalsIgnoreCase(paramString) || command.getAliases().contains(paramString))
                    return true;
            }
        } catch (IllegalAccessException ex) {
            Vars.plugin.getLogger().warning("An exception occurred while trying to retrieve and/or use the commandMap");
            ex.printStackTrace();
        }
        return false;
    }

    public static String timeFormat(int seconds) {
        int hours = 0;
        int minutes = 0;
        String game_time;

        while (true) {
            if (seconds >= 60) {
                if (seconds >= 3600) {
                    hours++;
                    seconds = seconds - 3600;
                } else {
                    minutes++;
                    seconds = seconds - 60;
                }
            } else {
                break;
            }
        }
        if (hours > 0) {
            if (String.valueOf(seconds).length() <= 1) {
                game_time = hours + ":" + minutes + ":0" + seconds;
            } else {
                if (String.valueOf(minutes).length() <= 1) {
                    game_time = hours + ":0" + minutes + ":" + seconds;
                } else {
                    game_time = hours + ":" + minutes + ":" + seconds;
                }
            }
        } else {
            if (String.valueOf(seconds).length() <= 1) {
                game_time = minutes + ":0" + seconds;
            } else {
                if (String.valueOf(minutes).length() <= 1) {
                    game_time = "0" + minutes + ":" + seconds;
                } else {
                    game_time = minutes + ":" + seconds;
                }
            }
        }

        return game_time;
    }

    public static String moneyFormat(String money) {
        String[] money_ = money.split("\\.");
        String final_money = "";
        if (money_.length > 1) final_money = "." + money_[1];
        char[] money0 = (money_[0]).toCharArray();
        List<Character> money0_ = new ArrayList<>();
        for (char money0__ : money0) {
            money0_.add(money0__);
        }
        Collections.reverse(money0_);
        int count = 0;
        for (char money_char : money0_) {
            count++;

            if (count > 3) {
                count = 1;
                final_money = money_char + "," + final_money;
            } else final_money = money_char + final_money;
        }

        return final_money;
    }

    public static void runActions(List<String> actions, HashMap<String, String> custom_replaces, Player p) {
        for (String command : actions) {
            try {
                command = command.replace("&", "§");
                command = command.replace("%player_name%", p.getName()).replace("%player_displayname%", p.getDisplayName()).replaceAll("%player_uuid%", p.getUniqueId().toString()).replaceAll("%world%", p.getWorld().getName()).replaceAll("%x%", String.valueOf(p.getLocation().getX())).replaceAll("%y%", String.valueOf(p.getLocation().getY())).replaceAll("%z%", String.valueOf(p.getLocation().getZ()));
                command = Vars.setPlaceholders(command, p);
                for (String placeholder : custom_replaces.keySet()) {
                    command = command.replace(placeholder, custom_replaces.get(placeholder));
                }
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) command = PlaceholderAPI.setPlaceholders(p, command);

                if (command.startsWith("tell:")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', command.replaceAll("tell:", "")));
                } else if (command.startsWith("broadcast:")) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', command.replace("broadcast:", "")));
                } else if (command.startsWith("sudo:")) {
                    command = command.replaceAll("sudo:", "");
                    if (Vars.isBukkitCommand(command)) {
                        p.performCommand(command);
                    } else {
                        p.chat("/" + command);
                    }
                } else if (command.startsWith("permitted_sudo:")) {
                    command = command.replaceAll("permitted_sudo:", "");
                    boolean op = p.isOp();

                    p.setOp(true);
                    if (Vars.isBukkitCommand(command)) {
                        p.performCommand(command);
                    } else {
                        p.chat("/" + command);
                    }
                    p.setOp(op);
                } else if (command.startsWith("actionbar:")) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', command.replace("actionbar:", ""))));
                } else if (command.startsWith("broadcastsound:")) {
                    command = command.replaceAll(" ", "");
                    String[] sound = command.replaceAll("broadcastsound:", "").split(",");
                    for (Player p_ : Bukkit.getOnlinePlayers()) {
                        p_.playSound(p_.getLocation(), Sound.valueOf(sound[0].toUpperCase()), Integer.parseInt(sound[1]), Integer.parseInt(sound[2]));
                    }
                }  else if (command.startsWith("sound:")) {
                    command = command.replaceAll(" ", "");
                    String[] sound = command.replaceAll("sound:", "").split(",");
                    p.playSound(p.getLocation(), Sound.valueOf(sound[0].toUpperCase()), Integer.parseInt(sound[1]), Integer.parseInt(sound[2]));
                } else if (command.startsWith("effect:")) {
                    command = command.replaceAll(" ", "");
                    String[] effect = command.replaceAll("effect:", "").split(",");
                    p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect[0]), Integer.parseInt(effect[1]), Integer.parseInt(effect[2]), Boolean.parseBoolean(effect[3]), Boolean.parseBoolean(effect[4]), Boolean.parseBoolean(effect[5])));
                } else if (command.startsWith("particle:")) {
                    command = command.replaceAll(" ", "");
                    String[] particle = command.replaceAll("particle:", "").split(",");
                    Bukkit.getWorld(particle[1]).spawnParticle(Particle.valueOf(particle[0].toUpperCase()), Double.parseDouble(particle[2]), Double.parseDouble(particle[3]), Double.parseDouble(particle[4]), Integer.parseInt(particle[5]), Double.parseDouble(particle[6]), Double.parseDouble(particle[7]), Double.parseDouble(particle[8]), Double.parseDouble(particle[9]));
                } else if (command.startsWith("title:")) {
                    String[] title = command.replaceAll("title:", "").split(",");
                    p.sendTitle(title[0],title[1],Integer.parseInt(title[2]),Integer.parseInt(title[3]),Integer.parseInt(title[4]));
                } else if (command.startsWith("money:")) {
                    if (Vars.econ != null) {
                        String[] money = command.replaceAll("money:", "").split(",");
                        ScriptEngineManager mgr = new ScriptEngineManager();
                        ScriptEngine engine = mgr.getEngineByName("JavaScript");
                        if (money[0].equalsIgnoreCase("give")) {
                            Vars.econ.depositPlayer(p, Double.parseDouble(money[1]));
                        } else if (money[0].equalsIgnoreCase("take")) {
                            Vars.econ.withdrawPlayer(p, Double.parseDouble(money[1]));
                        }
                    } else {
                        if (debug) plugin.getLogger().warning("Unable to interact with the economy!");
                    }
                } else if (command.startsWith("teleport:")) {
                    String teleport = command.replaceAll("teleport:", "");
                    p.teleport(Vars.stringToLocation(teleport));
                } else if (command.startsWith("close_inventory")) {
                    p.closeInventory();
                } else if (command.equalsIgnoreCase("fireball_cannon")) {
                    p.launchProjectile(Fireball.class);
                } else if (command.equalsIgnoreCase("egg_cannon")) {
                    p.launchProjectile(Egg.class);
                } else if (command.equalsIgnoreCase("snowball_cannon")) {
                    p.launchProjectile(Snowball.class);
                } else if (command.startsWith("sethealth")) {
                    String[] sethealth = command.split(":");
                    p.setHealth(Double.parseDouble(sethealth[1]));
                } else if (command.startsWith("setmaxhealth")) {
                    String[] setmaxhealth = command.split(":");
                    p.setMaxHealth(Double.parseDouble(setmaxhealth[1]));
                } else if (command.equalsIgnoreCase("lightning")) {
                    Block target_block = p.getTargetBlock(null, 50);
                    if (!target_block.getType().equals(Material.AIR)) {
                        p.getWorld().strikeLightning(target_block.getLocation());
                    }
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            } catch (Exception ex) {
                Vars.plugin.getLogger().warning("Unable to run the command: " + command + " Reason: " + ex.getMessage());
            }
        }
    }

    public static HashMap<String, String> generateCustomReplaces(OfflinePlayer p, String player_reference_name) {
        HashMap<String, String> custom_replaces = new HashMap<>();
        PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

        custom_replaces.put("%" + player_reference_name + "_name%", p.getName());
        custom_replaces.put("%" + player_reference_name + "_uuid%", String.valueOf(p.getUniqueId()));
        if (p.isOnline()) {
            Player p_ = p.getPlayer();
            custom_replaces.put("%" + player_reference_name + "_displayname%", p_.getDisplayName());
            custom_replaces.put("%" + player_reference_name + "_x%", String.valueOf(p_.getLocation().getX()));
            custom_replaces.put("%" + player_reference_name + "_y%", String.valueOf(p_.getLocation().getY()));
            custom_replaces.put("%" + player_reference_name + "_z%", String.valueOf(p_.getLocation().getZ()));
            custom_replaces.put("%" + player_reference_name + "_x_block%", String.valueOf(p_.getLocation().getBlockX()));
            custom_replaces.put("%" + player_reference_name + "_y_block%", String.valueOf(p_.getLocation().getBlockY()));
            custom_replaces.put("%" + player_reference_name + "_z_block%", String.valueOf(p_.getLocation().getBlockZ()));
            custom_replaces.put("%" + player_reference_name + "_world%", p_.getLocation().getWorld().getName());
        }
        if (p_data.hasPlacedFlag()) {
            custom_replaces.put("%" + player_reference_name + "_flag_x%", String.valueOf(p_data.getFlagLocation().getBlockX()));
            custom_replaces.put("%" + player_reference_name + "_flag_y%", String.valueOf(p_data.getFlagLocation().getBlockY()));
            custom_replaces.put("%" + player_reference_name + "_flag_z%", String.valueOf(p_data.getFlagLocation().getBlockZ()));
            custom_replaces.put("%" + player_reference_name + "_flag_world%", p_data.getFlagLocation().getWorld().getName());
        } else {
            custom_replaces.put("%" + player_reference_name + "_flag_x%", "0");
            custom_replaces.put("%" + player_reference_name + "_flag_y%", "0");
            custom_replaces.put("%" + player_reference_name + "_flag_z%", "0");
            custom_replaces.put("%" + player_reference_name + "_flag_world%", Bukkit.getWorlds().get(0).getName());
        }
        custom_replaces.put("%" + player_reference_name + "_kills%", String.valueOf(p_data.getKills()));
        custom_replaces.put("%" + player_reference_name + "_deaths%", String.valueOf(p_data.getDeaths()));
        custom_replaces.put("%" + player_reference_name + "_broken_flags%", String.valueOf(p_data.getBrokenFlags()));
        custom_replaces.put("%" + player_reference_name + "_level%", String.valueOf(p_data.getLevel()));
        custom_replaces.put("%" + player_reference_name + "_killstreak%", String.valueOf(Vars.alternativeIfNull(GeneralEvents.player_killstreaks.get(p_data.getPlayerUniqueId()), 0)));
        custom_replaces.put("%" + player_reference_name + "_levelup_cost%", String.valueOf(p_data.getLevelupCost()));
        custom_replaces.put("%" + player_reference_name + "_earned_amount%", String.valueOf(p_data.getEarnAmount()));

        return custom_replaces;
    }

    public static ArmorStand newArmorStand(Location loc) {
        ArmorStand armorstand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorstand.setInvisible(true);
        armorstand.setBasePlate(false);
        armorstand.setMarker(false);
        armorstand.setCustomNameVisible(true);
        armorstand.setInvulnerable(true);
        armorstand.setGravity(false);
        armorstand.setCanPickupItems(false);
        armorstand.setSmall(false);
        armorstand.setMetadata("pluginOwner", new FixedMetadataValue(Vars.plugin, "dummyText"));

        return armorstand;
    }

    public static Object alternativeIfNull(Object obj1, Object obj2) {
        if (obj1 != null) return obj1;
        else return obj2;
    }

}
