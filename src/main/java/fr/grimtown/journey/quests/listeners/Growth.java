package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class Growth implements Listener {
    private final Quest quest;
    private Material material;
    private final ArrayList<Material> materials = new ArrayList<>();

    public Growth(Quest quest) {
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
            QuestsUtils.questLoadLog(quest.getName(), "Whitelist=" +
                    materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
        } else {
            material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
            if (material != null) QuestsUtils.questLoadLog(quest.getName(), material.toString());
            else {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onBoneMeal(BlockFertilizeEvent event) {
        if (material!=null && event.getBlocks().stream().noneMatch(blockState -> blockState.getType().equals(material))) return;
        if (!materials.isEmpty() && event.getBlocks().stream().noneMatch(blockState -> materials.contains(blockState.getType()))) return;
        if (event.getPlayer()==null) return;
        if (GameUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).addProgress();
    }
}
