package fr.grimtown.journey.game.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.classes.Progression;

import java.util.ArrayList;
import java.util.UUID;

public class ProgressionManager {
    private static final Datastore DATASTORE = GamePlugin.getDatastore(GamePlugin.getEvent().getDatabase());

    /**
     * Get all quest progressed for this UUID of player
     */
    public static ArrayList<Progression> getProgressions(UUID uuid) {
        return new ArrayList<>(DATASTORE.find(Progression.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator().toList());
    }

    /**
     * Save a new Progression or save an existing if finished
     */
    public static void save(Progression progression) {
        DATASTORE.save(progression);
    }
}
