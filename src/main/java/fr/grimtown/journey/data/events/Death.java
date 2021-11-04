package fr.grimtown.journey.data.events;


import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.data.classes.DataDeath;
import fr.grimtown.journey.data.managers.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;

public class Death implements Listener {
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getKeepInventory()) return;
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(GamePlugin.class), ()-> {
            DataDeath dataDeath = new DataDeath(event.getEntity().getUniqueId(), new Date());
            if (event.getEntity().getLastDamageCause()!=null)  dataDeath.setCause(event.getEntity().getLastDamageCause().getCause());
            dataDeath.getSavedInventory().setArmor(event.getEntity().getInventory().getArmorContents());
            dataDeath.getSavedInventory().setInvB64(event.getEntity().getInventory().getContents());
            dataDeath.getSavedInventory().setLevelsB64(event.getEntity().getLevel());
            DataManager.save(dataDeath);
        });
    }
}
