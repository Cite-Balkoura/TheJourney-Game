package fr.grimtown.journey.game.utils;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.classes.DataPlayer;
import fr.grimtown.journey.game.listeners.PlayerSpawn;
import fr.grimtown.journey.game.managers.DataPlayerManager;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JourneyChest extends FastInv {
    private final int baseSlots = GamePlugin.getConfigs().getInt("game.journey-chest.base-slots");
    private final DataPlayer dataPlayer;
    public JourneyChest(UUID questsPlayer) {
        super(GamePlugin.getConfigs().getInt("game.journey-chest.size"),
                GamePlugin.getConfigs().getString("game.journey-chest.title"));
        setItems(0, getInventory().getSize()-1, new ItemBuilder(Material.BARRIER)
                .name(GamePlugin.getConfigs().getString("game.journey-chest.locked")).build(),
                event -> event.setCancelled(true));
        dataPlayer = DataPlayerManager.getDataPlayer(questsPlayer);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        setItems(0, baseSlots + dataPlayer.getBonusCompleted() - 1, null);
        for (Map.Entry<Integer, ItemStack> loop : dataPlayer.getJourneyChest().entrySet()) {
            if (loop.getKey() <= (baseSlots + dataPlayer.getBonusCompleted() - 1)) setItem(loop.getKey(), loop.getValue());
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(false);
        ItemStack item = event.getCurrentItem();
        if (item!=null) {
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                @SuppressWarnings("deprecation")
                List<String> lore = item.getItemMeta().getLore();
                if (lore != null && lore.stream().anyMatch(line -> line.contains(PlayerSpawn.lore))) event.setCancelled(true);
            }
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(GamePlugin.getPlugin(), ()-> saveInv(event.getInventory()), 1);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        saveInv(event.getInventory());
    }

    private void saveInv(Inventory inventory) {
        HashMap<Integer, ItemStack> storage = new HashMap<>();
        for (int i = 0; i < inventory.getContents().length; i++) {
            if (inventory.getContents()[i] !=null && inventory.getContents()[i].getType().equals(Material.BARRIER)) continue;
            storage.put(i, inventory.getContents()[i]);
        }
        dataPlayer.setJourneyChest(storage);
        DataPlayerManager.updateJourneyChest(dataPlayer);
    }
}
