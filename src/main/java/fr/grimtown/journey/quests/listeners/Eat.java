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
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.*;
import java.util.stream.Collectors;

public class Eat implements Listener {
    private final Quest quest;
    private Material material;
    private final Set<Material> materials = new HashSet<>();
    private final HashMap<Player, Set<Material>> eaten = new HashMap<>();

    public Eat(Quest quest) {
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
                            Bukkit.getLogger().warning("Can't load material: " + materialType);
                            HandlerList.unregisterAll(this);
                        }
                    });
            if (quest.getCount() > 0) QuestsUtils.questLoadLog(quest.getName(), "Whitelist=" +
                    materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
            else if (quest.getCount() == 0) QuestsUtils.questLoadLog(quest.getName(), "Exclusive=" +
                    materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
            else if (quest.getCount() < 0) {
                Bukkit.getLogger().warning("Only Whitelist or Exclusive work: " + quest.getName());
                HandlerList.unregisterAll(this);
            }
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
    public void onPlayerEat(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getItem()==null) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        if (quest.getCount()>0) {   //  Single and Whitelist
            if (material!=null && !event.getItem().getType().equals(material)) return;
            if (material==null && !materials.contains(event.getItem().getType())) return;
            GameUtils.getProgression(player.getUniqueId(), quest).addProgress();
        } else if (quest.getCount()==0) {   //  Exclusive
            Set<Material> foods = eaten.getOrDefault(player, new HashSet<>());
            foods.add(event.getItem().getType());
            eaten.put(player, foods);
            if (foods.containsAll(materials)) {
                GameUtils.getProgression(player.getUniqueId(), quest).setCompleted();
                eaten.remove(player);
            }
        }
    }
}
