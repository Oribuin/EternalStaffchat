package xyz.oribuin.staffchat.spigot.managers;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.staffchat.spigot.StaffChatSpigot;
import xyz.oribuin.staffchat.spigot.hook.PlaceholderAPIHook;
import xyz.oribuin.staffchat.spigot.utils.FileUtils;
import xyz.oribuin.staffchat.spigot.utils.HexUtils;
import xyz.oribuin.staffchat.spigot.utils.StringPlaceholders;

import java.io.File;
import java.util.regex.Pattern;

public class MessageManager extends Manager {

    private static final Pattern HEX_PATTERN = Pattern.compile("\\{#([A-Fa-f0-9]){6}}");
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

    /**
     * Send Message to CommandSender without StringPlaceholders
     */
    public void sendMessage(CommandSender sender, String messageId) {
        this.sendMessage(sender, messageId, StringPlaceholders.empty());
    }

    /**
     * Send message to CommandSender with Hex Colors &Placeholders
     */

    public void sendMessage(CommandSender sender, String messageId, StringPlaceholders placeholders) {

        if (messageConfig.getString(messageId) == null) {
            sender.spigot().sendMessage(TextComponent.fromLegacyText(HexUtils.colorify("{#ff4072}" + messageId + " is null in messages.yml")));
            return;
        }

        if (!messageConfig.getString(messageId).isEmpty()) {
            sender.spigot().sendMessage(TextComponent.fromLegacyText(HexUtils.colorify(this.parsePlaceholders(sender, this.messageConfig.getString("prefix") + placeholders.apply(this.messageConfig.getString(messageId))))));
        }
    }

    /**
     * Send StaffChat message to player
     */

    public void sendSCMessage(CommandSender sender, StringPlaceholders placeholders) {
        if (sender instanceof Player) {
            sender.spigot().sendMessage(TextComponent.fromLegacyText(HexUtils.colorify(this.parsePlaceholders(sender, placeholders.apply(ConfigManager.Setting.STAFFCHAT_FORMAT.getString())))));
        }
    }

    /**
     * Send StaffChat message to all players with eternalsc.use permission
     */
    public void sendSc(CommandSender sender, String message) {
        StringPlaceholders stringPlaceholders = StringPlaceholders.builder("sender", sender.getName())
                .addPlaceholder("message", message)
                .addPlaceholder("prefix", this.messageConfig.getString("prefix")).build();

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("eternalsc.use"))
                .forEach(player -> {
                    this.sendSCMessage(player, stringPlaceholders);

                    if (ConfigManager.Setting.STAFFCHAT_SOUND_ENABLED.getBoolean()) {
                        player.playSound(player.getLocation(), Sound.valueOf(ConfigManager.Setting.STAFFCHAT_SOUND.getString()), ConfigManager.Setting.STAFFCHAT_SOUND_VOLUME.getInt(), 0);
                    }
                });
    }

    /**
     * Parse PlaceholderAPI Placeholders
     */
    private String parsePlaceholders(CommandSender sender, String message) {
        if (sender instanceof Player)
            return PlaceholderAPIHook.apply((Player) sender, message);
        return message;
    }


}
