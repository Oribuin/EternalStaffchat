package xyz.oribuin.staffchat.spigot.managers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.staffchat.spigot.StaffChatSpigot;
import xyz.oribuin.staffchat.spigot.hook.PlaceholderAPIHook;
import xyz.oribuin.staffchat.spigot.utils.FileUtils;
import xyz.oribuin.staffchat.spigot.utils.StringPlaceholders;

import java.io.File;

public class MessageManager extends Manager {

    private final static String MESSAGE_CONFIG = "messages.yml";

    private FileConfiguration messageConfig;

    public MessageManager(StaffChatSpigot plugin) {
        super(plugin);
    }

    @Override
    public void reload() {
        FileUtils.createFile(this.plugin, MESSAGE_CONFIG);
        this.messageConfig = YamlConfiguration.loadConfiguration(new File(this.plugin.getDataFolder(), MESSAGE_CONFIG));
    }

    public void sendMessage(CommandSender sender, String messageId) {
        this.sendMessage(sender, messageId, StringPlaceholders.empty());
    }

    public void sendMessage(CommandSender sender, String messageId, StringPlaceholders placeholders) {
        sender.sendMessage(color(this.messageConfig.getString("prefix") + placeholders.apply(this.messageConfig.getString(messageId))));
    }

    public void sendSCMessage(CommandSender sender, StringPlaceholders placeholders) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage(PlaceholderAPIHook.apply(player, color(placeholders.apply(ConfigManager.Setting.STAFFCHAT_FORMAT.getString()))));
        }
    }

    private String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
