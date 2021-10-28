package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;

public class Brew implements Listener {
    private final Quest quest;
    private PotionEffectType effectType;
    private final ArrayList<PotionEffectType> effectTypes = new ArrayList<>();
    private final HashMap<BrewerInventory, ArrayList<Integer>> potions = new HashMap<>();

    public Brew(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else if (quest.getPayload().contains(",")) {
            Arrays.stream(quest.getPayload().split(",")).toList()
                    .forEach(materialType -> {
                        PotionEffectType effectType = PotionEffectType.getByName(materialType.toUpperCase(Locale.ROOT));
                        if (effectType!=null) effectTypes.add(effectType);
                        else {
                            Bukkit.getLogger().warning("Can't load: " + quest.getName());
                            HandlerList.unregisterAll(this);
                        }
                    });
            QuestsUtils.questLoadLog(quest.getName(), "Whitelist=" +
                    effectTypes.stream().map(PotionEffectType::getName).collect(Collectors.joining(",")));
        } else {
            effectType = PotionEffectType.getByName(quest.getPayload().toUpperCase(Locale.ROOT));
            if (effectType != null) QuestsUtils.questLoadLog(quest.getName(), effectType.toString());
            else {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onBrewingStandBrew(BrewEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(GamePlugin.getPlugin(),() -> {
            this.potions.remove(event.getContents());
            ArrayList<Integer> slots = new ArrayList<>();
            for (Integer slot : Arrays.asList(0,1,2)) {
                ItemStack potion = event.getContents().getItem(slot);
                if (potion==null) continue;
                if (effectType==null && effectTypes.isEmpty()) {
                    slots.add(slot);
                    continue;
                }
                if (!potion.hasItemMeta()) continue;
                PotionMeta meta = (PotionMeta) potion.getItemMeta();
                if (meta.getBasePotionData().getType().getEffectType()==null) continue;
                if (effectType!=null && meta.getBasePotionData().getType().getEffectType().equals(effectType)) slots.add(slot);
                else if (effectTypes.contains(meta.getBasePotionData().getType().getEffectType())) slots.add(slot);
            }
            if (!slots.isEmpty()) this.potions.put(event.getContents(), slots);
        }, 1);
    }

    @EventHandler (ignoreCancelled = true)
    public void onBrewingStandClick(InventoryClickEvent event) {
        if (event.getCurrentItem()==null) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getInventory().getType().equals(InventoryType.BREWING)) return;
        if (!potions.containsKey((BrewerInventory) event.getInventory())) return;
        if (!potions.get((BrewerInventory) event.getInventory()).contains(event.getSlot())) return;
        potions.get((BrewerInventory) event.getInventory()).remove((Integer) event.getSlot());
        if (potions.get((BrewerInventory) event.getInventory()).isEmpty()) potions.remove((BrewerInventory) event.getInventory());
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        GameUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
