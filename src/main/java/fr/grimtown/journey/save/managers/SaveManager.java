package fr.grimtown.journey.save.managers;

import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.save.classes.ConnectionSave;
import fr.grimtown.journey.save.classes.DeathSave;
import fr.grimtown.journey.save.classes.SavedInventory;

import java.util.ArrayList;
import java.util.UUID;

public class SaveManager {
    private static final Datastore DATASTORE = GamePlugin.getDatastore(GamePlugin.getEvent().getDatabase());

    /**
     * Get all deaths for this UUID of player
     */
    public static ArrayList<DeathSave> getDeaths(UUID uuid) {
        return new ArrayList<>(DATASTORE.find(DeathSave.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator().toList());
    }

    /**
     * Get all connections for this UUID of player
     */
    public static ArrayList<ConnectionSave> getConnections(UUID uuid) {
        return new ArrayList<>(DATASTORE.find(ConnectionSave.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator().toList());
    }

    /**
     * Get the last DataDeath of uuid
     */
    public static DeathSave getLastDeath(UUID uuid) {
        return DATASTORE.find(DeathSave.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator(new FindOptions().sort(Sort.descending("_id")).limit(1)).tryNext();
    }

    /**
     * Get the last DataConnection of uuid
     */
    public static ConnectionSave getLastConnection(UUID uuid) {
        return DATASTORE.find(ConnectionSave.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator(new FindOptions().sort(Sort.descending("_id")).limit(1)).tryNext();
    }

    /**
     * Get the last SavedInventory of uuid
     */
    public static SavedInventory getLastInventory(UUID uuid) {
        DeathSave deathSave = getLastDeath(uuid);
        ConnectionSave connectionSave = getLastConnection(uuid);
        if (deathSave.getDate().after(connectionSave.getDate())) return deathSave.getSavedInventory();
        else return connectionSave.getSavedInventory();
    }

    /**
     * Save a new player Death Data
     */
    public static void save(DeathSave deathSave) {
        DATASTORE.save(deathSave);
    }

    /**
     * Save a new player Connection Data
     */
    public static void save(ConnectionSave connectionSave) {
        DATASTORE.save(connectionSave);
    }
}

