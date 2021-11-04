package fr.grimtown.journey.game.utils;

import fr.grimtown.journey.GamePlugin;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

public class MainGui extends FastInv {
    public MainGui(Player player) {
        super(GamePlugin.getConfigs().getInt("game.main-gui.size"),
                GamePlugin.getConfigs().getString("game.main-gui.title"));
        setItems(0, getInventory().getSize()-1, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
        setItem(11, new ItemBuilder(Material.SPYGLASS)
                .name(GamePlugin.getConfigs().getString("game.main-gui.info.name"))
                .lore(GamePlugin.getConfigs().getStringList("game.main-gui.info.lore"))
                .build());
        setItem(4, new ItemBuilder(Material.PLAYER_HEAD)// TODO: 05/11/2021 Finish this
                .meta(itemMeta -> ((SkullMeta) itemMeta).setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId())))
                .name(GamePlugin.getConfigs().getString("game.main-gui.stats.name"))
                .lore(GamePlugin.getConfigs().getStringList("game.main-gui.stats.lore"))
                .build());
        setItem(13, new ItemBuilder(Material.ENDER_CHEST)
                .name(GamePlugin.getConfigs().getString("game.journey-chest.title"))
                .build(), event -> new JourneyChest(player.getUniqueId()).open(player));
        setItem(15, new ItemBuilder(Material.WRITABLE_BOOK)
                .name(GamePlugin.getConfigs().getString("game.main-gui.quests.name"))
                .lore(GamePlugin.getConfigs().getStringList("game.main-gui.quests.lore"))
                .build(), event -> new QuestGui(player.getUniqueId(), GamePlugin.getUniverse()).open(player));
    }
}
