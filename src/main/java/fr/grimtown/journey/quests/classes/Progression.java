package fr.grimtown.journey.quests.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.mapping.experimental.MorphiaReference;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.managers.ProgressionManager;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.UUID;

@Entity(value = "progression")
public class Progression {
    @Id
    private ObjectId id;
    private UUID uuid;
    private MorphiaReference<Quest> quest;
    private int progress;
    private Date completed;

    public Progression() {}

    public Progression(UUID uuid, Quest quest) {
        this.uuid = uuid;
        this.quest = MorphiaReference.wrap(quest);
        progress = 0;
        QuestsUtils.playerProgression.get(uuid).add(this);
    }

    public ObjectId getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Quest getQuest() {
        return quest.get();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        ProgressionManager.save(this);
    }

    public void addProgress() {
        progress++;
        if (progress >= getQuest().getCount()) setCompleted();
        ProgressionManager.save(this);
    }

    public void setCompleted() {
        setProgress(getQuest().getCount());
        completed = new Date();
        ProgressionManager.save(this);
        QuestsUtils.completionAnnounce(uuid, getQuest());
        // TODO: 16/10/2021 Trigger custom event, for announce, quests check, bonus check
    }

    public Date getCompleted() {
        return completed;
    }

    public boolean isCompleted() {
        return completed!=null;
    }
}
