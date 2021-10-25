package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

public class Breed implements Listener {
    private final Quest quest;
    private EntityType entity;

    public Breed(Quest quest) {
        this.quest = quest;
        try {
            entity = EntityType.valueOf(quest.getPayload());
            QuestsUtils.questLoadLog(quest.getName(), quest.getPayload());
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerBreed(EntityBreedEvent event) {
        if (!(event.getBreeder() instanceof Player player)) return;
        if (!(event.getFather().getType().equals(entity) && event.getMother().getType().equals(entity))) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        QuestsUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
