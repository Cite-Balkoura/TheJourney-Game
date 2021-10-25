package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Progression;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Locale;

public class Smelt implements Listener {
    private final Quest quest;
    private final Material material;
    public Smelt(Quest quest) {
        this.quest = quest;
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            material = null;
        } else {
            material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
            if (material != null) QuestsUtils.questLoadLog(quest.getName(), material.toString());
            else HandlerList.unregisterAll(this);
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onFurnaceResultClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory()==null || !event.getClickedInventory().getType().equals(InventoryType.FURNACE)) return;
        if (event.getSlot()!=2 || event.getCurrentItem()==null) return;
        if (material!=null && !event.getCurrentItem().getType().equals(material)) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        if (!event.getAction().equals(InventoryAction.PICKUP_ALL) && !event.getAction().equals(InventoryAction.PICKUP_HALF)) {
            event.setCancelled(true);
            return;
        }
        Progression progression = QuestsUtils.getProgression(player.getUniqueId(), quest);
        int loop = event.getCurrentItem().getAmount();
        while (loop > 0 && quest.getCount() > progression.getProgress()) {
            progression.addProgress();
            loop--;
        }
    }
}
