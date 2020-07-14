package xyz.oribuin.staffchat.spigot;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.oribuin.staffchat.spigot.commands.CmdStaffchat;
import xyz.oribuin.staffchat.spigot.commands.CmdToggle;
import xyz.oribuin.staffchat.spigot.hook.Metrics;
import xyz.oribuin.staffchat.spigot.listeners.PlayerChat;
import xyz.oribuin.staffchat.spigot.managers.CommandManager;
import xyz.oribuin.staffchat.spigot.managers.ConfigManager;
import xyz.oribuin.staffchat.spigot.managers.MessageManager;
import xyz.oribuin.staffchat.spigot.utils.OriCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaffChatSpigot extends JavaPlugin {

    private static StaffChatSpigot instance;
    public List<UUID> toggleList = new ArrayList<>();
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


        // Register commands
        this.registerCommands(new CmdStaffchat(), new CmdToggle());

        // Register Listeners
        this.registerListeners(new PlayerChat(this));

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

    public MessageManager getMessageManager() {
        return this.messageManager;
    }


    private void registerCommands(OriCommand... commands) {
        for (OriCommand cmd : commands) {
            cmd.registerCommand();
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}
