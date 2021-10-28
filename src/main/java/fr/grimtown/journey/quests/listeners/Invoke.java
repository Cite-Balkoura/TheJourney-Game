package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

public class Invoke implements Listener {
    private final Quest quest;
    private CreatureSpawnEvent.SpawnReason reason;
    private final HashMap<Player, Location> lastBlock = new HashMap<>();
    public Invoke(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            reason = null;
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else {
            try {
                reason = CreatureSpawnEvent.SpawnReason.valueOf(quest.getPayload().toUpperCase(Locale.ROOT));
                QuestsUtils.questLoadLog(quest.getName(), reason.toString());
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                exception.printStackTrace();
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        lastBlock.put(event.getPlayer(), event.getBlock().getLocation());
        Bukkit.getScheduler().runTaskLaterAsynchronously(GamePlugin.getPlugin(), ()-> {
            if (lastBlock.get(event.getPlayer()).equals(event.getBlock().getLocation())) lastBlock.remove(event.getPlayer());
        }, 10);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerInvoke(CreatureSpawnEvent event) {
        if (!event.getSpawnReason().toString().contains("BUILD_")) return;
        if (reason!=null && !event.getSpawnReason().equals(reason)) return;
        Collection<Player> placeNear = new ArrayList<>();
        lastBlock.forEach((key, value) -> {
            if (event.getLocation().distance(value) < 3) placeNear.add(key);
        });
        Collection<Player> nearby = event.getLocation().getNearbyPlayers(10);
        nearby.retainAll(placeNear);
        nearby.forEach(player -> {
            if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
            GameUtils.getProgression(player.getUniqueId(), quest).addProgress();
        });
    }
}
