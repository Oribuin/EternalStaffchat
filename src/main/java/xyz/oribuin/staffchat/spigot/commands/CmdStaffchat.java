package xyz.oribuin.staffchat.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class CmdStaffchat extends OriCommand {

    public CmdStaffchat() {
        super("staffchat");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        MessageManager messageManager = StaffChatSpigot.getInstance().getMessageManager();

        // Check for permission
        if (!sender.hasPermission("eternalsc.use")) {
            messageManager.sendMessage(sender, "invalid-permission");
            return true;
        }

        // Check for correct args
        if (args.length == 0) {
            messageManager.sendMessage(sender, "invalid-arguments.staffchat-command");
            return true;
        }

        final String msg = String.join(" ", args);
        messageManager.sendSc(sender, msg);
        if (sender instanceof Player)
            Bukkit.getConsoleSender().sendMessage("[StaffChat] " + sender.getName() + ": " + msg);

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
        } else {
            return null;
        }

        return suggestions;
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
