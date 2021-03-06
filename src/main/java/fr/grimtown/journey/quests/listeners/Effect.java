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
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Effect implements Listener {
    private final Quest quest;
    private final PotionEffectType effectType;
    private final HashMap<BrewerInventory, ArrayList<Integer>> potions = new HashMap<>();
    public Effect(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            effectType = null;
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else {
            effectType = PotionEffectType.getByName(quest.getPayload().toUpperCase(Locale.ROOT));
            if (effectType!=null) QuestsUtils.questLoadLog(quest.getName(), effectType.toString());
            else {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerGetPotion(EntityPotionEffectEvent event) {
        if (event.getNewEffect()==null) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (effectType!=null && !event.getNewEffect().getType().equals(effectType)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        GameUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
