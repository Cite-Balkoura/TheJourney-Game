package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import java.util.Locale;

public class Liquid implements Listener {
    private final Quest quest;
    private final Material material;
    public Liquid(Quest quest) {
        this.quest = quest;
        material = Material.getMaterial(quest.getPayload().toUpperCase(Locale.ROOT));
        if (material != null) QuestsUtils.questLoadLog(quest.getName(), material.toString());
        else HandlerList.unregisterAll(this);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerNetherWater(PlayerBucketEmptyEvent event) {
        if (!event.getPlayer().getWorld().getName().endsWith("_nether")) return;
        if (!material.equals(Material.WATER_BUCKET) || !event.getBucket().equals(material)) return;
        if (QuestsUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        QuestsUtils.getProgression(event.getPlayer().getUniqueId(), quest).setCompleted();
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerTakeLava(PlayerBucketFillEvent event) {
        if (!material.equals(Material.LAVA) || !event.getBlockClicked().getType().equals(material)) return;
        if (QuestsUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        QuestsUtils.getProgression(event.getPlayer().getUniqueId(), quest).setCompleted();
    }
}
