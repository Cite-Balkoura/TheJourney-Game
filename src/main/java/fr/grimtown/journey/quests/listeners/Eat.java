package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.Locale;

public class Eat implements Listener {
    private final Quest quest;
    private final Material material;
    public Eat(Quest quest) {
        this.quest = quest;
        material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
        if (material!=null) QuestsUtils.questLoadLog(quest.getName(), material.toString());
        else HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerDeath(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getItem()==null || !event.getItem().getType().equals(material)) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        QuestsUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
