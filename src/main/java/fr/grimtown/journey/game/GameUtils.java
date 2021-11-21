package fr.grimtown.journey.game;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.classes.Progression;
import fr.grimtown.journey.game.classes.Universe;
import fr.grimtown.journey.game.managers.ProgressionManager;
import fr.grimtown.journey.quests.classes.Quest;
import fr.grimtown.journey.quests.managers.QuestManager;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class GameUtils {
    /**
     * Get a quest from this universe (Need to be loaded)
     */
    public static Quest getQuest(String questName) {
        return GamePlugin.getManager().getLoadedQuests().stream()
                .filter(quest -> quest.getName().equalsIgnoreCase(questName))
                .findFirst().orElse(null);
    }

    /**
     * Get Progressions of loaded quests for uuid, create them if no exist
     */
    public static ArrayList<Progression> getProgressions(UUID uuid) {
        if (!GamePlugin.getManager().getPlayerProgression().containsKey(uuid)) {
            ArrayList<Progression> progression = ProgressionManager.getProgressions(uuid);
            GamePlugin.getManager().getPlayerProgression().put(uuid, progression);
        }
        return GamePlugin.getManager().getPlayerProgression().get(uuid);
    }

    /**
     * Get Progressions of quests from specific universe for uuid, create them if no exist
     */
    public static ArrayList<Progression> getProgressions(UUID uuid, Universe universe) {
        return new ArrayList<>(ProgressionManager.getProgressions(uuid).stream()
                .filter(loop -> loop.getQuest().getUniverse().equals(universe)).toList());
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
     * Check if player has completed this quest (Based only on the cache progress)
     */
    public static boolean hasCompleted(UUID uuid, Quest quest, Universe universe) {
        Optional<Progression> optionalProgression = getProgressions(uuid, universe).stream()
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
     * Get all completed quests for this uuid
     */
    public static int getPlayerQuests(UUID uuid) {
        return getProgressions(uuid).stream().filter(Progression::isCompleted)
                .filter(progression -> progression.getQuest().notBonus()).toList().size();
    }

    /**
     * Get all completed quests for this uuid in universe
     */
    public static int getPlayerQuests(UUID uuid, Universe universe) {
        return getProgressions(uuid).stream().filter(Progression::isCompleted)
                .filter(progression -> progression.getQuest().getUniverse().equals(universe))
                .filter(progression -> progression.getQuest().notBonus()).toList().size();
    }

    /**
     * Get all completed bonus for this uuid
     */
    public static int getPlayerBonus(UUID uuid) {
        return getProgressions(uuid).stream().filter(Progression::isCompleted)
                .filter(progression -> progression.getQuest().isBonus()).toList().size();
    }

    /**
     * Get all completed bonus for this uuid in universe
     */
    public static int getPlayerBonus(UUID uuid, Universe universe) {
        return getProgressions(uuid).stream().filter(Progression::isCompleted)
                .filter(progression -> progression.getQuest().getUniverse().equals(universe))
                .filter(progression -> progression.getQuest().isBonus()).toList().size();
    }

    /**
     * Get all completed quests and bonus for this uuid
     */
    public static int getPlayerFinished(UUID uuid) {
        return getProgressions(uuid).stream().filter(Progression::isCompleted).toList().size();
    }

    /**
     * Check if uuid has done all quests
     */
    public static boolean hasFinishUniverse(UUID uuid, Universe universe) {
        return getProgressions(uuid).stream().filter(Progression::isCompleted)
                .filter(progression -> progression.getQuest().notBonus())
                .filter(progression -> progression.getQuest().getUniverse().equals(universe)).toList().size()
                == QuestManager.getQuests(universe).stream().filter(Quest::notBonus).toList().size();
    }

    /**
     * Reset a progression for uuid
     */
    public static void reset(UUID uuid, Progression progression) {
        ArrayList<Progression> progressions = GamePlugin.getManager().getPlayerProgression().get(uuid);
        if (progressions!=null) progressions.remove(progression);
        ProgressionManager.reset(progression);
    }
}
