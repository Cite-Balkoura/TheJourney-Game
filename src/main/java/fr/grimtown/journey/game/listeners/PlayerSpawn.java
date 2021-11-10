package fr.grimtown.journey.game.listeners;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.utils.SpawnPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawn implements Listener {
    public PlayerSpawn() {
        Bukkit.getWorlds().forEach(world -> world.setGameRule(GameRule.KEEP_INVENTORY, true));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        Bukkit.getPluginManager().registerEvents(new SpawnPlayer(event.getEntity(), false), GamePlugin.getPlugin());
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) Bukkit.getPluginManager().registerEvents(new SpawnPlayer(event.getPlayer(), true), GamePlugin.getPlugin());
    }
}
