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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Break implements Listener {
    private final Quest quest;
    private Material material;
    private final ArrayList<Material> materials = new ArrayList<>();
    private final HashMap<Player, LinkedList<Material>> mined = new HashMap<>();

    public Break(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else if (quest.getPayload().contains(",")) {
            Arrays.stream(quest.getPayload().split(",")).toList()
                    .forEach(materialType -> {
                        Material material = Material.getMaterial(materialType.toUpperCase(Locale.ROOT));
                        if (material!=null) materials.add(material);
                        else {
                            Bukkit.getLogger().warning("Can't load: " + quest.getName());
                            HandlerList.unregisterAll(this);
                        }
                    });
            if (quest.getCount() > 0) QuestsUtils.questLoadLog(quest.getName(), "Whitelist=" +
                    materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
            else if (quest.getCount() < 0 && quest.getCount()*-1 < materials.size())
                QuestsUtils.questLoadLog(quest.getName(), "OneFrom=" +
                        materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
            else QuestsUtils.questLoadLog(quest.getName(), "Exclusive=" +
                        materials.stream().map(Enum::toString).collect(Collectors.joining(",")));
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
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        if (!materials.isEmpty()) {
            LinkedList<Material> mined = new LinkedList<>();
            if (this.mined.containsKey(event.getPlayer())) mined.addAll(this.mined.get(event.getPlayer()));
            mined.add(event.getBlock().getType());
            this.mined.put(event.getPlayer(), mined);
        }
        if (material!=null && !event.getBlock().getType().equals(material)) return;
        if (GameUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        if (quest.getCount() > 0) {
            if (material!=null && event.getBlock().getType().equals(material)) {
                GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).addProgress();
            }
        } /*else if (quest.getCount() < 0 && quest.getCount()*-1 < materials.size()) {
            int done = (int) materials.stream().filter(material -> this.mined.get(event.getPlayer())).count();
            if (have >= quest.getCount()*-1) {
                GameUtils.getProgression(event.getPlayer().getUniqueId(), quest);
                materials.forEach(material -> player.getInventory().removeItem(new ItemStack(material, 1)));
            }
        }*/ else if (quest.getCount()==0) {
            if (materials.containsAll(this.mined.get(event.getPlayer()))) {
                GameUtils.getProgression(event.getPlayer().getUniqueId(), quest);
            }
        }/* else if (quest.getCount()*-1 >= materials.size()) {
            if (materials.stream().allMatch(material -> player.getInventory().contains(material, quest.getCount()*-1))) {
                GameUtils.getProgression(event.getPlayer().getUniqueId(), quest);
                materials.forEach(material -> player.getInventory().removeItem(new ItemStack(material, quest.getCount()*-1)));
            }
        }

        GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).addProgress();*/
    }
}
