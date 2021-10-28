package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class Equip implements Listener {
    private final Quest quest;
    private Material helmet = Material.BARRIER;
    private Material chest = Material.BARRIER;
    private Material leggings = Material.BARRIER;
    private Material boots = Material.BARRIER;

    public Equip(Quest quest) {
        this.quest = quest;
        ArrayList<String> materialList = new ArrayList<>(Arrays.stream(quest.getPayload().split(",")).toList());
        if (materialList.get(0)!=null) helmet = Material.getMaterial(materialList.get(0));
        else {
            Bukkit.getLogger().warning("Can't load: " + quest.getName());
            HandlerList.unregisterAll(this);
        }
        if (materialList.get(1)!=null) chest = Material.getMaterial(materialList.get(1));
        if (materialList.get(2)!=null) leggings = Material.getMaterial(materialList.get(2));
        if (materialList.get(3)!=null) boots = Material.getMaterial(materialList.get(3));
        QuestsUtils.questLoadLog(quest.getName(), quest.getPayload());
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerEquip(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (QuestsUtils.hasCompleted(player.getUniqueId(), quest)) return;
        if (helmet==null && player.getEquipment().getHelmet()==null) return;
        else if (helmet!=null && !(player.getEquipment().getHelmet()!=null &&
                player.getEquipment().getHelmet().getType().equals(helmet))) return;
        if (chest==null && player.getEquipment().getChestplate()==null) return;
        else if (chest!=null && !(player.getEquipment().getChestplate()!=null &&
                player.getEquipment().getChestplate().getType().equals(chest))) return;
        if (leggings==null && player.getEquipment().getLeggings()==null) return;
        else if (leggings!=null && !(player.getEquipment().getLeggings()!=null &&
                player.getEquipment().getLeggings().getType().equals(leggings))) return;
        if (boots==null && player.getEquipment().getBoots()==null) return;
        else if (boots!=null && !(player.getEquipment().getBoots()!=null &&
                player.getEquipment().getBoots().getType().equals(boots))) return;
        QuestsUtils.getProgression(player.getUniqueId(), quest).setCompleted();
    }
}
