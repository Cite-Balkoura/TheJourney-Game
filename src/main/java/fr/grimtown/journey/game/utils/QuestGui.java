package fr.grimtown.journey.game.utils;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameManager;
import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.game.classes.Universe;
import fr.grimtown.journey.quests.classes.Quest;
import fr.grimtown.journey.quests.managers.QuestManager;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestGui extends FastInv {
    private final GameManager manager = GamePlugin.getManager();

    public QuestGui(UUID questsPlayer, Universe universe) {
        super(GamePlugin.getConfigs().getInt("game.quests-gui.size"),
                GamePlugin.getConfigs().getString("game.quests-gui.title"));
        setItems(0, getInventory().getSize()-1, new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .name(GamePlugin.getConfigs().getString("game.quests-gui.no-quest")).build());
        Bukkit.getScheduler().runTaskAsynchronously(GamePlugin.getPlugin(), ()-> displayQuest(questsPlayer, universe));
    }

    /**
     * Display each quests in slot position
     */
    private void displayQuest(UUID questsPlayer, Universe universe) {
        List<Quest> quests;
        if (universe.equals(GamePlugin.getUniverse())) {
            quests = manager.getLoadedQuests();
        } else {
            quests = QuestManager.getQuests(universe);
        }
        quests.forEach(quest -> {
            if (quest.getSlot() > getInventory().getSize()) return;
            ItemBuilder item = new ItemBuilder(Material.PAPER);
            List<String> lore = new ArrayList<>(List.of(quest.getLore().split("%n")));
            if (quest.notBonus()) {
                item.name("§2" + quest.getName());
            } else {
                item.name("§9" + quest.getName());
                item.type(Material.ENDER_CHEST);
                lore.add("§6Bonus");
            }
            if (GameUtils.hasCompleted(questsPlayer, quest, universe)) item.type(Material.BOOK);
            else if (quest.getCount()!=0) lore.add("§b" + GameUtils.getProgression(questsPlayer, quest).getProgress()
                    + "§6/§2" + Math.abs(quest.getCount()));
            setItem(quest.getSlot(), item.lore(lore).build());
        });
    }
}
