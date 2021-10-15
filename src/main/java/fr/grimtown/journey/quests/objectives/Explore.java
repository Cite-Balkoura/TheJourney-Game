package fr.grimtown.journey.quests.objectives;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public record Explore(Quest quest) implements Listener {
    public Explore(Quest quest) {
        this.quest = quest;
        Bukkit.getLogger().info("Quest listeners for quest '" + quest.getName() + "' loaded.");
    }

    @EventHandler
    public void onExplore(PlayerMoveEvent event) throws NoSuchMethodException {
        if (QuestsUtils.hasCompleted(quest, event.getPlayer().getUniqueId())) return;
        // TODO: 15/10/2021 Check StructureType parsing
        Location structure = event.getTo().getWorld().locateNearestStructure(event.getTo(),
                (StructureType) StructureType.class.getDeclaredMethod(quest.getPayload()).getDefaultValue(),
                50, false);
        if (structure==null) return;
        // TODO: 15/10/2021 Validate quest

    }
}
