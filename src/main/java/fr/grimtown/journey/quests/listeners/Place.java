package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.*;
import java.util.stream.Collectors;

public class Place implements Listener {
    private final Quest quest;
    private Material material;
    private final Set<Material> materials = new HashSet<>();
    private final HashMap<Player, Set<Material>> mined = new HashMap<>();

    public Place(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else if (quest.getPayload().contains(",")) {
            Arrays.stream(quest.getPayload().split(",")).toList()
                    .forEach(materialType -> {
                        Material material = Material.getMaterial(materialType.toUpperCase(Locale.ROOT));
                        if (material!=null) materials.add(material);
                        else {
                            Bukkit.getLogger().warning("Can't load: " + quest.getName());
                            HandlerList.unregisterAll(this);
                        }
                    });
            if (quest.getCount() > 0) QuestsUtils.questLoadLog(quest.getName(), "Whitelist=" +
                    materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
            else QuestsUtils.questLoadLog(quest.getName(), "Exclusive=" +
                        materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
        } else {
            material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
            if (material != null) QuestsUtils.questLoadLog(quest.getName(), material.toString());   //  Single count
            else {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                HandlerList.unregisterAll(this);
            }
        }

    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        if (!materials.isEmpty()) { // TODO: 20/11/2021 test
            Set<Material> mined = this.mined.getOrDefault(event.getPlayer(), new HashSet<>());
            if (materials.contains(event.getBlock().getType())) mined.add(event.getBlock().getType());
            this.mined.put(event.getPlayer(), mined);
        }
        if (material!=null && !event.getBlock().getType().equals(material)) return;
        if (GameUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        if (materials.isEmpty() && quest.getCount() > 0) {  //  Single count
            GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).addProgress();
        } else if (!materials.isEmpty() && quest.getCount() > 0) {   //  White list
            if (materials.contains(event.getBlock().getType())) {
                GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).addProgress();
            }
        } else if (quest.getCount()==0) {   //  Exclusive list
            if (this.mined.get(event.getPlayer()).containsAll(materials)) {
                GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).setCompleted();
            }
        }
    }
}
