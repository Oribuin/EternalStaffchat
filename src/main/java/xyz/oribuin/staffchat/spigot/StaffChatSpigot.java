package xyz.oribuin.staffchat.spigot;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.oribuin.staffchat.spigot.commands.CmdStaffchat;
import xyz.oribuin.staffchat.spigot.commands.CmdToggle;
import xyz.oribuin.staffchat.spigot.hook.Metrics;
import xyz.oribuin.staffchat.spigot.listeners.PlayerChat;
import xyz.oribuin.staffchat.spigot.managers.CommandManager;
import xyz.oribuin.staffchat.spigot.managers.ConfigManager;
import xyz.oribuin.staffchat.spigot.managers.MessageManager;
import xyz.oribuin.staffchat.spigot.utils.StringPlaceholders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaffChatSpigot extends JavaPlugin {

    public List<UUID> toggleList = new ArrayList<>();
    private static StaffChatSpigot instance;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private MessageManager messageManager;

    public static StaffChatSpigot getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getConsoleSender().sendMessage("[EternalEco] No PlaceholderAPI, Placeholders will not work.");
        }

        // Register Commands
        getCommand("staffchat").setExecutor(new CmdStaffchat());
        getCommand("sctoggle").setExecutor(new CmdToggle());

        // Register Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);

        // Register Managers
        this.commandManager = new CommandManager(this);
        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.reload();

        if (ConfigManager.Setting.USE_METRICS.getBoolean()) {
            new Metrics(this, 7391);
        }
    }

    public void reload() {
        this.commandManager.reload();
        this.configManager.reload();
        this.messageManager.reload();
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public MessageManager getMessageManager() {
        return this.messageManager;
    }

    public void sendSc(CommandSender sender, String message) {
        StringPlaceholders stringPlaceholders = StringPlaceholders.builder("sender", sender.getName())
                .addPlaceholder("message", message).build();

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("eternalsc.use"))
                .forEach(player -> {
                    messageManager.sendSCMessage(player, stringPlaceholders);

                    if (ConfigManager.Setting.STAFFCHAT_SOUND_ENABLED.getBoolean()) {
                        player.playSound(player.getLocation(), Sound.valueOf(ConfigManager.Setting.STAFFCHAT_SOUND.getString()), ConfigManager.Setting.STAFFCHAT_SOUND_VOLUME.getInt(), 0);
                    }
                });
    }
}
