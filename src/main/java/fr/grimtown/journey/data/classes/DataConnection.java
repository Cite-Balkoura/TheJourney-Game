package fr.grimtown.journey.data.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.Date;
import java.util.UUID;

@Entity(value = "data")
public class DataConnection {
    @Id
    private Object id;
    private UUID playerUuid;
    private Date date;
    private Action action;
    private SavedInventory savedInventory;

    public enum Action { JOIN, LEAVE }

    public DataConnection() {}

    public DataConnection(UUID playerUuid, Date date) {
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public SavedInventory getSavedInventory() {
        return savedInventory;
    }

    public void setSavedInventory(SavedInventory savedInventory) {
        this.savedInventory = savedInventory;
    }
}
