package fr.grimtown.journey.save.events;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.save.classes.ConnectionSave;
import fr.grimtown.journey.save.managers.SaveManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;

public class Connection implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        ItemStack[] armor = event.getPlayer().getInventory().getArmorContents().clone();
        ItemStack[] inv = event.getPlayer().getInventory().getContents().clone();
        int levels = event.getPlayer().getLevel();
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(GamePlugin.class), ()-> {
            ConnectionSave connectionSave = new ConnectionSave(event.getPlayer().getUniqueId(), new Date());
            connectionSave.setAction(ConnectionSave.Action.JOIN);
            connectionSave.getSavedInventory().setArmor(armor);
            connectionSave.getSavedInventory().setInvB64(inv);
            connectionSave.getSavedInventory().setLevelsB64(levels);
            SaveManager.save(connectionSave);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent event) {
        ItemStack[] armor = event.getPlayer().getInventory().getArmorContents().clone();
        ItemStack[] inv = event.getPlayer().getInventory().getContents().clone();
        int levels = event.getPlayer().getLevel();
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(GamePlugin.class), ()-> {
            ConnectionSave connectionSave = new ConnectionSave(event.getPlayer().getUniqueId(), new Date());
            connectionSave.setAction(ConnectionSave.Action.LEAVE);
            connectionSave.getSavedInventory().setArmor(armor);
            connectionSave.getSavedInventory().setInvB64(inv);
            connectionSave.getSavedInventory().setLevelsB64(levels);
            SaveManager.save(connectionSave);
        });
    }
}
