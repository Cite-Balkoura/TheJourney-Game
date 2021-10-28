package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class Sleep implements Listener {
    private final Quest quest;

    public Sleep(Quest quest) {
        this.quest = quest;
        QuestsUtils.questLoadLog(quest.getName(), "NONE");
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        if (!event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) return;
        if (QuestsUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        QuestsUtils.getProgression(event.getPlayer().getUniqueId(), quest).addProgress();
    }
}
