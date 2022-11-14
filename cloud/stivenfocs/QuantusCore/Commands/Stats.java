package cloud.stivenfocs.QuantusCore.Commands;

import cloud.stivenfocs.QuantusCore.GeneralEvents;
import cloud.stivenfocs.QuantusCore.PlayerData;
import cloud.stivenfocs.QuantusCore.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Stats implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Vars.hasUserPermission("stats", sender)) {
            if (sender instanceof Player) {
                if (args.length > 0) {
                    if (Vars.hasUserPermission("stats.others", sender)) {
                        PlayerData p_data = PlayerData.getPlayerDataFromName(args[0]);
                        if (p_data != null) {
                            List<String> player_stats = new ArrayList<>();
                            for (String text : Vars.stats_others) {
                                text = text.replace("%other_player_name%", args[0]);

                                if (p_data.hasPlacedFlag()) {
                                    text = text.replace("%other_player_flag_x%", String.valueOf(p_data.getFlagLocation().getBlockX()));
                                    text = text.replace("%other_player_flag_y%", String.valueOf(p_data.getFlagLocation().getBlockY()));
                                    text = text.replace("%other_player_flag_z%", String.valueOf(p_data.getFlagLocation().getBlockZ()));
                                    text = text.replace("%other_player_flag_world%", p_data.getFlagLocation().getWorld().getName());
                                } else {
                                    text = text.replace("%other_player_flag_x%", "0");
                                    text = text.replace("%other_player_flag_y%", "0");
                                    text = text.replace("%other_player_flag_z%", "0");
                                    text = text.replace("%other_player_flag_world%", Bukkit.getWorlds().get(0).getName());
                                }
                                text = text.replace("%other_player_kills%", String.valueOf(p_data.getKills()));
                                text = text.replace("%other_player_deaths%", String.valueOf(p_data.getDeaths()));
                                text = text.replace("%other_player_broken_flags%", String.valueOf(p_data.getBrokenFlags()));
                                text = text.replace("%other_player_level%", String.valueOf(p_data.getLevel()));
                                text = text.replace("%other_player_killstreak%", String.valueOf(Vars.alternativeIfNull(GeneralEvents.player_killstreaks.get(p_data.getPlayerUniqueId()), 0)));
                                text = text.replace("%other_player_multiplier%", String.valueOf(p_data.getMultiplier()));
                                text = text.replace("%other_player_levelup_cost%", String.valueOf(p_data.getLevelupCost()));
                                text = text.replace("%other_levelup_cost_format%", Vars.moneyFormat(String.valueOf(p_data.getLevelupCost())));
                                text = text.replace("%other_earned_amount%", String.valueOf(p_data.getEarnAmount()));
                                text = text.replace("%other_earned_amount_format%", Vars.moneyFormat(String.valueOf(p_data.getEarnAmount())));
                                text = text.replace("%other_next_earned_amount%", String.valueOf(p_data.getNextEarnAmount()));
                                text = text.replace("%other_next_earned_amount_format%", Vars.moneyFormat(String.valueOf(p_data.getNextEarnAmount())));

                                player_stats.add(text);
                            }

                            Vars.sendStringList(player_stats, sender);
                        } else Vars.sendString(Vars.player_not_found, sender);
                    }
                } else {
                    Player p = (Player) sender;
                    Vars.sendStringList(Vars.stats, p);
                }
            } else Vars.sendString(Vars.only_players, sender);
        } else Vars.sendString(Vars.no_permission, sender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> su = new ArrayList<>();

        if (args.length == 1) {
            if (args[0].equals("")) {
                if (Vars.hasUserPermission("stats.others", sender)) {
                    for (PlayerData p_data : PlayerData.getAllPlayersData()) {
                        su.add(p_data.getOfflinePlayer().getName());
                    }
                }
            } else {
                if (Vars.hasUserPermission("stats.others", sender)) {
                    for (PlayerData p_data : PlayerData.getAllPlayersData()) {
                        if (p_data.getOfflinePlayer().getName().toLowerCase().startsWith(args[0].toLowerCase())) su.add(p_data.getOfflinePlayer().getName());
                    }
                }
            }
        }

        return su;
    }
}