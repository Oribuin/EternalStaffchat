package xyz.oribuin.staffchat.bungee;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.oribuin.staffchat.bungee.managers.ConfigManager;
import xyz.oribuin.staffchat.bungee.managers.MessageManager;
import xyz.oribuin.staffchat.bungee.utils.StringPlaceholders;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Quick Note:
 *
 * bungeecord plugins are ass
 */
public class StaffChatBungee extends Plugin {

    public List<UUID> toggleList = new ArrayList<>();
    private static StaffChatBungee instance;
    private ConfigManager configManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.reload();

        createConfig("config.yml");
        createConfig("messages.yml");
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

    public static StaffChatBungee getInstance() {
        return instance;
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

    public void saveConfig(Plugin plugin, String fileName) {
        if (plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                InputStream inputStream = this.getResourceAsStream(fileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                ByteStreams.copy(inputStream, outputStream);

                Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        if (configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream inputStream = getResourceAsStream(fileName);
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(inputStream, os);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
