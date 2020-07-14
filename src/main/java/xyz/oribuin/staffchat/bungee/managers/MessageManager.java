package xyz.oribuin.staffchat.bungee.managers;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.oribuin.staffchat.bungee.StaffChatBungee;
import xyz.oribuin.staffchat.bungee.utils.FileUtils;
import xyz.oribuin.staffchat.bungee.utils.StringPlaceholders;
import xyz.oribuin.staffchat.bungee.utils.HexUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class MessageManager extends Manager {

    private static final Pattern HEX_PATTERN = Pattern.compile("\\{#([A-Fa-f0-9]){6}}");
    private final static String MESSAGE_CONFIG = "messages.yml";

    private Configuration messageConfig;

    public MessageManager(StaffChatBungee plugin) {
        super(plugin);
    }

    // Reload config
    @Override
    public void reload() {
        FileUtils.createFile(plugin, "messages.yml");

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
        if (messageConfig.getString(messageId) == null) {
            sender.sendMessage(TextComponent.fromLegacyText(HexUtils.colorify("{#ff4072}" + messageId + " is null in messages.yml")));
            return;
        }

        if (!messageConfig.getString(messageId).isEmpty()) {
            sender.sendMessage(TextComponent.fromLegacyText(HexUtils.colorify(this.messageConfig.getString("prefix") + placeholders.apply(this.messageConfig.getString(messageId)))));
        }
    }

    // Define a staffchat one because im lazy.
    public void sendSCMessage(CommandSender sender, ProxiedPlayer receiver, StringPlaceholders placeholders) {
        if (sender instanceof ProxiedPlayer) {
            receiver.sendMessage(TextComponent.fromLegacyText(HexUtils.colorify(placeholders.apply(ConfigManager.Setting.STAFFCHAT_FORMAT.getString()))));
        }
    }

    public void sendSc(CommandSender sender, String message) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pplayer = (ProxiedPlayer) sender;
            StringPlaceholders stringPlaceholders = StringPlaceholders.builder("sender", sender.getName())
                    .addPlaceholder("message", message)
                    .addPlaceholder("server", pplayer.getServer().getInfo().getName())
                    .addPlaceholder("prefix", this.messageConfig.getString("prefix")).build();

            ProxyServer.getInstance().getPlayers().stream()
                    .filter(player -> player.hasPermission("eternalsc.use"))
                    .forEach(player -> this.sendSCMessage(sender, player, stringPlaceholders));
        } else {

            StringPlaceholders stringPlaceholders = StringPlaceholders.builder("sender", sender.getName())
                    .addPlaceholder("message", message)
                    .addPlaceholder("server", "None")
                    .addPlaceholder("prefix", this.messageConfig.getString("prefix")).build();

            ProxyServer.getInstance().getPlayers().stream()
                    .filter(player -> player.hasPermission("eternalsc.use"))
                    .forEach(player -> this.sendSCMessage(sender, player, stringPlaceholders));
        }
    }
}
