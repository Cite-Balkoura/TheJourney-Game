package fr.grimtown.journey.data.managers;

import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.data.classes.DataConnection;
import fr.grimtown.journey.data.classes.DataDeath;
import fr.grimtown.journey.data.classes.SavedInventory;
import fr.grimtown.journey.data.events.Connection;

import java.util.ArrayList;
import java.util.UUID;

public class DataManager {
    private static final Datastore DATASTORE = GamePlugin.getDatastore(GamePlugin.getEvent().getDatabase());

    /**
     * Get all deaths for this UUID of player
     */
    public static ArrayList<DataDeath> getDeaths(UUID uuid) {
        return new ArrayList<>(DATASTORE.find(DataDeath.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator().toList());
    }

    /**
     * Get all connections for this UUID of player
     */
    public static ArrayList<Connection> getConnections(UUID uuid) {
        return new ArrayList<>(DATASTORE.find(Connection.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator().toList());
    }

    /**
     * Get the last DataDeath of uuid
     */
    public static DataDeath getLastDeath(UUID uuid) {
        return DATASTORE.find(DataDeath.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator(new FindOptions().sort(Sort.descending("_id")).limit(1)).tryNext();
    }

    /**
     * Get the last DataConnection of uuid
     */
    public static DataConnection getLastConnection(UUID uuid) {
        return DATASTORE.find(DataConnection.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator(new FindOptions().sort(Sort.descending("_id")).limit(1)).tryNext();
    }

    /**
     * Get the last SavedInventory of uuid
     */
    public static SavedInventory getLastInventory(UUID uuid) {
        DataDeath dataDeath = getLastDeath(uuid);
        DataConnection dataConnection = getLastConnection(uuid);
        if (dataDeath.getDate().after(dataConnection.getDate())) return dataDeath.getSavedInventory();
        else return dataConnection.getSavedInventory();
    }

    /**
     * Save a new player Death Data
     */
    public static void save(DataDeath dataDeath) {
        DATASTORE.save(dataDeath);
    }

    /**
     * Save a new player Connection Data
     */
    public static void save(DataConnection dataConnection) {
        DATASTORE.save(dataConnection);
    }
}

