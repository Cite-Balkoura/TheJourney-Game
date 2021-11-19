package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
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

    // TODO: 14/11/2021 Test async
    @EventHandler (ignoreCancelled = true)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(GamePlugin.getPlugin(), ()-> {
            if (event.getPlayer().getLevel() < quest.getCount()) return;
            if (GameUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
            GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).setCompleted();
        },1);
    }
}
