package cloud.stivenfocs.QuantusCore;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PAPIHook extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return Vars.plugin.getName();
    }

    @Override
    public String getAuthor() {
        return Vars.plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return Vars.plugin.getDescription().getVersion();
    }

    @Override
    public String getName() {
        return Vars.plugin.getName();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

            if (params.toLowerCase().startsWith("player_balance")) {
                String player_money = PlaceholderAPI.setPlaceholders(p, "%vault_eco_balance%");
                String[] amount_ = player_money.split("\\.");
                String final_amount = amount_[0] + ".";
                char[] amount_chars = String.valueOf(amount_[1]).toCharArray();
                int count = 2;
                for (char c : amount_chars) {
                    if (count > 0) {
                        final_amount = final_amount + c;
                        count--;
                    }
                }
                return Vars.moneyFormat(final_amount);
            }

            if (params.toLowerCase().startsWith("top_level")) {
                try {
                    HashMap<String, Integer> top_levels = PlayerData.getTopLevels();
                    String[] params_ = params.split("_");
                    if (params_.length > 3) {
                        if (Vars.isDigit(params_[2])) {
                            Integer index = Integer.parseInt(params_[2]);

                            if (index <= top_levels.keySet().size()) {
                                if (params_[3].equalsIgnoreCase("name"))
                                    return new ArrayList<>(top_levels.keySet()).get(index - 1);
                                if (params_[3].equalsIgnoreCase("amount"))
                                    return String.valueOf(new ArrayList<>(top_levels.values()).get(index - 1));
                            } else {
                                if (params_[3].equalsIgnoreCase("name")) return Vars.nobody;
                                if (params_[3].equalsIgnoreCase("amount")) return "0";
                            }
                        }
                    }
                } catch (Exception ex) {
                    Vars.plugin.getLogger().severe(ex.getMessage());
                    ex.printStackTrace();
                }
            }

            if (params.equalsIgnoreCase("player_banner_is_placed")) {
                if (p_data.hasPlacedFlag()) return Vars.setOfflinePlayerPlaceholders(Vars.banner_placed_placeholder, p);
                else return Vars.setOfflinePlayerPlaceholders(Vars.banner_not_placed_placeholder, p);
            }

            if (p.isOnline()) {
                Player p_ = p.getPlayer();
                if (params.equalsIgnoreCase("player_now_earning")) {
                    if (!Loader.isInNoEarnRegion(p_.getLocation())) {
                        if (GeneralEvents.afk_time.containsKey(p_.getUniqueId())) {
                            if (p_data.hasPlacedFlag() && (GeneralEvents.afk_time.get(p_.getUniqueId()).time < Vars.afk_time))
                                return String.valueOf(p_data.getEarnAmount());
                        }
                    }
                    return "0.0";
                }
            }

            if (GeneralEvents.player_breaking_his_flag.containsKey(p.getUniqueId())) {
                if (params.equalsIgnoreCase("flag_breaking_countdown")) return String.valueOf(GeneralEvents.player_breaking_his_flag.get(p.getUniqueId()));
            }

            if (p_data.hasPlacedFlag()) {
                if (params.equalsIgnoreCase("flag_x")) return String.valueOf(p_data.getFlagLocation().getBlockX());
                if (params.equalsIgnoreCase("flag_y")) return String.valueOf(p_data.getFlagLocation().getBlockY());
                if (params.equalsIgnoreCase("flag_z")) return String.valueOf(p_data.getFlagLocation().getBlockZ());
                if (params.equalsIgnoreCase("flag_world")) return p_data.getFlagLocation().getWorld().getName();
            } else {
                if (params.equalsIgnoreCase("flag_x")) return "0";
                if (params.equalsIgnoreCase("flag_y")) return "0";
                if (params.equalsIgnoreCase("flag_z")) return "0";
                if (params.equalsIgnoreCase("flag_world")) return Bukkit.getWorlds().get(0).getName();
            }
            if (params.equalsIgnoreCase("player_kills")) return String.valueOf(p_data.getKills());
            if (params.equalsIgnoreCase("player_deaths")) return String.valueOf(p_data.getDeaths());
            if (params.equalsIgnoreCase("player_broken_flags")) return String.valueOf(p_data.getBrokenFlags());
            if (params.equalsIgnoreCase("player_next_level")) return String.valueOf(p_data.getLevel() + 1);
            if (params.equalsIgnoreCase("player_level")) return String.valueOf(p_data.getLevel());
            if (params.equalsIgnoreCase("player_multiplier")) return String.valueOf(p_data.getMultiplier());
            if (params.equalsIgnoreCase("player_killstreak")) return String.valueOf(Vars.alternativeIfNull(GeneralEvents.player_killstreaks.get(p_data.getPlayerUniqueId()), 0));
            if (params.equalsIgnoreCase("player_levelup_cost")) return String.valueOf(p_data.getLevelupCost());
            if (params.equalsIgnoreCase("player_earned_amount")) return String.valueOf(p_data.getEarnAmount());
            if (params.equalsIgnoreCase("player_next_earned_amount")) return String.valueOf(p_data.getNextEarnAmount());
            if (params.equalsIgnoreCase("player_levelup_cost_format")) return Vars.moneyFormat(String.valueOf(p_data.getLevelupCost()));
            if (params.equalsIgnoreCase("player_earned_amount_format")) return Vars.moneyFormat(String.valueOf(p_data.getEarnAmount()));
            if (params.equalsIgnoreCase("player_next_earned_amount_format")) return Vars.moneyFormat(String.valueOf(p_data.getNextEarnAmount()));

        return null;
    }
}
