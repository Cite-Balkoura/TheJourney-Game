package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.game.classes.Progression;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.HashMap;
import java.util.UUID;

public class Travel implements Listener {
    private final Quest quest;
    private final HashMap<UUID, Location> travel = new HashMap<>();
    public Travel(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        QuestsUtils.questLoadLog(quest.getName(), quest.getPayload());
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerPearl(PlayerTeleportEvent event) {
        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) return;
        if (!quest.getPayload().equalsIgnoreCase("PEARL")) return;
        if (GameUtils.hasCompleted(event.getPlayer().getUniqueId(), quest)) return;
        if (quest.getCount() > (int) Math.round(event.getFrom().distance(event.getTo()))) return;
        GameUtils.getProgression(event.getPlayer().getUniqueId(), quest).setCompleted();
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerVehicleEnter(VehicleEnterEvent event) {
        if (!event.getVehicle().getType().toString().contains(quest.getPayload())) return;
        if (!(event.getEntered() instanceof Player player)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        travel.put(player.getUniqueId(), player.getLocation());
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerVehicleExit(VehicleExitEvent event) {
        if (!event.getVehicle().getType().toString().contains(quest.getPayload())) return;
        if (!(event.getExited() instanceof Player player)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        travelUpdate(player);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerGlide(EntityToggleGlideEvent event) {
        if (!quest.getPayload().equalsIgnoreCase("GLIDING")) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        if (event.isGliding()) travel.put(player.getUniqueId(), player.getLocation());
        else travelUpdate(player);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerMount(EntityMountEvent event) {
        if (!event.getEntity().getType().toString().equalsIgnoreCase(quest.getPayload())) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        travel.put(player.getUniqueId(), player.getLocation());
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerDismount(EntityDismountEvent event) {
        if (!event.getEntity().getType().toString().equalsIgnoreCase(quest.getPayload())) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (GameUtils.hasCompleted(player.getUniqueId(), quest)) return;
        travelUpdate(player);
    }

    /**
     * Update quest for player
     */
    private void travelUpdate(Player player) {
        if (travel.containsKey(player.getUniqueId())) {
            Progression progression = GameUtils.getProgression(player.getUniqueId(), quest);
            int distance = (int) Math.round(travel.get(player.getUniqueId()).distance(player.getLocation()));
            while (distance > 0 && quest.getCount() > progression.getProgress()) {
                progression.addProgress();
                distance--;
            }
        }
        travel.remove(player.getUniqueId());
    }
}
