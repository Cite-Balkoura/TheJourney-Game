package fr.grimtown.journey.data.events;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.data.classes.DataConnection;
import fr.grimtown.journey.data.managers.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;

public class Connection implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(GamePlugin.class), ()-> {
            DataConnection dataConnection = new DataConnection(event.getPlayer().getUniqueId(), new Date());
            dataConnection.setAction(DataConnection.Action.JOIN);
            dataConnection.getSavedInventory().setArmor(event.getPlayer().getInventory().getArmorContents());
            dataConnection.getSavedInventory().setInvB64(event.getPlayer().getInventory().getContents());
            dataConnection.getSavedInventory().setLevelsB64(event.getPlayer().getLevel());
            DataManager.save(dataConnection);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(GamePlugin.class), ()-> {
            DataConnection dataConnection = new DataConnection(event.getPlayer().getUniqueId(), new Date());
            dataConnection.setAction(DataConnection.Action.LEAVE);
            dataConnection.getSavedInventory().setArmor(event.getPlayer().getInventory().getArmorContents());
            dataConnection.getSavedInventory().setInvB64(event.getPlayer().getInventory().getContents());
            dataConnection.getSavedInventory().setLevelsB64(event.getPlayer().getLevel());
            DataManager.save(dataConnection);
        });
    }
}
