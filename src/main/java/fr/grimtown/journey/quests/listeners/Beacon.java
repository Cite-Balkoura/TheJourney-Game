package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;

import java.util.Locale;

public class Beacon implements Listener {
    private final Quest quest;
    private EntityPotionEffectEvent.Cause cause;

    public Beacon(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else {
            try {
                cause = EntityPotionEffectEvent.Cause.valueOf(quest.getPayload().toUpperCase(Locale.ROOT));
                QuestsUtils.questLoadLog(quest.getName(), cause.toString());
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                exception.printStackTrace();
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerGetPotion(EntityPotionEffectEvent event) {
        if (event.getNewEffect()==null) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (cause==null && !(event.getCause().equals(EntityPotionEffectEvent.Cause.BEACON) ||
                event.getCause().equals(EntityPotionEffectEvent.Cause.CONDUIT))) return;
        if (cause!=null && !event.getCause().equals(cause)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        GameUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
