package fr.grimtown.journey.game.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.managers.ProgressionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProgressionLoader implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        QuestsUtils.playerProgression.put(event.getPlayer().getUniqueId(),
                ProgressionManager.getProgressions(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        QuestsUtils.playerProgression.remove(event.getPlayer().getUniqueId());
    }
}
