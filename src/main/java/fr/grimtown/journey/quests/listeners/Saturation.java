package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Saturation implements Listener {
    private final Quest quest;
    public Saturation(Quest quest) {
        this.quest = quest;
        QuestsUtils.questLoadLog(quest.getName(), quest.getPayload() + " than " +  quest.getCount());
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerDeath(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(quest.getPayload().equalsIgnoreCase(">") && event.getFoodLevel() > quest.getCount()) &&
                !(quest.getPayload().equalsIgnoreCase("<") && event.getFoodLevel() < quest.getCount())) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        QuestsUtils.getProgression(player.getUniqueId(), quest).setCompleted();
    }
}
