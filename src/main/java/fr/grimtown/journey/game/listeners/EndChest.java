package fr.grimtown.journey.game.listeners;

import fr.grimtown.journey.game.utils.JourneyChest;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EndChest implements Listener {
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerOpenEndChest(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getClickedBlock()==null) return;
        if (event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
            event.setCancelled(true);
            new JourneyChest(event.getPlayer().getUniqueId()).open(event.getPlayer());
        }
    }
}
