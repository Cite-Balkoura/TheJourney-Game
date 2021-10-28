package fr.grimtown.journey.game;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.classes.Progression;
import fr.grimtown.journey.game.managers.ProgressionManager;
import fr.grimtown.journey.quests.classes.Quest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class GameUtils {
    /**
     * Get Progressions of uuid, create them if no exist
     */
    public static ArrayList<Progression> getProgressions(UUID uuid) {
        if (!GamePlugin.getManager().getPlayerProgression().containsKey(uuid)) {
            GamePlugin.getManager().getPlayerProgression().put(uuid, ProgressionManager.getProgressions(uuid));
        }
        return GamePlugin.getManager().getPlayerProgression().get(uuid);
    }

    /**
     * Check if player has completed this quest (Based only on the cache progress)
     */
    public static boolean hasCompleted(UUID uuid, Quest quest) {
        Optional<Progression> optionalProgression = getProgressions(uuid).stream()
                .filter(progression -> progression.getQuest().getId().equals(quest.getId())).findFirst();
        return optionalProgression.map(Progression::isCompleted).orElse(false);
    }

    /**
     * Get a quest Progression for player, if no completion exist yet, create it
     */
    public static Progression getProgression(UUID uuid, Quest quest) {
        Optional<Progression> optionalProgression = getProgressions(uuid).stream()
                .filter(progression -> progression.getQuest().getId().equals(quest.getId())).findFirst();
        return optionalProgression.orElseGet(() -> new Progression(uuid, quest));
    }

    /**
     * Check if uuid has done all quests
     */
    public static boolean hasFinishQuests(UUID uuid) {
        return getProgressions(uuid).stream().filter(Progression::isCompleted)
                .filter(progression -> progression.getQuest().notBonus()).toList().size() ==
                GamePlugin.getManager().getLoadedQuests().stream().filter(Quest::notBonus).toList().size();
    }
}
