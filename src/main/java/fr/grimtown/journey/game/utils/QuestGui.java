package fr.grimtown.journey.game.utils;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameManager;
import fr.grimtown.journey.game.GameUtils;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class QuestGui extends FastInv {
    private final GameManager manager = GamePlugin.getManager();

    public QuestGui(Player player) {
        super(GamePlugin.getConfigs().getInt("game.gui.size"), GamePlugin.getConfigs().getString("game.gui.title"));
        setItems(0, getInventory().getSize()-1, new ItemBuilder(Material.BEDROCK).build());
        displayQuest(player);
    }

    /**
     * Display each quests in slot position
     */
    private void displayQuest(Player player) {
        manager.getLoadedQuests().forEach(quest -> {
            if (quest.getSlot() > getInventory().getSize()) return;
            ItemBuilder item = new ItemBuilder(Material.PAPER);
            if (quest.isBonus()) item.type(Material.CHEST);
            if (GameUtils.hasCompleted(player.getUniqueId(), quest)) item.type(Material.BOOK);
            setItem(quest.getSlot(), item.name(quest.getName()).lore(quest.getLore()).build());
        });
    }

    @Override
    protected void onClick(InventoryClickEvent event) { event.setCancelled(true); }
}
