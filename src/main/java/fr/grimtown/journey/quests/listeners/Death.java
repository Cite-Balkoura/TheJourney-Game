package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Locale;

public class Death implements Listener {
    private final Quest quest;
    private EntityDamageEvent.DamageCause damageCause;
    public Death(Quest quest) {
        this.quest = quest;
        try {
            damageCause = EntityDamageEvent.DamageCause.valueOf(quest.getPayload().toUpperCase(Locale.ROOT));
            QuestsUtils.questLoadLog(quest.getName(), damageCause.toString());
        } catch (IllegalArgumentException ignore) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getLastDamageCause()==null) return;
        if (!event.getEntity().getLastDamageCause().getCause().equals(damageCause)) return;
        if (QuestsUtils.hasCompleted(event.getEntity().getUniqueId(), quest)) return;
        QuestsUtils.getProgression(event.getEntity().getUniqueId(), quest).addProgress();
    }
}
