package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;

import java.util.Locale;

public class Craft implements Listener {
    private final Quest quest;
    private final Material material;// TODO: 16/10/2021 Multiples
    public Craft(Quest quest) {
        this.quest = quest;
        material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
        if (material!=null) QuestsUtils.questLoadLog(quest.getName(), material.toString());
        else HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerMobKill(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getRecipe().getResult().getType().equals(material)) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        if (!event.getAction().equals(InventoryAction.PICKUP_ALL) && !event.getAction().equals(InventoryAction.PICKUP_HALF)) {
            event.getWhoClicked().sendMessage(event.getAction().toString());
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            return;
        }
        for (int i = 0; i < event.getRecipe().getResult().getAmount(); i++)
            QuestsUtils.getProgression(player.getUniqueId(), quest).addProgress();
    }
}
