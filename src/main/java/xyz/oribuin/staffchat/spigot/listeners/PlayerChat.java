package xyz.oribuin.staffchat.spigot.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.oribuin.staffchat.spigot.StaffChatSpigot;
import xyz.oribuin.staffchat.spigot.managers.ConfigManager;
import xyz.oribuin.staffchat.spigot.managers.MessageManager;

import java.util.List;
import java.util.UUID;

public class PlayerChat implements Listener {

    private final StaffChatSpigot plugin;

    public PlayerChat(StaffChatSpigot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        List<UUID> list = this.plugin.toggleList;
        Player player = event.getPlayer();
        MessageManager msgM = this.plugin.getMessageManager();


        if (list.contains(player.getUniqueId())) {
            if (player.hasPermission("eternal.sc")) {
                msgM.sendSc(player, event.getMessage());
                Bukkit.getConsoleSender().sendMessage("[StaffChat] " + player.getName() + ": " + event.getMessage());
                event.setCancelled(true);
            } else {
                list.remove(player.getUniqueId());
            }
        }

        // Check Player Shortcuts
        if (ConfigManager.Setting.SHORTCUTS_ENABLED.getBoolean() && player.hasPermission("eternalsc.use")) {
            if (list.contains(player.getUniqueId())) return;

            for (String string : ConfigManager.Setting.SHORTCUTS.getStringList()) {
                if (event.getMessage().startsWith(string)) {
                    event.setCancelled(true);
                    Bukkit.getConsoleSender().sendMessage("[StaffChat] " + player.getName() + ": " + event.getMessage().substring(1));
                    msgM.sendSc(player, event.getMessage().substring(1));
                }
            }
        }

    }
}
