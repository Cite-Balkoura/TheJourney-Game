package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.stream.Collectors;

public class StandOn implements Listener {
    private final Quest quest;
    private final LinkedList<Material> blocks = new LinkedList<>();

    public StandOn(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().contains(",")) {
            Arrays.stream(quest.getPayload().split(",")).toList()
                    .forEach(materialType -> {
                        Material material = Material.getMaterial(materialType.toUpperCase(Locale.ROOT));
                        if (material!=null) blocks.add(material);
                        else {
                            Bukkit.getLogger().warning("Can't load: " + quest.getName());
                            HandlerList.unregisterAll(this);
                        }
                    });
            QuestsUtils.questLoadLog(quest.getName(),
                    blocks.stream().map(Enum::toString).collect(Collectors.joining(",")));
        } else {
            Material material = Material.getMaterial(quest.getPayload());
            if (material!=null) {
                blocks.clear();
                blocks.add(material);
                QuestsUtils.questLoadLog(quest.getName(), material.toString());
            } else {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerStandOn(PlayerMoveEvent event) {
        for (int index = 0; index < blocks.size(); index++) {
            Location loc = event.getTo().clone();
            loc.setY(loc.getY()-(index+1));
            if (!loc.getBlock().getType().equals(blocks.get(index))) return;
        }
        if (GameUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).addProgress();
    }
}
