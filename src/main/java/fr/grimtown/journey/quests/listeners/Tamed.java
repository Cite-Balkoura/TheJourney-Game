package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

public class Tamed implements Listener {
    private final Quest quest;
    private EntityType entity;

    public Tamed(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        try {
            entity = EntityType.valueOf(quest.getPayload());
            QuestsUtils.questLoadLog(quest.getName(), quest.getPayload());
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("Can't load: " + quest.getName());
            exception.printStackTrace();
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerTame(EntityTameEvent event) {
        if (!(event.getOwner() instanceof Player player)) return;
        if (!event.getEntity().getType().equals(entity)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        GameUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
