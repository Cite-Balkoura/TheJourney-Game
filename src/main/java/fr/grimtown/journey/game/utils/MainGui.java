package fr.grimtown.journey.game.utils;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.classes.Quest;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.stream.Collectors;

public class MainGui extends FastInv {
    public MainGui(Player player) {
        super(GamePlugin.getConfigs().getInt("game.main-gui.size"),
                GamePlugin.getConfigs().getString("game.main-gui.title"));
        setItems(0, getInventory().getSize()-1, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
        setItem(11, new ItemBuilder(Material.SPYGLASS)
                .name(GamePlugin.getConfigs().getString("game.main-gui.info.name"))
                .lore(GamePlugin.getConfigs().getStringList("game.main-gui.info.lore").stream()
                        .map(lore -> lore
                                .replaceAll("<QUESTS>", String.valueOf(GamePlugin.getManager().getLoadedQuests()
                                        .stream().filter(Quest::notBonus).count()))
                                .replaceAll("<BONUS>", String.valueOf(GamePlugin.getManager().getLoadedQuests()
                                        .stream().filter(Quest::isBonus).count())))
                        .collect(Collectors.toList()))
                .build());
        setItem(13, new ItemBuilder(Material.ENDER_CHEST)
                .name(GamePlugin.getConfigs().getString("game.main-gui.journey-chest.name"))
                .lore(GamePlugin.getConfigs().getStringList("game.main-gui.journey-chest.lore").stream()
                        .map(lore -> lore
                                .replaceAll("<SLOTS>", String.valueOf(GameUtils.getPlayerBonus(player.getUniqueId())
                                        + GamePlugin.getConfigs().getInt("game.journey-chest.base-slots"))))
                        .collect(Collectors.toList()))
                .build(), event -> new JourneyChest(player.getUniqueId()).open(player));
        setItem(15, new ItemBuilder(Material.WRITABLE_BOOK)
                .name(GamePlugin.getConfigs().getString("game.main-gui.quests.name"))
                .lore(GamePlugin.getConfigs().getStringList("game.main-gui.quests.lore"))
                .build(), event -> new QuestGui(player.getUniqueId(), GamePlugin.getUniverse()).open(player));
        //  Async display
        Bukkit.getScheduler().runTaskAsynchronously(GamePlugin.getPlugin(), ()-> stats(player));
        Bukkit.getScheduler().runTaskAsynchronously(GamePlugin.getPlugin(), ()-> switchButton(player));
    }

    /**
     * Display player stats
     */
    private void stats(Player player) {
        setItem(4, new ItemBuilder(Material.PLAYER_HEAD)
                .meta(itemMeta -> ((SkullMeta) itemMeta).setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId())))
                .name(GamePlugin.getConfigs().getString("game.main-gui.stats.name"))
                .lore(GamePlugin.getConfigs().getStringList("game.main-gui.stats.lore")
                        .stream().map(line -> line
                                .replaceAll("<QUESTS>", String.valueOf(GameUtils.getPlayerQuests(player.getUniqueId())))
                                .replaceAll("<BONUS>", String.valueOf(GameUtils.getPlayerBonus(player.getUniqueId())))
                                .replaceAll("<TOTAL>", String.valueOf(GameUtils.getPlayerFinished(player.getUniqueId())))
                        ).collect(Collectors.toList()))
                .build());
    }

    /**
     * Display switch button if player has finish this universe
     */
    private void switchButton(Player player) {
        if (GameUtils.hasFinishUniverse(player.getUniqueId(), GamePlugin.getUniverse())) {
            setItem(22, new ItemBuilder(Material.GREEN_CONCRETE)
                    .name(GamePlugin.getConfigs().getString("game.switch-gui.title"))
                    .lore(GamePlugin.getConfigs().getStringList("game.switch-gui.lore").stream().map(line -> line
                            .replaceAll("<NEXT>", GamePlugin.getUniverse().getNext().name())).collect(Collectors.toList()))
                    .build(), event -> new SwitchGui(player.getUniqueId()).open(player));
        }
    }
}
