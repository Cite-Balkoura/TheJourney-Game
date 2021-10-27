package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
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
    // TODO: 28/10/2021 Multiples
    private Material material;
    private final ArrayList<Material> materials = new ArrayList<>();
    private final HashMap<Player, LinkedList<Material>> eaten = new HashMap<>();

    public Eat(Quest quest) {
        this.quest = quest;
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else if (quest.getPayload().contains(",")) {
            Arrays.stream(quest.getPayload().split(",")).toList()
                    .forEach(materialType -> {
                        Material material = Material.getMaterial(materialType.toUpperCase(Locale.ROOT));
                        if (material!=null) materials.add(material);
                        else HandlerList.unregisterAll(this);
                    });
            if (quest.getCount() < 0) QuestsUtils.questLoadLog(quest.getName(), "Exclusive=" +
                    materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
            else QuestsUtils.questLoadLog(quest.getName(), "Whitelist=" +
                    materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
        } else {
            material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
            if (material != null) QuestsUtils.questLoadLog(quest.getName(), material.toString());
            else HandlerList.unregisterAll(this);
        }
    }

    @EventHandler (ignoreCancelled = true)// TODO: 28/10/2021 Test it
    public void onPlayerEat(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getItem()==null) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        if (quest.getCount()>0) {
            if (material!=null && !event.getItem().getType().equals(material)) return;
            if (material==null && !materials.contains(event.getItem().getType())) return;
            QuestsUtils.getProgression(player.getUniqueId(), quest).addProgress();
        } else {
            LinkedList<Material> foods = eaten.getOrDefault(player, new LinkedList<>());
            if (materials.contains(event.getItem().getType())) foods.add(event.getItem().getType());
            if (foods.size()==materials.size()) {
                QuestsUtils.getProgression(player.getUniqueId(), quest).setCompleted();
                eaten.remove(player);
            } else eaten.put(player, foods);
        }
    }
}
