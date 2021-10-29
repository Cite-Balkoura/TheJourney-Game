package fr.grimtown.journey.game.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.classes.DataPlayer;
import fr.grimtown.journey.game.classes.Universe;

import java.util.UUID;

public class DataPlayerManager {
    private static final Datastore DATASTORE = GamePlugin.getDatastore(GamePlugin.getEvent().getDatabase());

    /**
     * Get game data of player by his lastNameSeen
     */
    public static DataPlayer getDataPlayer(String lastNameSeen) {
        return DATASTORE.find(DataPlayer.class)
                .filter(Filters.eq("lastNameSeen", lastNameSeen))
                .first();
    }

    /**
     * Update the universe of uuid
     */
    public static void updateUsername(UUID uuid, Universe universe) {
        DATASTORE.find(DataPlayer.class)
                .filter(Filters.eq("uuid", uuid))
                .update(UpdateOperators.set("universe", universe))
                .execute();
    }
}
