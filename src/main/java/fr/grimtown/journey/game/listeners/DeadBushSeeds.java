package fr.grimtown.journey.game.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class DeadBushSeeds implements Listener {
    @EventHandler
    public void onBreakDeadBush(BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.DEAD_BUSH)) {
            if (new Random().nextBoolean())
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.WHEAT_SEEDS));
        }
    }
}
