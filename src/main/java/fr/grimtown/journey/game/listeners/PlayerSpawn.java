package fr.grimtown.journey.game.listeners;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.utils.SpawnPlayer;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlayerSpawn implements Listener {
    public static String lore = "§6KeepInventory";

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Bukkit.getPluginManager().registerEvents(new SpawnPlayer(event.getEntity(), false), GamePlugin.getPlugin());
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            Bukkit.getPluginManager().registerEvents(new SpawnPlayer(event.getPlayer(), true), GamePlugin.getPlugin());
            Bukkit.getScheduler().runTaskAsynchronously(GamePlugin.getPlugin(), ()-> {
                List<Map<?, ?>> kit = GamePlugin.getConfigs().getMapList("game.kit");
                kit.forEach(item -> event.getPlayer().getInventory().addItem(
                            new ItemBuilder(Material.valueOf(((String) item.get("item")).toUpperCase(Locale.ROOT)))
                                    .amount((int) item.get("count"))
                                    .lore(lore)
                                    .build()));
            });
        }
    }
}
