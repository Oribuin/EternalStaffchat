package xyz.oribuin.staffchat.spigot.managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import xyz.oribuin.staffchat.spigot.StaffChatSpigot;
import xyz.oribuin.staffchat.spigot.utils.StringPlaceholders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager extends Manager implements TabExecutor {

    public CommandManager(StaffChatSpigot plugin) {
        super(plugin);

        PluginCommand shardCommand = this.plugin.getCommand("scadmin");
        if (shardCommand != null) {
            shardCommand.setExecutor(this);
            shardCommand.setTabCompleter(this);
        }
    }

    @Override
    public void reload() {
        // Unused
    }

    private void onReloadCommand(CommandSender sender) {
        MessageManager messageManager = this.plugin.getMessageManager();
        if (!sender.hasPermission("eternalsc.reload")) {
            messageManager.sendMessage(sender, "invalid-permission");
            return;
        }

        this.plugin.reload();
        messageManager.sendMessage(sender, "reload", StringPlaceholders.single("version", this.plugin.getDescription().getVersion()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessageManager messageManager = this.plugin.getMessageManager();

        if (args.length == 1 && args[0].toLowerCase().equals("reload")) {
            onReloadCommand(sender);
        } else {
            messageManager.sendMessage(sender, "invalid-command");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("scadmin"))
            return Collections.emptyList();

        List<String> suggestions = new ArrayList<>();

        if (args.length == 0 || args.length == 1) {
            String subCommandName = args.length == 0 ? "" : args[0];
            List<String> commands = new ArrayList<>();
            if (sender.hasPermission("eternalsc.reload"))
                commands.add("reload");

            StringUtil.copyPartialMatches(subCommandName, commands, suggestions);
        }


        return suggestions;
    }

}
