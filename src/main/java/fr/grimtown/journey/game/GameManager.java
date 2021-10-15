package fr.grimtown.journey.game;

import fr.grimtown.journey.game.core.listeners.JoinMsg;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GameManager {
    public GameManager(JavaPlugin plugin) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new JoinMsg(), plugin);
    }
}
