package fr.grimtown.journey.game.events;

import fr.grimtown.journey.game.classes.Progression;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerQuestComplete extends Event {
    private final Player player;
    private final Quest quest;
    private final Progression progression;

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerQuestComplete(Player player, Quest quest, Progression progression) {
        this.player = player;
        this.quest = quest;
        this.progression = progression;
    }

    public Player getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }

    public Progression getProgression() {
        return progression;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
