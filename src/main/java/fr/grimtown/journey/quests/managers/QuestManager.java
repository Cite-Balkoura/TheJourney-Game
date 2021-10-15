package fr.grimtown.journey.quests.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.quests.classes.Quest;

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
}
