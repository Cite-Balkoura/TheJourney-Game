package fr.grimtown.journey.quests.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.classes.Universe;
import fr.grimtown.journey.quests.classes.Quest;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class QuestManager {
    private static final Datastore DATASTORE = GamePlugin.getDatastore(GamePlugin.getEvent().getDatabase());

    /**
     * Get all quests from this Universe
     */
    public static ArrayList<Quest> getQuests() {
        return new ArrayList<>(DATASTORE.find(Quest.class)
                .filter(Filters.eq("universe", GamePlugin.getUniverse()))
                .iterator().toList());
    }

    /**
     * Get all quests from this Universe
     */
    public static Quest getQuest(ObjectId id) {
        return DATASTORE.find(Quest.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    /**
     * Get all quests from this Universe
     */
    public static ArrayList<Quest> getQuests(Universe universe) {
        return new ArrayList<>(DATASTORE.find(Quest.class)
                .filter(Filters.eq("universe", universe))
                .iterator().toList());
    }
}
