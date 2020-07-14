package xyz.oribuin.staffchat.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import xyz.oribuin.staffchat.bungee.StaffChatBungee;
import xyz.oribuin.staffchat.bungee.managers.MessageManager;
import xyz.oribuin.staffchat.bungee.utils.StringPlaceholders;

public class CmdAdmin extends Command {

    private final StaffChatBungee plugin;

    public CmdAdmin(StaffChatBungee plugin) {
        super("scadmin");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MessageManager messageManager = StaffChatBungee.getInstance().getMessageManager();

        if (args.length == 1 && args[0].equals("reload")) {
            if (!sender.hasPermission("eternalsc.reload")) {
                messageManager.sendMessage(sender, "invalid-permission");
                return;
            }

            this.plugin.reload();
            messageManager.sendMessage(sender, "reload", StringPlaceholders.single("version", this.plugin.getDescription().getVersion()));
        } else {
            messageManager.sendMessage(sender, "invalid-command");
        }
    }
}
