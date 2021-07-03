package me.diced.serverstats.common;

public enum ServerStatsType {
    BUKKIT,
    FABRIC,
    BUNGEE;

    public String toString() {
        if (this == FABRIC) return "Fabric";
        else if (this == BUKKIT) return "Fabric";
        else if (this == BUNGEE) return "Bungee";
        else return "Bukkit";
    }
}