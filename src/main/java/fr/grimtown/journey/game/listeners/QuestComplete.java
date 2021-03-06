package fr.grimtown.journey.game.listeners;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameManager;
import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.game.classes.DataPlayer;
import fr.grimtown.journey.game.events.PlayerQuestComplete;
import fr.grimtown.journey.game.managers.DataPlayerManager;
import fr.grimtown.journey.game.managers.ProgressionManager;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public record QuestComplete(GameManager gameManager) implements Listener {
    /**
     * Send an announcement when a player has complete a quest ?
     */
    @EventHandler
    public void onPlayerQuestComplete(PlayerQuestComplete event) {
        if (event.getQuest().notBonus()) {
            event.getPlayer().sendMessage(GamePlugin.getConfigs().getString("game.prefix") +
                    Objects.requireNonNull(GamePlugin.getConfigs().getString("game.messages.complete-quest"))
                            .replaceAll("<QUEST_NAME>", event.getQuest().getName()));
            Bukkit.getLogger().info("Player: " + event.getPlayer().getName() + " has made quest " + event.getQuest().getName());
            int doneQuests = GameUtils.getPlayerQuests(event.getPlayer().getUniqueId(), GamePlugin.getUniverse());
            int totalQuests = GamePlugin.getManager().getLoadedQuests().stream().filter(Quest::notBonus).toList().size();
            event.getPlayer().sendMessage(GamePlugin.getConfigs().getString("game.prefix") +
                    Objects.requireNonNull(GamePlugin.getConfigs().getString("game.messages.quests-count"))
                            .replaceAll("<DONE>", String.valueOf(doneQuests))
                            .replaceAll("<COUNT>", String.valueOf(totalQuests)));
        } else {
            event.getPlayer().sendMessage(GamePlugin.getConfigs().getString("game.prefix") +
                    Objects.requireNonNull(GamePlugin.getConfigs().getString("game.messages.complete-bonus"))
                            .replaceAll("<QUEST_NAME>", event.getQuest().getName()));
            Bukkit.getLogger().info("Player: " + event.getPlayer().getName() + " has made bonus " + event.getQuest().getName());
            DataPlayer dataPlayer = DataPlayerManager.getDataPlayer(event.getPlayer().getUniqueId());
            dataPlayer.setBonusCompleted(Math.toIntExact(ProgressionManager.getProgressions(event.getPlayer().getUniqueId())
                    .stream().filter(progression -> progression.getQuest().isBonus()).count()));
            DataPlayerManager.updateBonus(dataPlayer);
            int doneBonus = GameUtils.getPlayerBonus(event.getPlayer().getUniqueId(), GamePlugin.getUniverse());
            int totalBonus = GamePlugin.getManager().getLoadedQuests().stream().filter(Quest::isBonus).toList().size();
            event.getPlayer().sendMessage(GamePlugin.getConfigs().getString("game.prefix") +
                    Objects.requireNonNull(GamePlugin.getConfigs().getString("game.messages.bonus-count"))
                            .replaceAll("<DONE>", String.valueOf(doneBonus))
                            .replaceAll("<COUNT>", String.valueOf(totalBonus)));
        }
        if (GameUtils.hasFinishUniverse(event.getPlayer().getUniqueId(), GamePlugin.getUniverse())) {
            event.getPlayer().sendMessage(GamePlugin.getConfigs().getString("game.prefix") +
                    GamePlugin.getConfigs().getString("game.messages.all-completed"));
            event.getPlayer().sendMessage(Objects.requireNonNull(
                    GamePlugin.getConfigs().getString("game.messages.universe-switch-warning")));
        }
    }
}
