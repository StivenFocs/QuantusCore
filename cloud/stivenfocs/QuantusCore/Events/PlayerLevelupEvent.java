package cloud.stivenfocs.QuantusCore.Events;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public final class PlayerLevelupEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Inventory menu;

    public PlayerLevelupEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}