package xyz.oribuin.staffchat.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.oribuin.staffchat.bungee.StaffChatBungee;
import xyz.oribuin.staffchat.bungee.managers.MessageManager;
import xyz.oribuin.staffchat.bungee.utils.HexUtils;

public class CmdStaffchat extends Command {

    private final StaffChatBungee plugin;

    public CmdStaffchat(StaffChatBungee plugin) {
        super("staffchat", "", "sc", "schat");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MessageManager messageManager = this.plugin.getMessageManager();

        if (!sender.hasPermission("eternalsc.use")) {
            messageManager.sendMessage(sender, "invalid-permission");
            return;
        }

        if (args.length == 0) {
            messageManager.sendMessage(sender, "invalid-arguments.staffchat-command");
            return;
        }

        final String msg = String.join(" ", args);
        messageManager.sendSc(sender, msg);
        if (sender instanceof ProxiedPlayer)
            ProxyServer.getInstance().getConsole().sendMessage(msg("[StaffChat] (" + ((ProxiedPlayer) sender).getServer().getInfo().getName() + ") " + sender.getName() + ": " + msg));
    }

    private BaseComponent[] msg(String string) {
        return TextComponent.fromLegacyText(HexUtils.colorify(string));
    }
}
