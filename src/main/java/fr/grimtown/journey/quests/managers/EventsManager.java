package fr.grimtown.journey.quests.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.quests.classes.Event;

public class EventsManager {
    private static final Datastore DATASTORE = GamePlugin.getDatastore("master");

    /**
     * Get an Event by his name
     */
    public static Event getEvent(String eventName) {
        return DATASTORE.find(Event.class)
                .filter(Filters.eq("name", eventName))
                .first();
    }
}
