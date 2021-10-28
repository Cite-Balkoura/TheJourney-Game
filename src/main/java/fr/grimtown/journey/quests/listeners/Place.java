package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Locale;

public class Place implements Listener {
    private final Quest quest;
    private Material material;
    public Place(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else {
            material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
            if (material != null) QuestsUtils.questLoadLog(quest.getName(), material.toString());
            else {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        if (material!=null && !event.getBlock().getType().equals(material)) return;
        if (GameUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).addProgress();
    }
}
