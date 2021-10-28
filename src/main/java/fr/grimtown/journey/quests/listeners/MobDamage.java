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
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Locale;

public class MobDamage implements Listener {
    private final Quest quest;
    private EntityType entity;
    public MobDamage(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else {
            try {
                entity = EntityType.valueOf(quest.getPayload().toUpperCase(Locale.ROOT));
                QuestsUtils.questLoadLog(quest.getName(), entity.toString());
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                exception.printStackTrace();
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerDamagedByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (entity!=null && !event.getDamager().getType().equals(entity)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        GameUtils.getProgression(player.getUniqueId(), quest).setCompleted();
    }
}
