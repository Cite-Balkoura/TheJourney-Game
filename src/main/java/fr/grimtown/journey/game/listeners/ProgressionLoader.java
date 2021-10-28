package fr.grimtown.journey.game.listeners;

import fr.grimtown.journey.game.GameManager;
import fr.grimtown.journey.game.managers.ProgressionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record ProgressionLoader(GameManager gameManager) implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        gameManager.getPlayerProgression().put(event.getPlayer().getUniqueId(),
                ProgressionManager.getProgressions(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        gameManager.getPlayerProgression().remove(event.getPlayer().getUniqueId());
    }
}
