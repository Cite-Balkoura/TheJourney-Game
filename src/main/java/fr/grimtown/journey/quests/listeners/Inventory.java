package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Progression;
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
import java.util.Locale;
import java.util.stream.Collectors;

public class Inventory implements Listener {
    private final Quest quest;
    private Material material;
    private final ArrayList<Material> materials = new ArrayList<>();
    public Inventory(Quest quest) {
        this.quest = quest;
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
            if (quest.getCount() < 0) QuestsUtils.questLoadLog(quest.getName(), "Exclusive=" +
                    materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
            else QuestsUtils.questLoadLog(quest.getName(), "Whitelist=" +
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
        if (event.getEntity() instanceof Player) Bukkit.getScheduler().runTaskLaterAsynchronously(GamePlugin.getPlugin(),
                ()-> inventoryChangeEvent((Player) event.getEntity()),1);
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
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        if (quest.getCount()>0) {
            Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
                if (itemStack == null) return;
                if (material!=null && !itemStack.getType().equals(material)) return;
                if (material==null && !materials.contains(itemStack.getType())) return;
                ItemStack item = itemProcess(itemStack, QuestsUtils.getProgression(player.getUniqueId(), quest));
                if (item.getAmount() <= 0) itemStack.setType(Material.AIR);
                else itemStack.setAmount(item.getAmount());
            });
        } else {
            if (materials.stream().allMatch(material -> player.getInventory().contains(material, quest.getCount()*-1))) {
                QuestsUtils.getProgression(player.getUniqueId(), quest).setCompleted();
                Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
                    if (itemStack == null || !materials.contains(itemStack.getType())) return;
                    itemStack.setAmount(itemStack.getAmount() - quest.getCount()*-1);
                    if (itemStack.getAmount() <= 0) itemStack.setType(Material.AIR);
                    materials.remove(itemStack.getType());
                });
            }
        }
    }

    /**
     * Process completion increment and set the new amount of @item
     */
    private ItemStack itemProcess(ItemStack item, Progression progression) {
        if (item.getAmount()==1) {
            item.setAmount(0);
            progression.addProgress();
        } else while (item.getAmount()>=1 && quest.getCount() > progression.getProgress()) {
            progression.addProgress();
            item.setAmount(item.getAmount() - 1);
        }
        return item;
    }
}
