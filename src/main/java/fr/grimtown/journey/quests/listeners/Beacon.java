package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.BrewerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Beacon implements Listener {
    private final Quest quest;
    private EntityPotionEffectEvent.Cause cause;
    private final HashMap<BrewerInventory, ArrayList<Integer>> potions = new HashMap<>();
    public Beacon(Quest quest) {
        this.quest = quest;
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            cause = null;
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else {
            try {
                cause = EntityPotionEffectEvent.Cause.valueOf(quest.getPayload().toUpperCase(Locale.ROOT));
                QuestsUtils.questLoadLog(quest.getName(), cause.toString());
            } catch (IllegalArgumentException ignore) {
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler
    public void onPlayerGetPotion(EntityPotionEffectEvent event) {
        if (event.getNewEffect()==null) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (cause==null && !(event.getCause().equals(EntityPotionEffectEvent.Cause.BEACON) ||
                event.getCause().equals(EntityPotionEffectEvent.Cause.CONDUIT))) return;
        if (cause!=null && !event.getCause().equals(cause)) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        QuestsUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
