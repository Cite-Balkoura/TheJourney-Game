package fr.grimtown.journey.quests;

import fr.grimtown.journey.quests.classes.Progression;
import fr.grimtown.journey.quests.classes.Quest;
import fr.grimtown.journey.quests.managers.ProgressionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class QuestsUtils {
    /**
     * Check if player has completed this quest (Based only on the cache progress)
     */
    public static boolean hasCompleted(UUID uuid, Quest quest) {
        if (!QuestsManager.playerProgression.containsKey(uuid))
            QuestsManager.playerProgression.put(uuid, ProgressionManager.getProgressions(uuid));
        Optional<Progression> optionalProgression = QuestsManager.playerProgression.get(uuid).stream()
                .filter(progression -> progression.getQuest().getId().equals(quest.getId())).findFirst();
        return optionalProgression.map(Progression::isCompleted).orElse(false);
    }

    /**
     * Get a quest Progression for player, if no completion exist yet, create it
     */
    public static Progression getProgression(UUID uuid, Quest quest) {
        if (!QuestsManager.playerProgression.containsKey(uuid))
            QuestsManager.playerProgression.put(uuid, ProgressionManager.getProgressions(uuid));
        Optional<Progression> optionalProgression = QuestsManager.playerProgression.get(uuid).stream()
                .filter(progression -> progression.getQuest().getId().equals(quest.getId())).findFirst();
        return optionalProgression.orElseGet(() -> new Progression(uuid, quest));
    }

    /**
     * Console log a new quest loaded, and indicate the payload
     */
    public static void questLoadLog(String questName, @Nullable String payload) {
        Bukkit.getLogger().info(String.format("Listeners for quest '%s' loaded with payload: {%s}.", questName, payload));
    }

    /**
     * Send an announcement when a player has complete a quest ?
     */
    public static void completionAnnounce(UUID uuid, Quest quest) {
        Player player = Bukkit.getPlayer(uuid);
        if (player!=null) Bukkit.getOnlinePlayers().forEach(player1 ->
                player1.sendMessage(player.getName() + " has gg quest : " + quest.getName())
        );
        // TODO: 16/10/2021 Do it ?
    }
}
