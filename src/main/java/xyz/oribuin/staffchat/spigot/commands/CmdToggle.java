package xyz.oribuin.staffchat.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import xyz.oribuin.staffchat.spigot.StaffChatSpigot;
import xyz.oribuin.staffchat.spigot.managers.MessageManager;
import xyz.oribuin.staffchat.spigot.utils.OriCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CmdToggle extends OriCommand {

    public CmdToggle() {
        super("sctoggle");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        MessageManager messageManager = StaffChatSpigot.getInstance().getMessageManager();

        // Check for permission
        if (!sender.hasPermission("eternalsc.toggle")) {
            messageManager.sendMessage(sender, "invalid-permission");
            return true;
        }

        if (!(sender instanceof Player)) {
            messageManager.sendMessage(sender, "player-only");
            return true;
        }

        List<UUID> list = StaffChatSpigot.getInstance().toggleList;

        Player player = (Player) sender;
        if (!list.contains(player.getUniqueId())) {
            messageManager.sendMessage(sender, "commands.toggle-enabled");
            list.add(player.getUniqueId());
        } else {
            messageManager.sendMessage(sender, "commands.toggle-disabled");
            list.remove(player.getUniqueId());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("staffchat"))
            return Collections.emptyList();

        List<String> suggestions = new ArrayList<>();

        if (args.length == 0 || args.length == 1) {
            String subCommandName = args.length == 0 ? "" : args[0];
            List<String> commands = new ArrayList<>();
            if (sender.hasPermission("eternalsc.use"))
                commands.add("<message>");

            StringUtil.copyPartialMatches(subCommandName, commands, suggestions);
        }

        return suggestions;
    }
}