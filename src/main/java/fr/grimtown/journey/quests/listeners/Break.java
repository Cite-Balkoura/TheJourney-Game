package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Locale;

public class Break implements Listener {
    private final Quest quest;
    private Material material;

    public Break(Quest quest) {
        this.quest = quest;
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
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        if (material!=null && !event.getBlock().getType().equals(material)) return;
        if (QuestsUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        QuestsUtils.getProgression(event.getPlayer().getUniqueId(), quest).addProgress();
    }
}
