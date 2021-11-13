package fr.grimtown.journey;

import dev.morphia.Datastore;
import fr.grimtown.journey.game.GameManager;
import fr.grimtown.journey.game.classes.Event;
import fr.grimtown.journey.game.classes.Universe;
import fr.grimtown.journey.game.managers.EventsManager;
import fr.grimtown.journey.quests.QuestsManager;
import fr.grimtown.journey.utils.MongoDB;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class GamePlugin extends JavaPlugin {
    /* Core */
    private static Plugin plugin;
    /* Configs */
    public static boolean DEBUG_ERRORS = true;
    /* MongoDB */
    private static HashMap<String, Datastore> datastoreMap = new HashMap<>();
    /* Event */
    private static Event mcEvent;
    private static Universe universe;
    private static GameManager manager;

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        FastInvManager.register(this);
        /* MongoDB */
        datastoreMap = MongoDB.getDatastoreMap(getConfigs());
        /* Event load */
        universe = Universe.valueOf(getConfigs().getString("game.universe"));
        Bukkit.getLogger().info("Loaded universe: " + universe);
        mcEvent = EventsManager.getEvent(getConfigs().getString("data.event-name"));
        Bukkit.getLogger().info("Loaded event: " + mcEvent.getName());
        manager = new GameManager(this);
        Bukkit.getScheduler().runTaskAsynchronously(this, ()-> new QuestsManager(this, manager));
    }

    /**
     * Get plugin
     */
    public static Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get config
     */
    @NotNull
    public static FileConfiguration getConfigs() {
        return plugin.getConfig();
    }

    /**
     * Get loaded Event shortcut
     */
    public static Event getEvent() { return mcEvent; }

    /**
     * Get loaded Universe shortcut
     */
    public static Universe getUniverse() {
        return universe;
    }

    /**
     * Get the current game manager
     */
    public static GameManager getManager() {
        return manager;
    }

    /**
     * MongoDB Connection (Morphia Datastore) to query
     */
    public static Datastore getDatastore(String dbName) {
        return datastoreMap.get(dbName).startSession();
    }
}
