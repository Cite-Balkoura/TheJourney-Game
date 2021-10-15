package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsManager;
import fr.grimtown.journey.quests.managers.CompletionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProgressionLoader implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        QuestsManager.playerCompletion.put(event.getPlayer().getUniqueId(),
                CompletionManager.getCompletions(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        QuestsManager.playerCompletion.remove(event.getPlayer().getUniqueId());
    }
}
