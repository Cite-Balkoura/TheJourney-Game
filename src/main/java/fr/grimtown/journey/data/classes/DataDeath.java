package fr.grimtown.journey.data.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Date;
import java.util.UUID;

@Entity(value = "data")
public class DataDeath {
    @Id
    private Object id;
    private UUID playerUuid;
    private Date date;
    private DamageCause cause;
    private SavedInventory savedInventory;

    public DataDeath() {}

    public DataDeath(UUID playerUuid, Date date) {
        this.playerUuid = playerUuid;
        this.date = date;
        this.savedInventory = new SavedInventory();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Date getDate() {
        return date;
    }

    public DamageCause getCause() {
        return cause;
    }

    public void setCause(DamageCause cause) {
        this.cause = cause;
    }

    public SavedInventory getSavedInventory() {
        return savedInventory;
    }

    public void setSavedInventory(SavedInventory savedInventory) {
        this.savedInventory = savedInventory;
    }
}
