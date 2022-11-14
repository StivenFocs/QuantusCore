package cloud.stivenfocs.QuantusCore.Commands;

import cloud.stivenfocs.QuantusCore.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Top implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Vars.hasUserPermission("top", sender)) {
            Vars.sendStringList(Vars.top_levels, sender);
        } else Vars.sendString(Vars.no_permission, sender);
        return false;
    }
}
