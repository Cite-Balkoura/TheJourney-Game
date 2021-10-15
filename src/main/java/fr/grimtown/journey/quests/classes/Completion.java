package fr.grimtown.journey.quests.classes;

import dev.morphia.annotations.Id;
import dev.morphia.mapping.experimental.MorphiaReference;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.UUID;

public class Completion {
    @Id
    private ObjectId id;
    private UUID uuid;
    private MorphiaReference<Quest> quest;
    private int progress;
    private Date completed;

    public Completion() {}

    public Completion(UUID uuid, Quest quest) {
        this.uuid = uuid;
        this.quest = MorphiaReference.wrap(quest);
        progress = 0;
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
    }

    public void addProgress() {
        progress++;
    }

    public Date getCompleteDate() {
        return completed;
    }

    public void setCompleted() {
        completed = new Date();
    }
}
