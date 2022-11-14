/*
CIAO PROGRAMMATORE CHE NON SONO IO!
Hai presente quando qualcuno pensa: "questo standard non mi piace, FANCULO TUTTI, ora me ne faccio uno mio"..
ecco appunto!

packages: lowercase
classi: PascalCase
metodi: pascalCase
variabili: snake_lower_case
costanti? umh.. 🤷🏻‍♂️
*/

package cloud.stivenfocs.QuantusCore;

import cloud.stivenfocs.QuantusCore.Commands.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Loader extends JavaPlugin {

    public static PAPIHook papihook = null;
    public Integer mainTaskId = null;

    public void onEnable() {
        Vars.plugin = this;

        Vars vars = Vars.getVars();
        vars.reloadVars();

        getCommand("spawn").setExecutor(new Spawn());
        getCommand("spawn").setTabCompleter(new Spawn());
        getCommand("top").setExecutor(new Top());
        getCommand("stats").setExecutor(new Stats());
        getCommand("stats").setTabCompleter(new Stats());
        getCommand("quantus").setExecutor(new Quantus());
        getCommand("quantus").setTabCompleter(new Quantus());

        Bukkit.getPluginManager().registerEvents(new GeneralEvents(), this);

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            getLogger().info("Vault found and initialized, please make sure to install an economy plugin also.");
            setupEconomy();
        } else {
            getLogger().info("No vault found, all economy utilities has been disabled.");
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) papihook = new PAPIHook();
        if (papihook != null) papihook.register();
    }

    public static Integer data_save_delay = null;
    public static Integer top_refresh_delay = null;

    public void runMainTask() {
        if (mainTaskId != null)  Bukkit.getScheduler().cancelTask(mainTaskId);

        mainTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (data_save_delay == null || data_save_delay < 1) {
                data_save_delay = Vars.data_save_delay;
                Vars.getVars().saveDataConfig();
            }
            data_save_delay--;
            if (top_refresh_delay == null || top_refresh_delay < 1) {
                top_refresh_delay = Vars.top_refresh_delay;
                PlayerData.refreshTopLevels();
            }
            top_refresh_delay--;

            for (Player p : Bukkit.getOnlinePlayers()) {
                PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());
                if (GeneralEvents.afk_time.containsKey(p.getUniqueId())) {
                    if (GeneralEvents.afk_time.get(p.getUniqueId()).time >= Vars.afk_time) {
                        Vars.runActions(Vars.afk_event, Vars.generateCustomReplaces(p, "player"), p);
                        continue;
                    } else {
                        GeneralEvents.afk_time.get(p.getUniqueId()).time = GeneralEvents.afk_time.get(p.getUniqueId()).time + 1;
                    }

                    if (p_data.hasPlacedFlag()) {
                        if (!isInNoEarnRegion(p.getLocation())) {
                            Vars.runActions(Vars.earn_event, Vars.generateCustomReplaces(p, "player"), p);
                        }
                    }
                }
            }
        }, 0L, 20L * Vars.per_earn_seconds.longValue()).getTaskId();
    }

    public void onDisable() {
        Vars.getVars().saveDataConfig();

        Bukkit.getScheduler().cancelTask(mainTaskId);
        if (papihook != null) papihook.unregister();

        for (Inventory gui : new ArrayList<>(FlagMenu.flags_gui.values())) {
            for (HumanEntity viewer : new ArrayList<>(gui.getViewers())) {
                viewer.closeInventory();
            }
        }

        for (UUID pUUID : GeneralEvents.player_breaking_his_flag.keySet()) {
            PlayerData p_data = PlayerData.getPlayerData(pUUID);
            p_data.breakFlag();
        }

        for (List<ArmorStand> armorstands : GeneralEvents.player_breaking_flag_armorstands.values()) {
            for (ArmorStand armorstand : armorstands) {
                try {
                    armorstand.remove();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            Vars.econ = rsp.getProvider();
        }
    }

    public static boolean isInNoEarnRegion(Location loc) {
        try {
            List<NoEarnRegion> no_earn_regions = new ArrayList<>();
            for (String raw_no_earn_region : Vars.no_earn_regions) {
                try {
                    String[] locations = raw_no_earn_region.split("\\|");
                    no_earn_regions.add(new NoEarnRegion(Vars.stringToLocation(locations[0]), Vars.stringToLocation(locations[1])));
                } catch (Exception ignored) {
                    Vars.plugin.getLogger().warning("Unable to load the no earn region: " + raw_no_earn_region);
                }
            }


            for (NoEarnRegion no_earn_region : no_earn_regions) {
                if (no_earn_region.isInRegion(loc)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            Vars.plugin.getLogger().severe(ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }

}

class NoEarnRegion {

    public final Location pos1;
    public final Location pos2;

    public NoEarnRegion(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public boolean isInRegion(Location location) {
        if (location.getWorld().getName().equals(pos1.getWorld().getName())) {
            if (location.getBlockX() >= Math.min(pos1.getBlockX(), pos2.getBlockX()) && location.getBlockX() <= Math.max(pos1.getBlockX(), pos2.getBlockX())) {
                if (location.getBlockY() >= Math.min(pos1.getBlockY(), pos2.getBlockY()) && location.getBlockY() <= Math.max(pos1.getBlockY(), pos2.getBlockY())) {
                    if (location.getBlockZ() >= Math.min(pos1.getBlockZ(), pos2.getBlockZ()) && location.getBlockZ() <= Math.max(pos1.getBlockZ(), pos2.getBlockZ())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}