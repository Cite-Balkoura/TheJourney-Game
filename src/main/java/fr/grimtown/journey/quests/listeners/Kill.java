package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Locale;

public class Kill implements Listener {
    private final Quest quest;
    private EntityType type;
    public Kill(Quest quest) {
        this.quest = quest;
        try {
            type = EntityType.valueOf(quest.getPayload().toUpperCase(Locale.ROOT));
            QuestsUtils.questLoadLog(quest.getName(), type.toString());
        } catch (IllegalArgumentException ignore) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerMobKill(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) return;
        if (!event.getEntity().getType().equals(type)) return;
        if (event.getEntity().getKiller()==null) return;
        if (QuestsUtils.hasCompleted(event.getEntity().getKiller().getUniqueId(), quest)) return;
        QuestsUtils.getProgression(event.getEntity().getKiller().getUniqueId(), quest).addProgress();
    }
}
