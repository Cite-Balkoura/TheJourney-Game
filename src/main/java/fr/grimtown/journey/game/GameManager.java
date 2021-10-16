package fr.grimtown.journey.game;

import fr.grimtown.journey.game.listeners.ProgressionLoader;
import fr.grimtown.journey.game.listeners.RemoveJoinMsg;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GameManager {
    public GameManager(JavaPlugin plugin) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new RemoveJoinMsg(), plugin);
        pm.registerEvents(new ProgressionLoader(), plugin);
    }
}
