package xyz.oribuin.staffchat.bungee;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
import xyz.oribuin.staffchat.bungee.utils.StringPlaceholders;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Quick Note:
 * <p>
 * bungeecord plugins are ass
 */
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

        createConfig("config.yml");
        createConfig("messages.yml");

        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.reload();

        this.registerCommands(new CmdStaffchat(), new CmdAdmin(), new CmdToggle());
        this.getProxy().getPluginManager().registerListener(this, new PlayerChat());
    }

    private void registerCommands(Command... commands) {
        for (Command cmd : commands) {
            this.getProxy().getPluginManager().registerCommand(this, cmd);
        }
    }

    public void sendSc(CommandSender sender, String message) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer pplayer = (ProxiedPlayer) sender;
            StringPlaceholders stringPlaceholders = StringPlaceholders.builder("sender", sender.getName())
                    .addPlaceholder("message", message)
                    .addPlaceholder("server", pplayer.getServer().getInfo().getName()).build();

            ProxyServer.getInstance().getPlayers().stream()
                    .filter(player -> player.hasPermission("eternalsc.use"))
                    .forEach(player -> messageManager.sendSCMessage(player, stringPlaceholders));
        } else {

            StringPlaceholders stringPlaceholders = StringPlaceholders.builder("sender", sender.getName())
                    .addPlaceholder("message", message)
                    .addPlaceholder("server", "None").build();

            ProxyServer.getInstance().getPlayers().stream()
                    .filter(player -> player.hasPermission("eternalsc.use"))
                    .forEach(player -> messageManager.sendSCMessage(player, stringPlaceholders));
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

    public void createConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        try {

            if (!configFile.getParentFile().exists())
                configFile.getParentFile().mkdir();

            if (!configFile.exists()) {
                configFile.createNewFile();
                ;
                try (InputStream inputStream = getResourceAsStream(fileName);
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(inputStream, os);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
