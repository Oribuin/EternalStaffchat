package xyz.oribuin.staffchat.bungee.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import xyz.oribuin.staffchat.bungee.StaffChatBungee;
import xyz.oribuin.staffchat.bungee.managers.ConfigManager;
import xyz.oribuin.staffchat.bungee.managers.MessageManager;

import javax.swing.*;
import java.util.List;
import java.util.UUID;

public class PlayerChat implements Listener {

    private StaffChatBungee plugin;

    public PlayerChat() {
        this.plugin = StaffChatBungee.getInstance();
    }

    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        List<UUID> list = this.plugin.toggleList;
        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            if (event.isCancelled() || event.isProxyCommand()) return;

            if (!player.hasPermission("eternalsc.use")) {
                if (list.contains(player.getUniqueId())) {
                    list.remove(player.getUniqueId());
                }

                return;
            }

            if (list.contains(player.getUniqueId())) {
                if (player.hasPermission("eternalsc.sc")) {
                    this.plugin.sendSc(player, event.getMessage());
                    ProxyServer.getInstance().getConsole().sendMessage(msg("[StaffChat] (" + player.getServer().getInfo().getName() + ") " + player.getName() + ": " + event.getMessage()));
                    event.setCancelled(true);
                }
            }

            if (ConfigManager.Setting.SHORTCUTS_ENABLED.getBoolean() && player.hasPermission("eternalsc.use")) {
                if (list.contains(player.getUniqueId())) return;

                for (String string : ConfigManager.Setting.SHORTCUTS.getStringList()) {
                    if (event.getMessage().startsWith(string)) {
                        this.plugin.sendSc(player, event.getMessage().substring(1));
                        ProxyServer.getInstance().getConsole().sendMessage(msg("[StaffChat] (" + player.getServer().getInfo().getName() + ") " + player.getName() + ": " + event.getMessage().substring(1)));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    // Define the dumb BaseComponent because Bungeecord sucks ass
    private BaseComponent[] msg(String string) {
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', string));
    }
}
