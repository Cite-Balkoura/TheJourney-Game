package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.game.classes.Progression;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
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
    private final Material material;
    public Craft(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            material = null;
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
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
    public void onPlayerCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (material!=null && !event.getRecipe().getResult().getType().equals(material)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        if (!event.getAction().equals(InventoryAction.PICKUP_ALL) && !event.getAction().equals(InventoryAction.PICKUP_HALF)) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("§cNo shift click please.");
            return;
        }
        Progression progression = GameUtils.getProgression(player.getUniqueId(), quest);
        int loop = event.getRecipe().getResult().getAmount();
        while (loop > 0 && quest.getCount() > progression.getProgress()) {
            progression.addProgress();
            loop--;
        }
    }
}
