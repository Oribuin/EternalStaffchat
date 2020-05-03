package xyz.oribuin.staffchat.spigot.managers;

import xyz.oribuin.staffchat.spigot.StaffChatSpigot;

public abstract class Manager {

    protected final StaffChatSpigot plugin;

    public Manager(StaffChatSpigot plugin) {
        this.plugin = plugin;
    }

    public abstract void reload();
}
