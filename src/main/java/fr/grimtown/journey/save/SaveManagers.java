package fr.grimtown.journey.save;

import fr.grimtown.journey.save.events.Connection;
import fr.grimtown.journey.save.events.Death;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveManagers {
    public SaveManagers(JavaPlugin plugin) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new Connection(), plugin);
        pm.registerEvents(new Death(), plugin);
    }
}
