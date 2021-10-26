package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Locale;

public class Shoot implements Listener {
    private final Quest quest;
    private EntityType type;
    public Shoot(Quest quest) {
        this.quest = quest;
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            type = null;
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else {
            try {
                type = EntityType.valueOf(quest.getPayload().toUpperCase(Locale.ROOT));
                QuestsUtils.questLoadLog(quest.getName(), type.toString());
            } catch (IllegalArgumentException ignore) {
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler
    public void onPlayerShoot(EntityDeathEvent event) {
        if (event.getEntity().getLastDamageCause()==null) return;
        if (event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) return;
        if (type!=null && !event.getEntity().getType().equals(type)) return;
        if (event.getEntity().getKiller()==null) return;
        if (QuestsUtils.hasCompleted(event.getEntity().getKiller().getUniqueId(), quest)) return;
        QuestsUtils.getProgression(event.getEntity().getKiller().getUniqueId(), quest).addProgress();
    }
}
