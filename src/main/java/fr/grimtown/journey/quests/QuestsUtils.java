package fr.grimtown.journey.quests;

import fr.grimtown.journey.quests.classes.Completion;
import fr.grimtown.journey.quests.classes.Quest;
import fr.grimtown.journey.quests.managers.CompletionManager;

import java.util.Optional;
import java.util.UUID;

public class QuestsUtils {
    /**
     * Check if player has completed this quest (Based only on the cache progress)
     */
    public static boolean hasCompleted(Quest quest, UUID uuid) {
        if (!QuestsManager.playerCompletion.containsKey(uuid))
            QuestsManager.playerCompletion.put(uuid, CompletionManager.getCompletions(uuid));
        Optional<Completion> optionalCompletion = QuestsManager.playerCompletion.get(uuid).stream()
                .filter(completion -> completion.getQuest().equals(quest)).findFirst();
        if (optionalCompletion.isPresent()) {
            Completion completion = optionalCompletion.get();
            return completion.getProgress()>=quest.getCount();
        } else return false;
    }
}
