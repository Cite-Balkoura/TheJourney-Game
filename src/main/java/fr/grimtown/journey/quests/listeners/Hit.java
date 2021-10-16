package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Locale;

public class Hit implements Listener {
    private final Quest quest;
    private EntityType type;
    public Hit(Quest quest) {
        this.quest = quest;
        try {
            type = EntityType.valueOf(quest.getPayload().toUpperCase(Locale.ROOT));
            QuestsUtils.questLoadLog(quest.getName(), type.toString());
        } catch (IllegalArgumentException ignore) {
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public void onMonsterDamage(EntityDamageByEntityEvent event) {
        if (!event.getEntity().getType().equals(type)) return;
        if (!(event.getDamager() instanceof Player player)) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        QuestsUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
