package fr.grimtown.journey.game.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.classes.Progression;
import fr.grimtown.journey.quests.classes.Quest;

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
     * Get the completion of quest for this uuid
     */
    public static Progression hasStart(UUID uuid, Quest quest) {
        return DATASTORE.find(Progression.class)
                .filter(Filters.eq("uuid", uuid))
                .filter(Filters.eq("quest", quest))// TODO: 15/10/2021 [TEST] Check that
                .first();
    }

    /**
     * Save a new Progression or save an existing if finished
     */
    public static void save(Progression progression) {
        DATASTORE.save(progression);
    }

    /**
     * Update the completion for a quest for a player
     */
    public static void updateProgression(Progression progression) {
        DATASTORE.find(Progression.class)
                .filter(Filters.eq("_id", progression.getId()))
                .update(UpdateOperators.set("progress", progression.getProgress()))
                .execute();
    }
}
