package fr.grimtown.journey.game.listeners;

import fr.grimtown.journey.quests.QuestsManager;
import fr.grimtown.journey.quests.managers.ProgressionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProgressionLoader implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        QuestsManager.playerProgression.put(event.getPlayer().getUniqueId(),
                ProgressionManager.getProgressions(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        QuestsManager.playerProgression.remove(event.getPlayer().getUniqueId());
    }
}
