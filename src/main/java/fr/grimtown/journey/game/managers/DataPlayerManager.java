package fr.grimtown.journey.game.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.classes.DataPlayer;

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
     * Get game data of player by his lastNameSeen
     */
    public static DataPlayer getDataPlayer(UUID uuid) {
        return DATASTORE.find(DataPlayer.class)
                .filter(Filters.eq("uuid", uuid))
                .first();
    }

    /**
     * Update bonus count of uuid
     */
    public static void updateBonus(DataPlayer dataPlayer) {
        DATASTORE.find(DataPlayer.class)
                .filter(Filters.eq("uuid", dataPlayer.getUuid()))
                .update(UpdateOperators.set("bonusCompleted", dataPlayer.getBonusCompleted()))
                .execute();
    }

    /**
     * Update the journey chest content of uuid
     */
    public static void updateJourneyChest(DataPlayer dataPlayer) {
        DATASTORE.find(DataPlayer.class)
                .filter(Filters.eq("uuid", dataPlayer.getUuid()))
                .update(UpdateOperators.set("journeyChest", dataPlayer.getJourneyChestB64()))
                .execute();
    }
}
