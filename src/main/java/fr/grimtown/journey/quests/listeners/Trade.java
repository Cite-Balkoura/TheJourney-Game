package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class Trade implements Listener {
    private final Quest quest;
    public Trade(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        QuestsUtils.questLoadLog(quest.getName(), "NONE");
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerTradeVillager(InventoryClickEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.MERCHANT)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getSlot()!=2) return;
        if (event.getCurrentItem()==null) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        GameUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
