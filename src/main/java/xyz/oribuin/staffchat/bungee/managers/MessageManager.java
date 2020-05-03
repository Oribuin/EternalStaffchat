package xyz.oribuin.staffchat.bungee.managers;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bukkit.ChatColor;
import xyz.oribuin.staffchat.bungee.StaffChatBungee;
import xyz.oribuin.staffchat.bungee.utils.StringPlaceholders;

import java.io.File;
import java.io.IOException;

public class MessageManager extends Manager {

    private final static String MESSAGE_CONFIG = "messages.yml";

    private Configuration messageConfig;

    public MessageManager(StaffChatBungee plugin) {
        super(plugin);
    }

    // Reload config
    @Override
    public void reload() {
        this.plugin.createConfig(MESSAGE_CONFIG);
        try {
            this.messageConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.plugin.getDataFolder(), MESSAGE_CONFIG));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Send message without String Placeholders
    public void sendMessage(CommandSender sender, String messageId) {
        this.sendMessage(sender, messageId, StringPlaceholders.empty());
    }

    // Build String Placeholders
    public void sendMessage(CommandSender sender, String messageId, StringPlaceholders placeholders) {
        sender.sendMessage(msg(this.messageConfig.getString("prefix") + placeholders.apply(this.messageConfig.getString(messageId))));
    }

    // Define a staffchat one because im lazy.
    public void sendSCMessage(CommandSender sender, StringPlaceholders placeholders) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            player.sendMessage(msg(placeholders.apply(ConfigManager.Setting.STAFFCHAT_FORMAT.getString())));
        }
    }

    // Define the dumb BaseComponent because Bungeecord sucks ass
    private BaseComponent[] msg(String string) {
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', string));
    }
}
