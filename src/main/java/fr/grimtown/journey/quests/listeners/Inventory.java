package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Progression;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Locale;

public class Inventory implements Listener {
    private final Quest quest;
    private final Material material;// TODO: 16/10/2021 Multiples ? Hm
    public Inventory(Quest quest) {
        this.quest = quest;
        material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
        if (material!=null) QuestsUtils.questLoadLog(quest.getName(), material.toString());
        else HandlerList.unregisterAll(this);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerLoot(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!event.getItem().getItemStack().getType().equals(material)) return;
        if (QuestsUtils.hasCompleted(event.getEntity().getUniqueId(), quest)) return;
        ItemStack item = itemProcess(event.getItem().getItemStack(),
                QuestsUtils.getProgression(event.getEntity().getUniqueId(), quest));
        if (item.getAmount()<=0) event.getItem().remove();
        else event.getItem().setItemStack(item);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerClickInv(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) inventoryCheck((Player) event.getWhoClicked());
    }

    @EventHandler (ignoreCancelled = true)
    public void onCloseInventory(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) inventoryCheck((Player) event.getPlayer());
    }

    /**
     * Check inventory of player to check if quest item can be found on it
     */
    private void inventoryCheck(Player player) {
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack==null) return;
            if (!itemStack.getType().equals(material)) return;
            ItemStack item = itemProcess(itemStack,
                    QuestsUtils.getProgression(player.getUniqueId(), quest));
            if (item.getAmount()<=0) itemStack.setType(Material.AIR);
            else itemStack.setAmount(item.getAmount());
        });
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
