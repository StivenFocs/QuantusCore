package cloud.stivenfocs.QuantusCore;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

import java.util.*;

import static org.bukkit.Particle.BLOCK_CRACK;

public class PlayerData {

    public static PlayerData getPlayerDataFromName(String p_name) {
        for (PlayerData p_data : getAllPlayersData()) {
            if (p_data.getOfflinePlayer().getName().equals(p_name)) return p_data;
        }

        return null;
    }

    public static PlayerData getPlayerData(UUID pUUID) {
        return new PlayerData(pUUID);
    }

    final UUID pUUID;

    public PlayerData(UUID pUUID) {
        this.pUUID = pUUID;

        addDefault();
    }

    /////////////////////////////////////////

    public void addDefault() {
        if (Vars.getVars().dataConfig.getString(pUUID + ".name") == null) {
            Vars.getVars().dataConfig.set(pUUID + ".name", getOfflinePlayer().getName());
        }
    }

    /////////////////////////////////////////

    public UUID getPlayerUniqueId() {
        return pUUID;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getPlayerUniqueId());
    }

    public int getKills() {
        return Vars.getVars().dataConfig.getInt(pUUID + ".kills");
    }

    public int getDeaths() {
        return Vars.getVars().dataConfig.getInt(pUUID + ".deaths");
    }

    public int getBrokenFlags() {
        return Vars.getVars().dataConfig.getInt(pUUID + ".broken_flags");
    }

    public int getLevel() {
        if (Vars.getVars().dataConfig.getInt(pUUID + ".level", 1) < 1) setLevel(1);
        return Vars.getVars().dataConfig.getInt(pUUID + ".level", 1);
    }

    public Double getMultiplier() {
        return Vars.getVars().dataConfig.getDouble(pUUID + ".multiplier", 1.0);
    }

    public Double getEarnAmount() {
        String earn_amount = Vars.setPlaceholdersForEarn(Vars.earn_amount, getOfflinePlayer());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) earn_amount = PlaceholderAPI.setPlaceholders(getOfflinePlayer(), earn_amount);
        Double amount = new DoubleEvaluator().evaluate(earn_amount);
        String[] amount_ = String.valueOf(amount).split("\\.");
        String final_amount = amount_[0] + ".";
        char[] amount_chars = String.valueOf(amount_[1]).toCharArray();
        int count = 2;
        for (char c : amount_chars) {
            if (count > 0) {
                final_amount = final_amount + c;
                count--;
            }
        }
        return Double.parseDouble(final_amount);
    }
    public Double getNextEarnAmount() {
        String earn_amount = Vars.setPlaceholdersForEarn(Vars.earn_amount.replace("%player_level%", String.valueOf(getLevel() + 1)).replace("%quantuscore_player_level%", String.valueOf(getLevel() + 1)), getOfflinePlayer());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) earn_amount = PlaceholderAPI.setPlaceholders(getOfflinePlayer(), earn_amount);
        Double amount = new DoubleEvaluator().evaluate(earn_amount);
        String[] amount_ = String.valueOf(amount).split("\\.");
        String final_amount = amount_[0] + ".";
        char[] amount_chars = String.valueOf(amount_[1]).toCharArray();
        int count = 2;
        for (char c : amount_chars) {
            if (count > 0) {
                final_amount = final_amount + c;
                count--;
            }
        }
        return Double.parseDouble(final_amount);
    }

    public Double getLevelupCost() {
        String levelup_cost = Vars.setPlaceholdersForLevelupCost(Vars.levelup_cost, getOfflinePlayer());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) levelup_cost = PlaceholderAPI.setPlaceholders(getOfflinePlayer(), levelup_cost);
        Double amount = new DoubleEvaluator().evaluate(levelup_cost);
        String[] amount_ = String.valueOf(amount).split("\\.");
        String final_amount = amount_[0] + ".";
        char[] amount_chars = String.valueOf(amount_[1]).toCharArray();
        int count = 2;
        for (char c : amount_chars) {
            if (count > 0) {
                final_amount = final_amount + c;
                count--;
            }
        }
        return Double.parseDouble(final_amount);
    }

    public boolean hasPlacedFlag() {
        if (Vars.getVars().dataConfig.getString(pUUID + ".flag_location") != null) {
            if (getFlagBlock() == null || getFlagBlock().getType().equals(Material.AIR)) {
                unsetFlagLocation();
                return false;
            } else return true;
        }

        return false;
    }

    public Location getFlagLocation() {
        String flag_location_string = Vars.getVars().dataConfig.getString(pUUID + ".flag_location");
        if (flag_location_string != null) {
            return Vars.stringToLocation(flag_location_string);
        } else return null;
    }

    public Location getRespawnLocation() {
        if (hasPlacedFlag()) {
            Location flag_location = getFlagLocation();
            flag_location.setX(flag_location.getBlockX() + 0.5);
            flag_location.setZ(flag_location.getBlockZ() + 0.5);
            if (flag_location.getWorld().getBlockAt(flag_location).getType().isOccluding()) flag_location.setY(flag_location.getBlockY() + 1);
            return flag_location;
        }

        return null;
    }

    public Block getFlagBlock() {
        return getFlagLocation().getWorld().getBlockAt(getFlagLocation());
    }

    /////////////////////////////////////////

    public void setKills(Integer new_kills) {
        addDefault();

        if (new_kills < 0) new_kills = 0;
        Vars.getVars().dataConfig.set(pUUID + ".kills", new_kills);
    }

    public void setDeaths(Integer new_deaths) {
        addDefault();

        if (new_deaths < 0) new_deaths = 0;
        Vars.getVars().dataConfig.set(pUUID + ".deaths", new_deaths);
    }

    public void setBrokenFlags(Integer new_broken_flags) {
        addDefault();

        if (new_broken_flags < 0) new_broken_flags = 0;
        Vars.getVars().dataConfig.set(pUUID + ".broken_flags", new_broken_flags);
    }

    public void setLevel(Integer new_level) {
        addDefault();

        if (new_level < 1) new_level = 1;
        Vars.getVars().dataConfig.set(pUUID + ".level", new_level);

        if (Vars.use_expbar_to_display_level) {
            if (getOfflinePlayer().isOnline()) {
                getOfflinePlayer().getPlayer().setExp(0F);
                getOfflinePlayer().getPlayer().setLevel(new_level);
            }
        }
    }

    public void setMultiplier(Double new_multiplier) {
        addDefault();

        if (new_multiplier < 1.0) new_multiplier = 1.0;
        Vars.getVars().dataConfig.set(pUUID + ".multiplier", new_multiplier);
    }

    public void unsetFlagLocation() {
        addDefault();

        Vars.getVars().dataConfig.set(pUUID + ".flag_location", null);
    }

    public void setFlagLocation(Location new_flag_location) {
        addDefault();

        Vars.getVars().dataConfig.set(pUUID + ".flag_location", Vars.locationBlockToString(new_flag_location, true));
    }

    public void breakFlag() {
        if (hasPlacedFlag()) {
            Block flag_block = getFlagBlock();
            getFlagLocation().getWorld().spawnParticle(BLOCK_CRACK, getFlagLocation(), 1, flag_block.getBlockData());
            try {
                getFlagLocation().getWorld().playEffect(getFlagLocation(), Effect.STEP_SOUND, getFlagBlock().getType());
            } catch (Exception ex) {
                getFlagLocation().getWorld().playEffect(getFlagLocation(), Effect.STEP_SOUND, 5);
            }
            if (!flag_block.getType().equals(Material.AIR)) flag_block.setType(Material.AIR);
            unsetFlagLocation();
            FlagMenu.deletePlayerFlagMenu(getOfflinePlayer());
            if (GeneralEvents.player_breaking_his_flag_taskId.containsKey(pUUID)) {
                try {
                    for (ArmorStand armorstand : GeneralEvents.player_breaking_flag_armorstands.get(pUUID)) {
                        try {
                            armorstand.remove();
                        } catch (Exception ignored) {
                        }
                    }
                } catch (Exception ex_) {
                    ex_.printStackTrace();
                }

                GeneralEvents.player_breaking_flag_armorstands.remove(pUUID);
                GeneralEvents.player_breaking_his_flag.remove(pUUID);
                if (GeneralEvents.player_breaking_his_flag_taskId.containsKey(pUUID))
                    Bukkit.getScheduler().cancelTask(GeneralEvents.player_breaking_his_flag_taskId.get(pUUID));
                GeneralEvents.player_breaking_his_flag_taskId.remove(pUUID);
            }
            if (getOfflinePlayer().isOnline()) {
                GeneralEvents.giveNewFlagItem(getOfflinePlayer().getPlayer());
            }

        }
    }

    public boolean isHisFlag(Location loc) {
        loc.setX(loc.getBlockX());
        loc.setY(loc.getBlockY());
        loc.setZ(loc.getBlockZ());

        return hasPlacedFlag() && loc.equals(getFlagLocation());
    }

    /////////////////////////////////////////

    public static boolean isAPlayerFlag(Location flag_location) {
        return getFlagOwner(flag_location) != null;
    }

    public static PlayerData getFlagOwner(Location flag_location) {
        flag_location.setX(flag_location.getBlockX());
        flag_location.setY(flag_location.getBlockY());
        flag_location.setZ(flag_location.getBlockZ());

        for (String pUUID_string : Vars.getVars().dataConfig.getKeys(false)) {
            PlayerData p_data = PlayerData.getPlayerData(UUID.fromString(pUUID_string));

            if (p_data.hasPlacedFlag()) {
                if (p_data.getFlagLocation().equals(flag_location)) return p_data;
            }
        }

        return null;
    }

    public static List<PlayerData> getAllPlayersData() {
        List<PlayerData> all_players_data = new ArrayList<>();
        for (String pUUID_string : Vars.getVars().dataConfig.getKeys(false)) {
            UUID pUUID = UUID.fromString(pUUID_string);
            all_players_data.add(getPlayerData(pUUID));
        }

        return all_players_data;
    }

    static HashMap<String, Integer> levels_top;

    public static HashMap<String, Integer> getTopLevels() {
        if (levels_top == null || levels_top.size() < 1) levels_top = refreshTopLevels();
        return levels_top;
    }

    public static HashMap<String, Integer> refreshTopLevels() {
        /*HashMap<String, Integer> top_levels = new HashMap<>();
        for (PlayerData p_data : getAllPlayersData()) {
            top_levels.put(p_data.getOfflinePlayer().getName(), p_data.getLevel());
        }

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>(top_levels);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list);
        Collections.reverse(list);
        for (int num : list) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedMap.put(entry.getKey(), num);
                }
            }
        }

        levels_top = sortedMap;
        return sortedMap;*/

        HashMap<String, Integer> top_levels = new HashMap<>();
        for (PlayerData p_data : getAllPlayersData()) {
            top_levels.put(p_data.getOfflinePlayer().getName(), p_data.getLevel());
        }

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : top_levels.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list);
        Collections.reverse(list);
        for (int num : list) {
            for (Map.Entry<String, Integer> entry : top_levels.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedMap.put(entry.getKey(), num);
                }
            }
        }

        levels_top = sortedMap;
        return sortedMap;
    }

}
