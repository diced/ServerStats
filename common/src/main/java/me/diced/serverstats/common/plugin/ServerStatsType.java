package me.diced.serverstats.common.plugin;

public enum ServerStatsType {
    BUKKIT,
    FABRIC,
    BUNGEE,
    VELOCITY;

    public String toString() {
        if (this == FABRIC) return "Fabric";
        else if (this == BUKKIT) return "Bukkit";
        else if (this == BUNGEE) return "Bungee";
        else if (this == VELOCITY) return "Velocity";
        else return "Bukkit";
    }

    public boolean isProxy() {
        return this == VELOCITY || this == BUNGEE;
    }
}