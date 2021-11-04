package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.GamePlugin;
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
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Inventory implements Listener {
    private final Quest quest;
    private Material material;
    private final ArrayList<Material> materials = new ArrayList<>();

    public Inventory(Quest quest) {
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
            else if (quest.getCount() < 0 && quest.getCount()*-1 < materials.size())
                QuestsUtils.questLoadLog(quest.getName(), "OneFrom=" +
                    materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
            else QuestsUtils.questLoadLog(quest.getName(), "Exclusive=" +
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
    public void onPlayerLoot(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) Bukkit.getScheduler().runTaskLaterAsynchronously(
                GamePlugin.getPlugin(), ()-> inventoryChangeEvent(player),1);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerClickInv(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) inventoryChangeEvent((Player) event.getWhoClicked());
    }

    @EventHandler (ignoreCancelled = true)
    public void onCloseInventory(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) inventoryChangeEvent((Player) event.getPlayer());
    }

    /**
     * Check inventory of player to check if quest item can be found on it
     */
    private void inventoryChangeEvent(Player player) {
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        Progression progression = GameUtils.getProgression(player.getUniqueId(), quest);
        if (quest.getCount() > 0) {
            if (material!=null && player.getInventory().contains(material)) {
                HashMap<Integer, ItemStack> removed = player.getInventory().removeItem(new ItemStack(material, quest.getCount()));
                if (removed.isEmpty()) progression.setCompleted();
                else IntStream.range(0, quest.getCount() - progression.getProgress() - removed.get(0).getAmount())
                            .forEach(ignored -> progression.addProgress());
            } else if (!materials.isEmpty() && materials.stream().anyMatch(material -> player.getInventory().contains(material))) {
                materials.forEach(material -> {
                    if (progression.isCompleted()) return;
                    HashMap<Integer, ItemStack> removed = player.getInventory().removeItem(new ItemStack(material, quest.getCount() - progression.getProgress()));
                    if (removed.isEmpty()) progression.setCompleted();
                    else IntStream.range(0, quest.getCount() - progression.getProgress() - removed.get(0).getAmount())
                                .forEach(ignored -> progression.addProgress());
                });
            }
        } else if (quest.getCount() < 0 && quest.getCount()*-1 < materials.size()) {
            int have = (int) materials.stream().filter(material -> player.getInventory().contains(material)).count();
            if (have >= quest.getCount()*-1) {
                progression.setCompleted();
                materials.forEach(material -> player.getInventory().removeItem(new ItemStack(material, 1)));
            }
        } else if (quest.getCount()==0) {
            if (materials.stream().allMatch(material -> player.getInventory().contains(material, 1))) {
                progression.setCompleted();
                materials.forEach(material -> player.getInventory().removeItem(new ItemStack(material, 1)));
            }
        } else if (quest.getCount()*-1 >= materials.size()) {
            if (materials.stream().allMatch(material -> player.getInventory().contains(material, quest.getCount()*-1))) {
                progression.setCompleted();
                materials.forEach(material -> player.getInventory().removeItem(new ItemStack(material, quest.getCount()*-1)));
            }
        }
    }
}
