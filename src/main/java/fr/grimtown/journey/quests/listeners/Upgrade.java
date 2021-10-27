package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Locale;

public class Upgrade implements Listener {
    private final Quest quest;
    private final Material material;// TODO: 16/10/2021 Multiples
    public Upgrade(Quest quest) {
        this.quest = quest;
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            material = null;
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else {
            material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
            if (material != null) QuestsUtils.questLoadLog(quest.getName(), material.toString());
            else HandlerList.unregisterAll(this);
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerUpgrade(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory()==null || !event.getClickedInventory().getType().equals(InventoryType.SMITHING)) return;
        if (event.getSlot()!=2 || event.getCurrentItem()==null) return;
        if (material!=null && !event.getCurrentItem().getType().equals(material)) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        QuestsUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
