package xyz.oribuin.staffchat.bungee.managers;

import net.md_5.bungee.config.Configuration;
import xyz.oribuin.staffchat.bungee.StaffChatBungee;

import java.util.List;

public class ConfigManager extends Manager {

    public ConfigManager(StaffChatBungee plugin) {
        super(plugin);
        this.reload();
    }

    @Override
    public void reload() {
        this.plugin.reloadConfig();
        this.plugin.createConfig("config.yml");

        Configuration config = this.plugin.getConfig("config.yml");
        for (ConfigManager.Setting value : ConfigManager.Setting.values())
            value.load(config);
    }

    public enum Setting {
        USE_METRICS("bstats-metrics"),

        // Staffchat Text
        STAFFCHAT_FORMAT("staffchat.format"),

        // Staffchat Shortcuts
        SHORTCUTS_ENABLED("staffchat.shortcut.enabled"),
        SHORTCUTS("staffchat.shortcut.shortcuts"),

        // Staffchat Sounds
        STAFFCHAT_SOUND_ENABLED("staffchat.sound.enabled"),
        STAFFCHAT_SOUND("staffchat.sound.sound"),
        STAFFCHAT_SOUND_VOLUME("staffchat.sound.volume");

        private final String key;
        private Object value = null;

        Setting(String key) {
            this.key = key;
        }

        /**
         * Gets the setting as a boolean
         *
         * @return The setting as a boolean
         */
        public boolean getBoolean() {
            return (boolean) this.value;
        }

        /**
         * @return the setting as an int
         */
        public int getInt() {
            return (int) this.getNumber();
        }

        /**
         * @return the setting as a long
         */
        public long getLong() {
            return (long) this.getNumber();
        }

        /**
         * @return the setting as a double
         */
        public double getDouble() {
            return this.getNumber();
        }

        /**
         * @return the setting as a float
         */
        public float getFloat() {
            return (float) this.getNumber();
        }

        /**
         * @return the setting as a String
         */
        public String getString() {
            return (String) this.value;
        }

        private double getNumber() {
            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            }

            return (double) this.value;
        }

        /**
         * @return the setting as a string list
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            return (List<String>) this.value;
        }

        /**
         * Loads the value from the config and caches it
         */
        private void load(Configuration config) {
            this.value = config.get(this.key);
        }

    }

}