package fr.grimtown.journey.game.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class LavaGenerator implements Listener {
    @EventHandler
    public void onLavaGenerate(PlayerInteractEvent event) {
        if (event.getItem()==null || !event.getMaterial().equals(Material.COBBLESTONE)) return;
        if (event.getClickedBlock()==null) return;
        if (!event.getClickedBlock().getType().equals(Material.CAULDRON)) return;
        Location fireLoc = event.getClickedBlock().getLocation().clone().add(0,-1,0);
        if (!fireLoc.getBlock().getType().equals(Material.FIRE)) return;
        event.setCancelled(true);
        event.getItem().setAmount(event.getItem().getAmount()-1);
        event.getClickedBlock().setType(Material.LAVA_CAULDRON);
    }
}
