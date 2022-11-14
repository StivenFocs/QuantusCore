package cloud.stivenfocs.QuantusCore.Commands;

import cloud.stivenfocs.QuantusCore.PlayerData;
import cloud.stivenfocs.QuantusCore.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Quantus implements CommandExecutor, TabCompleter {

    public static HashMap<UUID, Location> pos1 = new HashMap<>();
    public static HashMap<UUID, Location> pos2 = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (Vars.hasAdminPermission("help", sender)) {
                Vars.sendStringList(Vars.help_admin, sender);
            } else {
                Vars.sendStringList(Vars.help_user, sender);
            }
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (Vars.hasAdminPermission("reload", sender)) {
                    if (sender instanceof ConsoleCommandSender) {
                        Vars.getVars().saveDataConfig();
                        Vars.getVars().reloadVars();
                    } else {
                        if (Vars.getVars().reloadVars()) Vars.sendString(Vars.configuration_reloaded, sender);
                        else Vars.sendString(Vars.an_error_occurred, sender);
                    }
                } else {
                    Vars.sendString(Vars.no_permission, sender);
                }
            } else if (args[0].equalsIgnoreCase("setspawn")) {
                if (Vars.hasAdminPermission("setspawn", sender)) {
                    if (sender instanceof Player) {
                        Vars.getVars().getConfig().set("options.spawn_location", Vars.locationToString(((Player) sender).getLocation()));
                        Vars.plugin.saveConfig();
                        Vars.plugin.reloadConfig();
                        Vars.getVars().reloadVars();
                        Vars.sendString(Vars.spawn_location_set, sender);
                    } else Vars.sendString(Vars.only_players, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("setspectatorspawn")) {
                if (Vars.hasAdminPermission("setspectatorspawn", sender)) {
                    if (sender instanceof Player) {
                        Vars.getVars().getConfig().set("options.respawn_location", Vars.locationToString(((Player) sender).getLocation()));
                        Vars.plugin.saveConfig();
                        Vars.plugin.reloadConfig();
                        Vars.getVars().reloadVars();
                        Vars.sendString(Vars.respawn_location_set, sender);
                    } else Vars.sendString(Vars.only_players, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("setplayerstat")) {
                if (Vars.hasAdminPermission("setplayerstat", sender)) {
                    if (args.length > 1) {
                        PlayerData p_data = PlayerData.getPlayerDataFromName(args[1]);
                        if (p_data != null) {

                            if (args.length > 2) {
                                enum Statistics { KILLS, DEATHS, BROKEN_FLAGS, LEVEL, MULTIPLIER }
                                Statistics statistic;
                                try {
                                    statistic = Statistics.valueOf(args[2].toUpperCase());
                                } catch (Exception ignored) {
                                    Vars.sendString(Vars.unknown_statistic.replace("%statistic%", args[2]), sender);
                                    return true;
                                }

                                if (args.length > 3) {
                                    enum Action { RESET ,SET ,ADD ,REMOVE }
                                    Action action;
                                    try {
                                        action = Action.valueOf(args[3].toUpperCase());
                                    } catch (Exception ignored) {
                                        Vars.sendString(Vars.unknown_action.replace("%action%", args[3]), sender);
                                        return true;
                                    }

                                    if (action.equals(Action.RESET)) {
                                        if (statistic.equals(Statistics.KILLS)) p_data.setKills(0);
                                        if (statistic.equals(Statistics.DEATHS)) p_data.setDeaths(0);
                                        if (statistic.equals(Statistics.BROKEN_FLAGS)) p_data.setBrokenFlags(0);
                                        if (statistic.equals(Statistics.LEVEL)) p_data.setLevel(1);
                                        Vars.sendString(Vars.statistic_reset.replace("%player_name%", args[1]), sender);
                                    } else {
                                        if (args.length > 4) {
                                            if (Vars.isDigit(args[4])) {
                                                Integer amount = Integer.parseInt(args[4]);

                                                if (action.equals(Action.SET)) {
                                                    if (statistic.equals(Statistics.KILLS)) p_data.setKills(amount);
                                                    if (statistic.equals(Statistics.DEATHS)) p_data.setDeaths(amount);
                                                    if (statistic.equals(Statistics.BROKEN_FLAGS)) p_data.setBrokenFlags(amount);
                                                    if (statistic.equals(Statistics.LEVEL)) p_data.setLevel(amount);
                                                } else if (action.equals(Action.ADD)) {
                                                    if (statistic.equals(Statistics.KILLS)) p_data.setKills(p_data.getKills() + amount);
                                                    if (statistic.equals(Statistics.DEATHS)) p_data.setDeaths(p_data.getDeaths() + amount);
                                                    if (statistic.equals(Statistics.BROKEN_FLAGS)) p_data.setBrokenFlags(p_data.getBrokenFlags() + amount);
                                                    if (statistic.equals(Statistics.LEVEL)) p_data.setLevel(p_data.getLevel() + amount);
                                                } else if (action.equals(Action.REMOVE)) {
                                                    if (statistic.equals(Statistics.KILLS)) p_data.setKills(p_data.getKills() - amount);
                                                    if (statistic.equals(Statistics.DEATHS)) p_data.setDeaths(p_data.getDeaths() - amount);
                                                    if (statistic.equals(Statistics.BROKEN_FLAGS)) p_data.setBrokenFlags(p_data.getBrokenFlags() - amount);
                                                    if (statistic.equals(Statistics.LEVEL)) p_data.setLevel(p_data.getLevel() - amount);
                                                }
                                                Vars.sendString(Vars.statistic_set.replace("%player_name%", args[1]).replace("%amount%", String.valueOf(amount)), sender);
                                            } else Vars.sendString(Vars.an_integer_needed, sender);
                                        } else Vars.sendString(Vars.an_integer_needed, sender);
                                    }
                                } else Vars.sendString(Vars.incomplete_command, sender);
                            } else Vars.sendString(Vars.incomplete_command, sender);
                        } else Vars.sendString(Vars.player_not_found, sender);
                    } else Vars.sendString(Vars.incomplete_command, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("multiplier")) {
                if (Vars.hasAdminPermission("multiplier", sender)) {
                    if (args.length > 1) {
                        Player p = Bukkit.getPlayerExact(args[1]);
                        PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());
                        if (p != null) {
                            if (args.length > 2) {
                                if (args.length > 3) {
                                    if (Vars.isDoubleDigit(args[3])) {
                                        Double amount = Double.parseDouble(args[3]);

                                        if (args[2].equalsIgnoreCase("set")) {
                                            p_data.setMultiplier(amount);
                                        }

                                        Vars.sendString(p.getName() + "'s multiplier has been set to " + amount, sender);
                                    } else Vars.sendString("double value needed", sender);
                                } else Vars.sendString(Vars.incomplete_command, sender);
                            } else Vars.sendString(Vars.incomplete_command, sender);
                        } else Vars.sendString(Vars.player_not_found, sender);
                    } else Vars.sendString(Vars.incomplete_command, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("addflagitem")) {
                if (Vars.hasAdminPermission("addflagitem", sender)) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (!p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                            if (p.getInventory().getItemInMainHand().getType().isBlock()) {
                                List<ItemStack> flag_items = (List<ItemStack>) Vars.getVars().getConfig().getList("options.flag_items");
                                flag_items.add(p.getInventory().getItemInMainHand());
                                Vars.getVars().getConfig().set("options.flag_items", flag_items);
                                Vars.plugin.saveConfig();
                                Vars.plugin.reloadConfig();
                                Vars.getVars().reloadVars();
                                Vars.sendString(Vars.flag_item_added, sender);
                            } else Vars.sendString(Vars.only_placeable_blocks, sender);
                        } else Vars.sendString(Vars.hold_an_item, sender);
                    } else Vars.sendString(Vars.only_players, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("gotoflag")) {
                if (Vars.hasAdminPermission("gotoflag", sender)) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (args.length > 1) {
                            OfflinePlayer offlineplayer = Bukkit.getOfflinePlayer(args[1]);
                            if (offlineplayer != null) {
                                PlayerData offlineplayer_data = PlayerData.getPlayerData(offlineplayer.getUniqueId());

                                if (offlineplayer_data.hasPlacedFlag()) {
                                    Location flag_location = offlineplayer_data.getFlagLocation();
                                    flag_location.setX(flag_location.getBlockX() + .5);
                                    flag_location.setZ(flag_location.getBlockZ() + .5);
                                    p.teleport(flag_location);
                                } else Vars.sendString(Vars.flag_not_placed, sender);
                            } else Vars.sendString(Vars.player_not_found, sender);
                        } else Vars.sendString(Vars.incomplete_command, sender);
                    } else Vars.sendString(Vars.only_players, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("breakflag")) {
                if (Vars.hasAdminPermission("breakflag", sender)) {
                    if (args.length > 1) {
                        PlayerData offlineplayer_data = PlayerData.getPlayerDataFromName(args[1]);
                        if (offlineplayer_data != null) {
                            if (offlineplayer_data.hasPlacedFlag()) {
                                offlineplayer_data.breakFlag();
                                Vars.sendString(Vars.flag_broken.replace("%player%", args[1]), sender);
                            } else Vars.sendString(Vars.flag_not_placed, sender);
                        } else Vars.sendString(Vars.player_not_found, sender);
                    } else Vars.sendString(Vars.incomplete_command, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("pos1")) {
                if (Vars.hasAdminPermission("pos", sender)) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        pos1.put(p.getUniqueId(), p.getLocation());
                        Vars.sendString(Vars.pos1_set.replace("%x%", String.valueOf(p.getLocation().getBlockX())).replace("%y%", String.valueOf(p.getLocation().getBlockY())).replace("%z%", String.valueOf(p.getLocation().getBlockZ())).replace("%world%", p.getLocation().getWorld().getName()), sender);
                    } else Vars.sendString(Vars.only_players, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("pos2")) {
                if (Vars.hasAdminPermission("pos", sender)) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        pos2.put(p.getUniqueId(), p.getLocation());
                        Vars.sendString(Vars.pos2_set.replace("%x%", String.valueOf(p.getLocation().getBlockX())).replace("%y%", String.valueOf(p.getLocation().getBlockY())).replace("%z%", String.valueOf(p.getLocation().getBlockZ())).replace("%world%", p.getLocation().getWorld().getName()), sender);
                    } else Vars.sendString(Vars.only_players, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("addnoearnregion")) {
                if (Vars.hasAdminPermission("addnoearnregion", sender)) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (pos1.containsKey(p.getUniqueId()) && pos2.containsKey(p.getUniqueId()) && pos1.get(p.getUniqueId()).getWorld().getName().equals(pos2.get(p.getUniqueId()).getWorld().getName())) {
                            List<String> no_earn_regions = Vars.no_earn_regions;
                            no_earn_regions.add(Vars.locationBlockToString(pos1.get(p.getUniqueId()), true) + "|" + Vars.locationBlockToString(pos2.get(p.getUniqueId()), true));
                            Vars.plugin.getConfig().set("options.no_earn_regions", no_earn_regions);
                            Vars.plugin.saveConfig();
                            Vars.getVars().reloadVars();
                            Vars.sendString(Vars.added_no_earn_region, sender);
                        } else Vars.sendString(Vars.invalid_selection, sender);
                    } else Vars.sendString(Vars.only_players, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            } else if (args[0].equalsIgnoreCase("debug")) {
                if (Vars.hasAdminPermission("debug", sender)) {
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("togglemaintask")) {
                            if (Vars.plugin.mainTaskId == null) {
                                Vars.plugin.runMainTask();
                            } else {
                                Bukkit.getScheduler().cancelTask(Vars.plugin.mainTaskId);
                                Vars.plugin.mainTaskId = null;
                            }

                            sender.sendMessage(String.valueOf(Vars.plugin.mainTaskId));
                        }
                    }
                } else Vars.sendString(Vars.unknown_subcommand, sender);
            } else {
                Vars.sendString(Vars.unknown_subcommand, sender);
            }

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> su = new ArrayList<>();

        if (args.length == 1) {
            if (args[0].equals("")) {
                if (Vars.hasAdminPermission("reload", sender)) {
                    su.add("reload");
                }
                if (Vars.hasAdminPermission("setspawn", sender)) {
                    su.add("setspawn");
                }
                if (Vars.hasAdminPermission("setspectatorspawn", sender)) {
                    su.add("setspectatorspawn");
                }
                if (Vars.hasAdminPermission("setplayerstat", sender)) {
                    su.add("setplayerstat");
                }
                if (Vars.hasAdminPermission("addflagitem", sender)) {
                    su.add("addflagitem");
                }
                if (Vars.hasAdminPermission("gotoflag", sender)) {
                    su.add("gotoflag");
                }
                if (Vars.hasAdminPermission("breakflag", sender)) {
                    su.add("breakflag");
                }
                if (Vars.hasAdminPermission("pos", sender)) {
                    su.add("pos1");
                    su.add("pos2");
                }
                if (Vars.hasAdminPermission("addnoearnregion", sender)) {
                    su.add("addnoearnregion");
                }
            } else {
                if ("reload".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("reload", sender)) {
                        su.add("reload");
                    }
                }
                if ("setspawn".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("setspawn", sender)) {
                        su.add("setspawn");
                    }
                }
                if ("setspectatorspawn".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("setspectatorspawn", sender)) {
                        su.add("setspectatorspawn");
                    }
                }
                if ("setplayerstat".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("setplayerstat", sender)) {
                        su.add("setplayerstat");
                    }
                }
                if ("addflagitem".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("addflagitem", sender)) {
                        su.add("addflagitem");
                    }
                }
                if ("gotoflag".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("gotoflag", sender)) {
                        su.add("gotoflag");
                    }
                }
                if ("breakflag".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("breakflag", sender)) {
                        su.add("breakflag");
                    }
                }
                if ("pos1".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("pos", sender)) {
                        su.add("pos1");
                    }
                }
                if ("pos2".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("pos", sender)) {
                        su.add("pos2");
                    }
                }
                if ("addnoearnregion".startsWith(args[0].toLowerCase())) {
                    if (Vars.hasAdminPermission("addnoearnregion", sender)) {
                        su.add("addnoearnregion");
                    }
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setplayerstat") || args[0].equalsIgnoreCase("gotoflag") || args[0].equalsIgnoreCase("breakflag")) {
                if (Vars.hasAdminPermission(args[0], sender)) {
                    if (args[1].equals("")) {
                        for (PlayerData p_data : PlayerData.getAllPlayersData()) {
                            su.add(p_data.getOfflinePlayer().getName());
                        }
                    } else {
                        for (PlayerData p_data : PlayerData.getAllPlayersData()) {
                            if (p_data.getOfflinePlayer().getName().toLowerCase().startsWith(args[1].toLowerCase())) su.add(p_data.getOfflinePlayer().getName());
                        }
                    }
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setplayerstat")) {
                if (Vars.hasAdminPermission("setplayerstat", sender)) {
                    if (args[2].equals("")) {
                        su.add("kills");
                        su.add("deaths");
                        su.add("broken_flags");
                        su.add("level");
                    } else {
                        if ("kills".startsWith(args[2].toLowerCase())) su.add("kills");
                        if ("deaths".startsWith(args[2].toLowerCase())) su.add("deaths");
                        if ("broken_flags".startsWith(args[2].toLowerCase())) su.add("broken_flags");
                        if ("level".startsWith(args[2].toLowerCase())) su.add("level");
                    }
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("setplayerstat")) {
                if (Vars.hasAdminPermission("setplayerstat", sender)) {
                    if (args[3].equals("")) {
                        su.add("reset");
                        su.add("set");
                        su.add("add");
                        su.add("remove");
                    } else {
                        if ("reset".startsWith(args[3].toLowerCase())) su.add("reset");
                        if ("set".startsWith(args[3].toLowerCase())) su.add("set");
                        if ("add".startsWith(args[3].toLowerCase())) su.add("add");
                        if ("remove".startsWith(args[3].toLowerCase())) su.add("remove");
                    }
                }
            }
        }

        return su;
    }
}
