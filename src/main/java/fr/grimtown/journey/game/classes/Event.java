package fr.grimtown.journey.game.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

@Entity(value = "event")
public class Event {
    @Id
    private ObjectId id;
    private String name;
    private String database;
    private ArrayList<EventFeature> eventFeatures;
    private Date startDate;
    private Date endDate;

    public enum EventFeature {
        TIME,
        OBJECTIVE,
        CITE,
        TEAM
    }

    public Event() {}

    public String getName() {
        return name;
    }

    public String getDatabase() {
        return database;
    }

    public ArrayList<EventFeature> getEventFeatures() {
        return eventFeatures;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isRunning() {
        return startDate.before(new Date()) && endDate.after(new Date());
    }
}