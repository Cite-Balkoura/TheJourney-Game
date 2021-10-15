package fr.grimtown.journey.quests.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.quests.classes.Completion;
import fr.grimtown.journey.quests.classes.Quest;

import java.util.ArrayList;
import java.util.UUID;

public class CompletionManager {
    private static final Datastore DATASTORE = GamePlugin.getDatastore(GamePlugin.getEvent().getDatabase());

    /**
     * Get all quests completed for this UUID of player
     */
    public static ArrayList<Completion> getCompletions(UUID uuid) {
        return new ArrayList<>(DATASTORE.find(Completion.class)
                .filter(Filters.eq("uuid", uuid))
                .iterator().toList());
    }

    /**
     * Get the completion of quest for this uuid
     */
    public static Completion hasStart(UUID uuid, Quest quest) {
        return DATASTORE.find(Completion.class)
                .filter(Filters.eq("uuid", uuid))
                .filter(Filters.eq("quest", quest))// TODO: 15/10/2021 Check that
                .first();
    }

    /**
     * Save a new completion or save an existing if finished
     */
    public static void save(Completion completion) {
        DATASTORE.save(completion);
    }

    /**
     * Update the completion for a quest for a player
     */
    public static void updateProgression(Completion completion) {
        DATASTORE.find(Completion.class)
                .filter(Filters.eq("_id", completion.getId()))
                .update(UpdateOperators.set("progress", completion.getProgress()))
                .execute();
    }
}
