package xyz.oribuin.staffchat.bungee.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.oribuin.staffchat.bungee.StaffChatBungee;
import xyz.oribuin.staffchat.bungee.managers.ConfigManager;
import xyz.oribuin.staffchat.bungee.managers.MessageManager;
import xyz.oribuin.staffchat.bungee.utils.HexUtils;

import java.util.List;
import java.util.UUID;

public class PlayerChat implements Listener {

    private final StaffChatBungee plugin;

    public PlayerChat(StaffChatBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        List<UUID> list = this.plugin.toggleList;
        // Get Message Manager
        MessageManager msgM = this.plugin.getMessageManager();

        // CHeck if the sender is a player
        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();

            // Ignore ChatEvent if its cancelled or a command
            if (event.isCancelled() || event.isProxyCommand() || event.isCommand()) return;

            // If the player can't use staffchat and they have statffchat toggled, disable it
            if (!player.hasPermission("eternalsc.use")) {
                list.remove(player.getUniqueId());
                return;
            }

            if (!list.contains(player.getUniqueId())) return;

            // If the list contains the player and they permission to use the plugin
            if (list.contains(player.getUniqueId()) && player.hasPermission("eternalsc.sc")) {
                // Send staffchat message
                msgM.sendSc(player, event.getMessage());

                // Send message to console
                ProxyServer.getInstance().getConsole().sendMessage(msg("[StaffChat] (" + player.getServer().getInfo().getName() + ") " + player.getName() + ": " + event.getMessage()));
                // Cancel event
                event.setCancelled(true);
            }

            // If Shortcuts are enabled and the player has permission to use staffchat
            if (ConfigManager.Setting.SHORTCUTS_ENABLED.getBoolean() && player.hasPermission("eternalsc.use")) {
                // Check if they have staffchat toggled on


                // For loop each shortcut
                for (String string : ConfigManager.Setting.SHORTCUTS.getStringList()) {

                    // If the string contains the shortcut, send staffchat message
                    if (event.getMessage().startsWith(string)) {
                        msgM.sendSc(player, event.getMessage().substring(1));

                        // Send message to console
                        ProxyServer.getInstance().getConsole().sendMessage(msg("[StaffChat] (" + player.getServer().getInfo().getName() + ") " + player.getName() + ": " + event.getMessage().substring(1)));
                        // Cancel event
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private BaseComponent[] msg(String string) {
        return TextComponent.fromLegacyText(HexUtils.colorify(string));
    }
}
