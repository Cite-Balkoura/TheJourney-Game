package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.game.classes.Progression;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class Smelt implements Listener {
    private final Quest quest;
    private Material material;
    private final ArrayList<Material> materials = new ArrayList<>();

    public Smelt(Quest quest) {
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
    public void onFurnaceResultClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory()==null || !event.getClickedInventory().getType().equals(InventoryType.FURNACE)) return;
        if (event.getSlot()!=2 || event.getCurrentItem()==null) return;
        if (material!=null && !event.getCurrentItem().getType().equals(material)) return;
        if (material==null && !materials.isEmpty() && !materials.contains(event.getCurrentItem().getType())) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        if (!event.getAction().equals(InventoryAction.PICKUP_ALL) && !event.getAction().equals(InventoryAction.PICKUP_HALF)) {
            event.setCancelled(true);
            return;
        }
        Progression progression = GameUtils.getProgression(player.getUniqueId(), quest);
        int loop = event.getCurrentItem().getAmount();
        while (loop > 0 && quest.getCount() > progression.getProgress()) {
            progression.addProgress();
            loop--;
        }
    }
}
