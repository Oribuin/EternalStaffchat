package xyz.oribuin.staffchat.bungee.utils;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;

public class FileUtils {

    public static void createFile(Plugin plugin, String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        try {

            if (!configFile.getParentFile().exists())
                configFile.getParentFile().mkdir();

            if (!configFile.exists()) {
                configFile.createNewFile();
                ;
                try (InputStream inputStream = plugin.getResourceAsStream(fileName);
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(inputStream, os);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
