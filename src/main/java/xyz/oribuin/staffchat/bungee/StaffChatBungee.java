package xyz.oribuin.staffchat.bungee;

import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.oribuin.staffchat.bungee.commands.CmdAdmin;
import xyz.oribuin.staffchat.bungee.commands.CmdStaffchat;
import xyz.oribuin.staffchat.bungee.commands.CmdToggle;
import xyz.oribuin.staffchat.bungee.listeners.PlayerChat;
import xyz.oribuin.staffchat.bungee.managers.ConfigManager;
import xyz.oribuin.staffchat.bungee.managers.MessageManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class StaffChatBungee extends Plugin {

    private static StaffChatBungee instance;
    public List<UUID> toggleList = new ArrayList<>();
    private ConfigManager configManager;
    private MessageManager messageManager;

    public static StaffChatBungee getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.reload();

        this.registerCommands(new CmdStaffchat(this), new CmdAdmin(this), new CmdToggle(this));
        this.getProxy().getPluginManager().registerListener(this, new PlayerChat(this));
    }

    private void registerCommands(Command... commands) {
        for (Command cmd : commands) {
            this.getProxy().getPluginManager().registerCommand(this, cmd);
        }
    }

    public void reload() {
        this.configManager.reload();
        this.messageManager.reload();
    }

    public xyz.oribuin.staffchat.bungee.managers.ConfigManager getConfigManager() {
        return this.configManager;
    }

    public xyz.oribuin.staffchat.bungee.managers.MessageManager getMessageManager() {
        return this.messageManager;
    }

    public Configuration getConfig(String fileName) {
        Configuration config = null;
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    public void reloadConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
