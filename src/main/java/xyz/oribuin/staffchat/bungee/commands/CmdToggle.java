package xyz.oribuin.staffchat.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.oribuin.staffchat.bungee.StaffChatBungee;
import xyz.oribuin.staffchat.bungee.managers.MessageManager;

import java.util.List;
import java.util.UUID;

public class CmdToggle extends Command {

    private StaffChatBungee plugin;

    public CmdToggle() {
        super("sctoggle", "", "sct");
        this.plugin = StaffChatBungee.getInstance();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MessageManager messageManager = this.plugin.getMessageManager();

        if (!sender.hasPermission("eternalsc.toggle")) {
            messageManager.sendMessage(sender, "invalid-permission");
            return;
        }

        if (!(sender instanceof ProxiedPlayer)) {
            messageManager.sendMessage(sender, "player-only");
            return;
        }

        List<UUID> list = StaffChatBungee.getInstance().toggleList;
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (!list.contains(player.getUniqueId())) {
            messageManager.sendMessage(sender, "commands.toggle-enabled");
            list.add(player.getUniqueId());
        } else {
            messageManager.sendMessage(sender, "commands.toggle-disabled");
            list.remove(player.getUniqueId());
        }
    }
}
