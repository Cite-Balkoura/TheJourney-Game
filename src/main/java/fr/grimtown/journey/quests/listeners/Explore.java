package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Locale;

public class Explore implements Listener {
    private final Quest quest;
    private StructureType questStructure = null;
    private World questWorld = null;
    public Explore(Quest quest) {
        this.quest = quest;
        Bukkit.getServer().getWorlds().forEach(world -> {
            if (world.getName().equals(quest.getPayload())) questWorld = world;
        });
        if (StructureType.getStructureTypes().containsKey(quest.getPayload().toLowerCase(Locale.ROOT)))
            questStructure = StructureType.getStructureTypes().get(quest.getPayload().toLowerCase(Locale.ROOT));
        if (questWorld!=null) QuestsUtils.questLoadLog(quest.getName(), "world=" + questWorld.getName());
        else if (questStructure!=null) QuestsUtils.questLoadLog(quest.getName(), "structure=" + questStructure.getName());
        else HandlerList.unregisterAll(this);
    }

    @EventHandler (ignoreCancelled = true)
    public void onStructureExplore(PlayerMoveEvent event) {
        if (questStructure==null) return;
        if (QuestsUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        Location structure = event.getTo().getWorld()
                .locateNearestStructure(event.getTo(), questStructure, 1, false);
        if (structure==null || !event.getPlayer().getChunk().equals(structure.getChunk())) return;
        QuestsUtils.getProgression(event.getPlayer().getUniqueId(), quest).setCompleted();
    }

    @EventHandler (ignoreCancelled = true)
    public void onWorldExplore(PlayerMoveEvent event) {
        if (questWorld==null) return;
        if (QuestsUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        if (event.getTo().getWorld().equals(questWorld))
            QuestsUtils.getProgression(event.getPlayer().getUniqueId(), quest).setCompleted();
    }
}
