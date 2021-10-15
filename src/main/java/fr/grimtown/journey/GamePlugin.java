package fr.grimtown.journey;

import dev.morphia.Datastore;
import fr.grimtown.journey.game.GameManager;
import fr.grimtown.journey.quests.QuestsManager;
import fr.grimtown.journey.quests.classes.Event;
import fr.grimtown.journey.quests.managers.EventsManager;
import fr.grimtown.journey.utils.MongoDB;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class GamePlugin extends JavaPlugin {
    /* Configs */
    public static boolean DEBUG_ERRORS = true;
    /* MongoDB */
    private static HashMap<String, Datastore> datastoreMap = new HashMap<>();
    /* Event */
    private static Event mcEvent;
    private static String universe;

    @Override
    public void onEnable() {
        /* MongoDB */
        datastoreMap = MongoDB.getDatastoreMap(this.getConfig());
        /* Event load */
        universe = this.getConfig().getString("game.universe");
        mcEvent = EventsManager.getEvent(this.getConfig().getString("data.event-name"));
        new GameManager(this);
        new QuestsManager(this);
    }

    /**
     * Get loaded Event shortcut
     */
    public static Event getEvent() { return mcEvent; }

    public static String getUniverse() {
        return universe;
    }

    /**
     * MongoDB Connection (Morphia Datastore) to query
     */
    public static Datastore getDatastore(String dbName) {
        return datastoreMap.get(dbName).startSession();
    }
}
