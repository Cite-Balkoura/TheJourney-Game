package fr.grimtown.journey.game.utils;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameManager;
import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.game.classes.Universe;
import fr.grimtown.journey.quests.managers.QuestManager;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;

import java.util.UUID;

public class QuestGui extends FastInv {
    private final GameManager manager = GamePlugin.getManager();

    public QuestGui(UUID questsPlayer, Universe universe) {
        super(GamePlugin.getConfigs().getInt("game.quests-gui.size"),
                GamePlugin.getConfigs().getString("game.quests-gui.title"));
        setItems(0, getInventory().getSize()-1, new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .name(GamePlugin.getConfigs().getString("game.quests-gui.no-quest")).build());
        displayQuest(questsPlayer, universe);
    }

    /**
     * Display each quests in slot position
     */
    private void displayQuest(UUID questsPlayer, Universe universe) {
        if (universe.equals(GamePlugin.getUniverse())) {
            manager.getLoadedQuests().forEach(quest -> {
                if (quest.getSlot() > getInventory().getSize()) return;
                ItemBuilder item = new ItemBuilder(Material.PAPER);
                if (quest.isBonus()) item.type(Material.ENDER_CHEST);
                if (GameUtils.hasCompleted(questsPlayer, quest)) item.type(Material.BOOK);
                setItem(quest.getSlot(), item.name(quest.getName()).lore(quest.getLore()).build());
            });
        } else {
            QuestManager.getQuests(universe).forEach(quest -> {
                if (quest.getSlot() > getInventory().getSize()) return;
                ItemBuilder item = new ItemBuilder(Material.PAPER);
                if (quest.isBonus()) item.type(Material.ENDER_CHEST);
                if (GameUtils.hasCompleted(questsPlayer, quest, universe)) item.type(Material.BOOK);
                setItem(quest.getSlot(), item.name(quest.getName()).lore(quest.getLore()).build());
            });
        }
    }
}
