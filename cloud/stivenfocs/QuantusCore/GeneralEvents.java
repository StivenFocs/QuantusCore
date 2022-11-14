package cloud.stivenfocs.QuantusCore;

import cloud.stivenfocs.QuantusCore.Commands.Spawn;
import cloud.stivenfocs.QuantusCore.Events.PlayerLevelupEvent;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import me.NoChance.PvPManager.PvPManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class GeneralEvents implements Listener {

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (Vars.disabled_worlds.contains(event.getPlayer().getLocation().getWorld().getName())) return;

        if (isFlagItem(event.getItemDrop().getItemStack())) event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (Vars.disabled_worlds.contains(event.getEntity().getLocation().getWorld().getName())) return;

        if (event.getEntity().getType().equals(EntityType.PLAYER)) {
            if (isFlagItem(event.getItem().getItemStack())) event.setCancelled(true);
        }
    }

    public static HashMap<UUID, Integer> hookfeather_cooldown = new HashMap<>();
    public static List<UUID> hookfeather_fall_damage = new ArrayList<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        try {
            if (Vars.disabled_worlds.contains(event.getPlayer().getLocation().getWorld().getName())) return;

            Player p = event.getPlayer();
            PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

                if (event.getItem() != null && event.getItem().getType().isBlock()) {
                    Location water_block_location = event.getClickedBlock().getLocation();
                    if (event.getBlockFace().equals(BlockFace.UP)) {
                        water_block_location.setY(water_block_location.getBlockY() + 1);
                    }
                    if (event.getBlockFace().equals(BlockFace.DOWN)) {
                        water_block_location.setY(water_block_location.getBlockY() - 1);
                    }
                    if (event.getBlockFace().equals(BlockFace.EAST)) {
                        water_block_location.setX(water_block_location.getBlockX() + 1);
                    }
                    if (event.getBlockFace().equals(BlockFace.WEST)) {
                        water_block_location.setX(water_block_location.getBlockX() - 1);
                    }
                    if (event.getBlockFace().equals(BlockFace.NORTH)) {
                        water_block_location.setZ(water_block_location.getBlockZ() - 1);
                    }
                    if (event.getBlockFace().equals(BlockFace.SOUTH)) {
                        water_block_location.setZ(water_block_location.getBlockZ() + 1);
                    }

                    if (!Vars.hasAdminPermission("blockbreak", p)) {
                        if (water_block_location.getWorld().getBlockAt(water_block_location).isLiquid()) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }

                if (PlayerData.isAPlayerFlag(event.getClickedBlock().getLocation())) {
                    event.setCancelled(true);

                    if (event.getHand().equals(EquipmentSlot.HAND)) {
                        if (p_data.isHisFlag(event.getClickedBlock().getLocation())) {
                            p.openInventory(FlagMenu.getPlayerFlagGUI(p));
                        } else {
                            HashMap<String, String> custom_replaces = new HashMap<>();
                            custom_replaces.putAll(Vars.generateCustomReplaces(p, "player"));
                            custom_replaces.putAll(Vars.generateCustomReplaces(PlayerData.getFlagOwner(event.getClickedBlock().getLocation()).getOfflinePlayer(), "his"));
                            Vars.runActions(Vars.other_flag_interaction_event, custom_replaces, p);
                        }
                    }

                    return;
                }
            }

            if (event.getItem() != null) {
                if (event.getItem().getType().equals(Material.valueOf(Vars.hookfeather_material))) {
                    event.setCancelled(true);
                    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        if (!hookfeather_cooldown.containsKey(p.getUniqueId())) {
                            event.getItem().setAmount(event.getItem().getAmount() - 1);
                            Vector player_velocity = p.getLocation().getDirection().multiply(Vars.hookfeather_jump_forward);
                            player_velocity.setY(Vars.hookfeather_jump_boost);
                            p.setVelocity(player_velocity);
                            Vars.playWorldSound("hookfeather_jump", p.getLocation());
                            hookfeather_fall_damage.add(p.getUniqueId());
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (!hookfeather_cooldown.containsKey(p.getUniqueId())) hookfeather_cooldown.put(p.getUniqueId(), Vars.hookfeather_use_cooldown);
                                    else hookfeather_cooldown.put(p.getUniqueId(), hookfeather_cooldown.get(p.getUniqueId()) - 1);

                                    if (Bukkit.getPlayer(p.getUniqueId()) != null && p.getInventory().getItemInMainHand().getType().equals(Material.valueOf(Vars.hookfeather_material.toUpperCase()))) {
                                        if (!Vars.hookfeather_actionbar_text.equals("")) {
                                            if (GeneralEvents.hookfeather_cooldown.containsKey(p.getUniqueId())) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Vars.hookfeather_actionbar_text.replace("%remaining_seconds%", String.valueOf(GeneralEvents.hookfeather_cooldown.get(p.getUniqueId())))));
                                        }
                                    }

                                    if (hookfeather_cooldown.get(p.getUniqueId()) < 1) {
                                        hookfeather_cooldown.remove(p.getUniqueId());
                                        if (Bukkit.getPlayer(p.getUniqueId()) != null && p.getInventory().getItemInMainHand().getType().equals(Material.valueOf(Vars.hookfeather_material.toUpperCase()))) {
                                            if (!Vars.hookfeather_actionbar_text.equals("")) {
                                                if (GeneralEvents.hookfeather_cooldown.containsKey(p.getUniqueId())) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
                                            }
                                        }
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(Vars.plugin, 0L, 20L);
                        } else Vars.sendString(Vars.hookfeather_cooldown_message.replace("%cooldown_seconds%", String.valueOf(hookfeather_cooldown.get(p.getUniqueId()))), p);
                    }
                } else if (isFlagItem(event.getItem())) {
                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        Location loc = event.getClickedBlock().getLocation();
                        loc.setY(loc.getBlockY() + 1);

                        if (!event.getBlockFace().equals(BlockFace.UP) || PlayerData.isAPlayerFlag(event.getClickedBlock().getLocation()) || Vars.flag_place_block_blacklist.contains(event.getClickedBlock().getType().toString()) || !loc.getWorld().getBlockAt(loc).getType().isAir()) {
                            event.setCancelled(true);
                            Vars.sendString(Vars.cant_place_there, p);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Vars.plugin.getLogger().severe(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        if (FlagMenu.isFlagMenu(event.getClickedInventory())) {
            event.setCancelled(true);

            Player p = (Player) event.getWhoClicked();
            PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

            String itemName = null;
            try {
                itemName =  event.getCurrentItem().getItemMeta().getCustomTagContainer().getCustomTag(new NamespacedKey(Vars.plugin, "itemName"), ItemTagType.STRING);
            } catch (Exception ignored) {}

            if (itemName != null) {
                if (itemName.equals("flag_upgrade")) {
                    if (Vars.econ != null) {
                        if (Vars.econ.getBalance(p) >= p_data.getLevelupCost()) {
                            Double money_spent = p_data.getLevelupCost();
                            Vars.econ.withdrawPlayer(p, money_spent);
                            p_data.setLevel(p_data.getLevel() + 1);

                            HashMap<String, String> custom_replaces = Vars.generateCustomReplaces(p, "player");
                            custom_replaces.put("%money_spent%", String.valueOf(money_spent));
                            Vars.runActions(Vars.levelup_event, custom_replaces, p);
                            
                            Bukkit.getPluginManager().callEvent(new PlayerLevelupEvent(p));
                        } else {
                            Vars.sendString(Vars.not_enough_money, p);
                        }
                    }
                } else if (itemName.equals("shop_button")) {
                    Vars.runActions(Vars.shop_button_event, Vars.generateCustomReplaces(p, "player"), p);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        if (Vars.disabled_worlds.contains(event.getPlayer().getLocation().getWorld().getName())) return;

        Player p = event.getPlayer();
        Block block = event.getBlockPlaced();
        PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

        if (isFlagItem(event.getItemInHand())) {
            if (Vars.hasUserPermission("ownflag.place", p)) {
                Location loc = block.getLocation();
                loc.setY(loc.getBlockY() + 1);
                if (!loc.getWorld().getBlockAt(loc).getType().isAir()) {
                    event.setCancelled(true);
                    Vars.sendString(Vars.cant_place_there, p);
                    return;
                }

                removeFlagItems(p);

                event.getBlockPlaced().getDrops().clear();

                p_data.breakFlag();
                p_data.setFlagLocation(block.getLocation());

                Vars.runActions(Vars.flag_place_event, Vars.generateCustomReplaces(p, "player"), p);
            } else {
                Vars.sendString(Vars.no_permission, p);
                event.setCancelled(true);
            }
        } else {
            Location loc1 = block.getLocation();
            loc1.setY(loc1.getBlockY() + 1);
            if (PlayerData.isAPlayerFlag(loc1)) {
                event.setCancelled(true);
                Vars.sendString(Vars.cant_place_there, p);
            }
            loc1.setY(loc1.getBlockY() - 2);
            if (PlayerData.isAPlayerFlag(loc1)) {
                event.setCancelled(true);
                Vars.sendString(Vars.cant_place_there, p);
            }
        }
    }

    public static HashMap<UUID, Integer> player_breaking_his_flag = new HashMap<>();
    public static HashMap<UUID, Integer> player_breaking_his_flag_taskId = new HashMap<>();
    public static HashMap<UUID, List<ArmorStand>> player_breaking_flag_armorstands = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (Vars.disabled_worlds.contains(event.getPlayer().getLocation().getWorld().getName())) return;

        Player p = event.getPlayer();
        Block block = event.getBlock();
        PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());
        if (PlayerData.isAPlayerFlag(block.getLocation())) {
            block.getDrops().clear();
            if (p_data.hasPlacedFlag() && block.getLocation().equals(p_data.getFlagLocation())) {
                event.setCancelled(true);

                if (Vars.hasUserPermission("ownflag.break", p)) {
                    if (!player_breaking_his_flag_taskId.containsKey(p.getUniqueId())) {
                        player_breaking_his_flag.put(p.getUniqueId(), Vars.player_flag_breaking_countdown);
                        Location loc = p_data.getFlagLocation();
                        loc.setX(loc.getBlockX() + .5);
                        loc.setY(loc.getBlockY() + Vars.breaking_hologram_height);
                        loc.setZ(loc.getBlockZ() + .5);
                        player_breaking_flag_armorstands.put(p.getUniqueId(), new ArrayList<>());
                        for (String line : Vars.breaking_hologram_text) {
                            ArmorStand armorstand = Vars.newArmorStand(loc);
                            player_breaking_flag_armorstands.get(p.getUniqueId()).add(armorstand);
                            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) line = PlaceholderAPI.setPlaceholders(p, line);
                            line = Vars.setPlaceholders(line, p);
                            armorstand.setCustomName(line);
                            loc.setY(loc.getY() - Vars.breaking_hologram_lines_height);
                        }

                        player_breaking_his_flag_taskId.put(p.getUniqueId(), Bukkit.getScheduler().runTaskTimer(Vars.plugin, () -> {
                            try {
                                if (!player_breaking_his_flag.containsKey(p.getUniqueId()))
                                    player_breaking_his_flag.put(p.getUniqueId(), Vars.player_flag_breaking_countdown);
                                else
                                    player_breaking_his_flag.put(p.getUniqueId(), player_breaking_his_flag.get(p.getUniqueId()) - 1);

                                int armorstand_count = 0;
                                for (String line : Vars.breaking_hologram_text) {
                                    try {
                                        ArmorStand armorstand = player_breaking_flag_armorstands.get(p.getUniqueId()).get(armorstand_count);
                                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                                            line = PlaceholderAPI.setPlaceholders(p, line);
                                        line = Vars.setPlaceholders(line, p);
                                        armorstand.setCustomName(line);
                                        armorstand_count++;
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

                                if (player_breaking_his_flag.get(p.getUniqueId()) < 1) {
                                    p_data.breakFlag();;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                p_data.breakFlag();
                            }
                        }, 0L, 20L).getTaskId());
                    }
                } else Vars.sendString(Vars.no_permission, p);
            } else {
                if (Vars.hasUserPermission("otherflags.break", p)) {
                    PlayerData flag_owner_data = PlayerData.getFlagOwner(block.getLocation());
                    OfflinePlayer flag_owner = flag_owner_data.getOfflinePlayer();

                    p_data.setBrokenFlags(p_data.getBrokenFlags() + 1);

                    String flag_broken_loss = Vars.setOfflinePlayerPlaceholders(Vars.flag_broken_loss, flag_owner);
                    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) flag_broken_loss = PlaceholderAPI.setPlaceholders(flag_owner, flag_broken_loss);
                    Double amount = new DoubleEvaluator().evaluate(flag_broken_loss);
                    try {
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
                        amount = Double.parseDouble(final_amount);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (Vars.econ != null) {
                        Vars.econ.withdrawPlayer(flag_owner_data.getOfflinePlayer(), amount);
                        Vars.econ.depositPlayer(p, amount);
                    }

                    HashMap<String, String> custom_replaces = new HashMap<>();
                    custom_replaces.put("%amount%", String.valueOf(amount));
                    custom_replaces.put("%amount_format%", Vars.moneyFormat(String.valueOf(amount)));
                    custom_replaces.putAll(Vars.generateCustomReplaces(p, "player"));
                    custom_replaces.putAll(Vars.generateCustomReplaces(flag_owner, "his"));
                    Vars.runActions(Vars.you_broken_flag_event, custom_replaces, p);

                    if (flag_owner.isOnline()) {
                        custom_replaces.clear();

                        custom_replaces.put("%amount%", String.valueOf(amount));
                        custom_replaces.put("%amount_format%", Vars.moneyFormat(String.valueOf(amount)));
                        custom_replaces.putAll(Vars.generateCustomReplaces(p, "his"));
                        custom_replaces.putAll(Vars.generateCustomReplaces(flag_owner, "player"));
                        Vars.runActions(Vars.your_flag_broken_event, custom_replaces, flag_owner_data.getOfflinePlayer().getPlayer());
                    }

                    flag_owner_data.breakFlag();
                } else Vars.sendString(Vars.no_permission, p);
            }
            return;
        } else {
            Location loc = block.getLocation();
            loc.setY(loc.getBlockY() + 1);
            if (PlayerData.isAPlayerFlag(loc)) event.setCancelled(true);
        }

        if (!Vars.hasAdminPermission("blockbreak", event.getPlayer())) {
            if (!Vars.allowed_blocks_break.contains(event.getBlock().getType().toString().toUpperCase())) event.setCancelled(true);
        }
    }

    public static HashMap<UUID, Integer> player_killstreaks = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (Vars.disabled_worlds.contains(event.getEntity().getLocation().getWorld().getName())) return;

        event.getDrops().removeAll(getAvailableFlagItems());

        for (ItemStack drop : event.getDrops()) {
            if (isFlagItem(drop)) drop.setAmount(0);
        }

        Player dead_player = event.getEntity().getPlayer();
        Player killer_player = null;
        if (dead_player.getKiller() != null) killer_player = dead_player.getKiller();
        PlayerData dead_player_data = PlayerData.getPlayerData(dead_player.getUniqueId());

        dead_player_data.setDeaths(dead_player_data.getDeaths() + 1);
        player_killstreaks.remove(dead_player.getUniqueId());

        if (killer_player != null) {
            PlayerData killer_player_data = PlayerData.getPlayerData(killer_player.getUniqueId());
            HashMap<String, String> custom_replaces = new HashMap<>(Vars.generateCustomReplaces(dead_player, "dead_player"));

            String player_killed_loss = Vars.setPlaceholders(Vars.player_killed_loss, dead_player);
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) player_killed_loss = PlaceholderAPI.setPlaceholders(dead_player, player_killed_loss);
            Double amount = new DoubleEvaluator().evaluate(player_killed_loss);
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
            amount = Double.parseDouble(final_amount);

            if (Vars.econ != null) {
                Vars.econ.withdrawPlayer(dead_player, amount);
                Vars.econ.depositPlayer(killer_player, amount);
            }

            custom_replaces.put("%amount%", String.valueOf(amount));
            custom_replaces.put("%amount_format%", Vars.moneyFormat(String.valueOf(amount)));

            killer_player_data.setKills(killer_player_data.getKills() + 1);
            if (!player_killstreaks.containsKey(killer_player.getUniqueId())) player_killstreaks.put(killer_player.getUniqueId(), 1);
            else player_killstreaks.put(killer_player.getUniqueId(), player_killstreaks.get(killer_player.getUniqueId()) + 1);
            if ((player_killstreaks.get(killer_player.getUniqueId()) % Vars.killstreak_offset_count) == 0) {
                Vars.runActions(Vars.killstreak_offset_reach_event, Vars.generateCustomReplaces(killer_player, "player"), killer_player);
            }
            custom_replaces.putAll(Vars.generateCustomReplaces(killer_player, "killer"));

            Vars.runActions(Vars.kill_event, custom_replaces, killer_player);
            Vars.runActions(Vars.killed_event, custom_replaces, dead_player);
        }
    }

    public static HashMap<UUID, Integer> player_respawn_cooldown = new HashMap<>();

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (Vars.disabled_worlds.contains(event.getPlayer().getLocation().getWorld().getName())) return;

        Player p = event.getPlayer();
        PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

        HashMap<String, String> custom_replaces = Vars.generateCustomReplaces(p, "player");

        if (Vars.respawn_cooldown) {
            p.setGameMode(GameMode.valueOf(Vars.spectator_gamemode));
            if (Vars.respawn_keeps_player_location) event.setRespawnLocation(p.getLocation());
            else if (!Vars.respawn_location.equals("")) event.setRespawnLocation(Vars.stringToLocation(Vars.respawn_location));
            p.setVelocity(new Vector(0,0,0));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Bukkit.getPlayer(p.getUniqueId()) == null) cancel();

                    if (!player_respawn_cooldown.containsKey(p.getUniqueId()))
                        player_respawn_cooldown.put(p.getUniqueId(), Vars.player_respawn_cooldown);
                    else
                        player_respawn_cooldown.put(p.getUniqueId(), player_respawn_cooldown.get(p.getUniqueId()) - 1);
                    custom_replaces.put("%remaining_seconds%", String.valueOf(player_respawn_cooldown.get(p.getUniqueId())));

                    if (player_respawn_cooldown.get(p.getUniqueId()) > 0) {
                        Vars.runActions(Vars.respawn_cooldown_event, custom_replaces, p);
                    } else {
                        player_respawn_cooldown.remove(p.getUniqueId());

                        if (p_data.hasPlacedFlag()) {
                            Location spawn_location = p_data.getRespawnLocation();
                            spawn_location.setPitch(p.getLocation().getPitch());
                            spawn_location.setYaw(p.getLocation().getYaw());
                            p.teleport(spawn_location);
                        } else {
                            Spawn.sendToSpawn(p, false);
                        }
                        p.setGameMode(GameMode.valueOf(Vars.respawn_gamemode));
                        Vars.runActions(Vars.respawn_event, custom_replaces, Bukkit.getPlayer(p.getUniqueId()));

                        cancel();
                    }
                }
            }.runTaskTimer(Vars.plugin, 0L, 20L);
        } else {
            if (p_data.hasPlacedFlag()) {
                Location spawn_location = p_data.getRespawnLocation();
                spawn_location.setPitch(p.getLocation().getPitch());
                spawn_location.setYaw(p.getLocation().getYaw());
                event.setRespawnLocation(spawn_location);
            } else {
                if (!Vars.spawn_location.equals("")) event.setRespawnLocation(Vars.stringToLocation(Vars.spawn_location));
            }
            p.setGameMode(GameMode.valueOf(Vars.respawn_gamemode));
            Vars.runActions(Vars.respawn_event, custom_replaces, p);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (Vars.disabled_worlds.contains(event.getEntity().getLocation().getWorld().getName())) return;

        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();

            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                if (hookfeather_fall_damage.contains(p.getUniqueId())) {
                    event.setCancelled(true);
                    hookfeather_fall_damage.remove(p.getUniqueId());
                }
            }
        }
    }

    public static HashMap<UUID, PlayerAFKTracker> afk_time = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

        if (Spawn.teleport_cooldown_taskId.containsKey(p.getUniqueId())) {
            Location loc = p.getLocation();
            Location loc2 = Spawn.teleport_location.get(p.getUniqueId());
            if (loc.getX() != loc2.getX() || loc.getY() != loc2.getY() || loc.getZ() != loc2.getZ()) {
                Bukkit.getScheduler().cancelTask(Spawn.teleport_cooldown_taskId.get(p.getUniqueId()));

                Spawn.teleport_cooldown_taskId.remove(p.getUniqueId());
                Spawn.teleport_cooldown.remove(p.getUniqueId());
                Spawn.teleport_location.remove(p.getUniqueId());

                Vars.sendString(Vars.you_moved, p);
            }
        }

        if (Vars.disabled_worlds.contains(event.getPlayer().getLocation().getWorld().getName())) return;

        if (!p_data.hasPlacedFlag()) giveNewFlagItem(p);
        if (!afk_time.containsKey(p.getUniqueId())) afk_time.put(p.getUniqueId(), new PlayerAFKTracker(p.getUniqueId(), p.getLocation()));
        if (p.getLocation().getX() != afk_time.get(p.getUniqueId()).location.getX() || p.getLocation().getZ() != afk_time.get(p.getUniqueId()).location.getZ()) {
            afk_time.get(p.getUniqueId()).location = p.getLocation();
            afk_time.get(p.getUniqueId()).time = 0;
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (Vars.disabled_worlds.contains(event.getPlayer().getLocation().getWorld().getName())) return;

        Player p = event.getPlayer();

        if (Spawn.teleport_cooldown_taskId.containsKey(p.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(Spawn.teleport_cooldown_taskId.get(p.getUniqueId()));

            Spawn.teleport_cooldown_taskId.remove(p.getUniqueId());
            Spawn.teleport_cooldown.remove(p.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

        p.setGameMode(GameMode.valueOf(Vars.respawn_gamemode));

        if (player_respawn_cooldown.containsKey(p.getUniqueId())) {
            p.setGameMode(GameMode.valueOf(Vars.respawn_gamemode));
            if (p_data.hasPlacedFlag()) p.teleport(p_data.getRespawnLocation());
            else Spawn.sendToSpawn(p, false);
            player_respawn_cooldown.remove(p.getUniqueId());
        }

        afk_time.remove(p.getUniqueId());

        if (Vars.teleport_to_spawn_on_quit) {
            if (p_data.hasPlacedFlag()) p.teleport(p_data.getRespawnLocation());
            else Spawn.sendToSpawn(p, false);
        }

        Vars.runActions(Vars.join_event, Vars.generateCustomReplaces(p, "player"), p);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        PlayerData p_data = PlayerData.getPlayerData(p.getUniqueId());

        if (player_respawn_cooldown.containsKey(p.getUniqueId())) {
            p.setGameMode(GameMode.valueOf(Vars.respawn_gamemode));
            if (p_data.hasPlacedFlag()) p.teleport(p_data.getRespawnLocation());
            else Spawn.sendToSpawn(p, false);
            player_respawn_cooldown.remove(p.getUniqueId());
        }
        if (Spawn.teleport_cooldown_taskId.containsKey(p.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(Spawn.teleport_cooldown_taskId.get(p.getUniqueId()));
            Spawn.teleport_cooldown_taskId.remove(p.getUniqueId());
        }

        afk_time.remove(p.getUniqueId());
        
        if (Vars.teleport_to_spawn_on_quit) {
            if (p_data.hasPlacedFlag()) p.teleport(p_data.getRespawnLocation());
            else Spawn.sendToSpawn(p, false);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PvPManager")) {
            if (PvPManager.getInstance().getPlayerHandler().get(p).isInCombat()) {
                Player enemy_p = PvPManager.getInstance().getPlayerHandler().get(p).getEnemy().getPlayer();
                PlayerData enemy_p_data = PlayerData.getPlayerData(enemy_p.getUniqueId());
                HashMap<String, String> custom_replaces = new HashMap<>();

                String player_ct_logout_loss = Vars.setPlaceholders(Vars.player_ct_logout_loss, p);
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) player_ct_logout_loss = PlaceholderAPI.setPlaceholders(p, player_ct_logout_loss);
                Double amount = 0D;
                try {
                    amount = new DoubleEvaluator().evaluate(player_ct_logout_loss);
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
                    amount = Double.parseDouble(final_amount);

                    if (Vars.econ != null) {
                        Vars.econ.withdrawPlayer(p, amount);
                        Vars.econ.depositPlayer(enemy_p, amount);
                    }
                } catch (Exception ex) {
                    Vars.plugin.getLogger().warning("Unable to get the player ct logout money loss amount!!");
                    ex.printStackTrace();
                }

                custom_replaces.put("%amount%", String.valueOf(amount));
                custom_replaces.put("%amount_format%", Vars.moneyFormat(String.valueOf(amount)));

                player_killstreaks.remove(p.getUniqueId());

                enemy_p_data.setKills(enemy_p_data.getKills() + 1);
                if (!player_killstreaks.containsKey(enemy_p.getUniqueId())) player_killstreaks.put(enemy_p.getUniqueId(), 1);
                else player_killstreaks.put(enemy_p.getUniqueId(), player_killstreaks.get(enemy_p.getUniqueId()) + 1);
                if ((player_killstreaks.get(enemy_p.getUniqueId()) % Vars.killstreak_offset_count) == 0) {
                    Vars.runActions(Vars.killstreak_offset_reach_event, Vars.generateCustomReplaces(enemy_p, "player"), enemy_p);
                }
                custom_replaces.putAll(Vars.generateCustomReplaces(p, "left_player"));
                custom_replaces.putAll(Vars.generateCustomReplaces(enemy_p, "enemy_player"));

                Vars.runActions(Vars.player_ct_logout_event, custom_replaces, p);
                Vars.runActions(Vars.enemy_ct_logout_event, custom_replaces, enemy_p);
            }
        }
    }

    ///////////////////////////////////////////////////////

    @EventHandler
    public void onExpGain(PlayerExpChangeEvent event) {
        if (Vars.use_expbar_to_display_level) event.getPlayer().setExp(0F);
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        for (Block block : new ArrayList<>(event.blockList())) {
            if (PlayerData.isAPlayerFlag(block.getLocation())) event.blockList().remove(block);

            Location loc1 = block.getLocation();
            loc1.setY(loc1.getBlockY() + 1);
            if (PlayerData.isAPlayerFlag(loc1)) {
                event.blockList().remove(block);
            }
        }
    }

    ///////////////////////////////////////////////////////

    public static List<ItemStack> getAvailableFlagItems() {
        List<ItemStack> available_flag_items = new ArrayList<>();
        for (Object flag_item_object : Vars.flag_items) {
            try {
                available_flag_items.add((ItemStack) flag_item_object);
            } catch (Exception ex) {
                Vars.plugin.getLogger().severe("invalid item in flag_items list: " + flag_item_object.toString());
            }
        }

        return available_flag_items;
    }

    public static boolean hasFlagItem(Player p) {
        Boolean has_the_flag = false;
        for (ItemStack flag_item : getAvailableFlagItems()) {
            for (ItemStack player_item : p.getInventory()) {
                if (player_item != null && player_item.isSimilar(flag_item)) {
                    has_the_flag = true;
                    break;
                }
            }
            p.getInventory().getItemInOffHand().isSimilar(flag_item);
        }
        return has_the_flag;
    }

    public static void removeFlagItems(Player p) {
        try {
            for (ItemStack flag_item : getAvailableFlagItems()) {
                for (ItemStack player_item : p.getInventory()) {
                    if (player_item != null && player_item.isSimilar(flag_item)) p.getInventory().remove(player_item);
                }
                if (p.getInventory().getItemInOffHand().isSimilar(flag_item))
                    p.getInventory().getItemInOffHand().setAmount(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void giveNewFlagItem(Player p) {
        if (!hasFlagItem(p)) {
            List<ItemStack> available_flag_items = getAvailableFlagItems();
            if (available_flag_items.size() > 0) p.getInventory().addItem(available_flag_items.get(new Random().nextInt(available_flag_items.size())));
        }
    }

    public static boolean isFlagItem(ItemStack item) {
        for (ItemStack flag_item : getAvailableFlagItems()) {
            if (flag_item.isSimilar(item)) return true;
        }

        return false;
    }

}

class PlayerAFKTracker {

    public final UUID pUUID;
    public Integer time;
    public Location location;

    public PlayerAFKTracker(UUID pUUID, Location location) {
        this.pUUID = pUUID;
        time = 0;
        this.location = location;
    }

}