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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Locale;

public class Repair implements Listener {
    private final Quest quest;
    private Material material;

    public Repair(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else {
            material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
            if (material != null) {
                QuestsUtils.questLoadLog(quest.getName(), material.toString());
            } else {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerRepair(InventoryClickEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.ANVIL)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getSlot()!=2) return;
        if (event.getInventory().getItem(0)==null || event.getCurrentItem()==null) return;
        ItemStack baseItem = event.getInventory().getItem(0);
        if (baseItem==null || (material!=null && !baseItem.getType().equals(material))) return;
        if (!baseItem.hasItemMeta() || !event.getCurrentItem().hasItemMeta()) return;
        Damageable baseDmg = (Damageable) baseItem.getItemMeta();
        Damageable repairedDmg = (Damageable) event.getCurrentItem().getItemMeta();
        if (repairedDmg.getDamage() >= baseDmg.getDamage()) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        GameUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
