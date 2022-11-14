package cloud.stivenfocs.QuantusCore.Commands;

import cloud.stivenfocs.QuantusCore.PlayerData;
import cloud.stivenfocs.QuantusCore.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Spawn implements CommandExecutor, TabCompleter {

    public static HashMap<UUID, Integer> teleport_cooldown_taskId = new HashMap<>();
    public static HashMap<UUID, Integer> teleport_cooldown = new HashMap<>();
    public static HashMap<UUID, Location> teleport_location = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Vars.hasUserPermission("spawncommand", sender)) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (!PlayerData.getPlayerData(p.getUniqueId()).hasPlacedFlag()) {
                        sendToSpawn(p, true);
                    } else Vars.sendString(Vars.cant_go_to_spawn, sender);
                } else Vars.sendString(Vars.only_players, sender);
            } else {
                if (Vars.hasUserPermission("spawncommand.others", sender)) {
                    Player p = Bukkit.getPlayerExact(args[0]);
                    if (p != null) {
                        sendToSpawn(p, false);
                        Vars.sendString(Vars.teleporting_player.replace("%player_name%", p.getName()), sender);
                    } else Vars.sendString(Vars.player_not_found, sender);
                } else Vars.sendString(Vars.no_permission, sender);
            }
        } else Vars.sendString(Vars.no_permission, sender);
        return false;
    }

    public static void sendToSpawn(Player p, Boolean cooldown) {
        Location spawn;

        if (!Vars.spawn_location.equals("")) {
            spawn = Vars.stringToLocation(Vars.spawn_location);
        } else {
            Vars.sendString(Vars.no_spawn_set, p);
            return;
        }

        teleportPlayer(spawn, p, cooldown);
    }

    public static void teleportPlayer(Location spawn, Player p, Boolean cooldown) {
        teleport_location.put(p.getUniqueId(), p.getLocation());

        if (cooldown) {
            Vars.sendString(Vars.teleporting_you.replace("%remaining_seconds%", String.valueOf(Vars.player_respawn_cooldown)), p);
            Location finalSpawn = spawn;
            teleport_cooldown_taskId.put(p.getUniqueId(), Bukkit.getScheduler().runTaskTimer(Vars.plugin, () -> {
                if (!teleport_cooldown.containsKey(p.getUniqueId()))
                    teleport_cooldown.put(p.getUniqueId(), Vars.player_teleport_cooldown * 20);
                else teleport_cooldown.put(p.getUniqueId(), teleport_cooldown.get(p.getUniqueId()) - 1);

                if ((teleport_cooldown.get(p.getUniqueId()) % 20) == 0 && teleport_cooldown.get(p.getUniqueId()) > 0) {
                    HashMap<String, String> custom_replaces = Vars.generateCustomReplaces(p, "player");
                    custom_replaces.put("%remaining_seconds%", String.valueOf((teleport_cooldown.get(p.getUniqueId()) / 20)));
                    Vars.runActions(Vars.teleport_cooldown_event, custom_replaces, p);
                }

                if (teleport_cooldown.get(p.getUniqueId()) < 1) {
                    p.teleport(finalSpawn);

                    teleport_cooldown.remove(p.getUniqueId());
                    Bukkit.getScheduler().cancelTask(teleport_cooldown_taskId.get(p.getUniqueId()));
                    teleport_cooldown_taskId.remove(p.getUniqueId());
                    teleport_location.remove(p.getUniqueId());
                }
            }, 0L, 1L).getTaskId());
        } else {
            p.teleport(spawn);
            Vars.runActions(Vars.respawn_event, Vars.generateCustomReplaces(p, "player"), p);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> su = new ArrayList<>();

        if (args.length == 1) {
            if (args[0].equals("")) {
                if (Vars.hasUserPermission("spawncommand.others", sender)) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        su.add(p.getName());
                    }
                }
            } else {
                if (Vars.hasUserPermission("spawncommand.others", sender)) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) su.add(p.getName());
                    }
                }
            }
        }

        return su;
    }

}
