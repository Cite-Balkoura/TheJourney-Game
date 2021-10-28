package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class Experience implements Listener {
    private final Quest quest;
    public Experience(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        QuestsUtils.questLoadLog(quest.getName(), "levels=" + quest.getCount());
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (event.getPlayer().getLevel() < quest.getCount()) return;
        if (GameUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).setCompleted();
    }
}
