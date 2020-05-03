package xyz.oribuin.staffchat.bungee.managers;

import xyz.oribuin.staffchat.bungee.StaffChatBungee;

public abstract class Manager {

    protected final StaffChatBungee plugin;

    public Manager(StaffChatBungee plugin) {
        this.plugin = plugin;
    }

    public abstract void reload();
}
