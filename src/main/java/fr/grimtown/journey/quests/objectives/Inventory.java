package fr.grimtown.journey.quests.objectives;

import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public record Inventory(Quest quest) implements Listener {
    @EventHandler
    public void onPlayerLoot(EntityPickupItemEvent event) {
        //event.getEntity().getLocation().getWorld().locateNearestStructure()
    }
}
