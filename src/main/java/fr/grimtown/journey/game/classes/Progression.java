package fr.grimtown.journey.game.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.game.events.PlayerQuestComplete;
import fr.grimtown.journey.game.managers.ProgressionManager;
import fr.grimtown.journey.quests.classes.Quest;
import fr.grimtown.journey.quests.managers.QuestManager;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

@Entity(value = "progression")
public class Progression {
    @Id
    private ObjectId id;
    private UUID uuid;
    private ObjectId quest;
    private int progress;
    private Date completed;

    public Progression() {}

    public Progression(UUID uuid, Quest quest) {
        this.uuid = uuid;
        this.quest = quest.getId();
        progress = 0;
        GameUtils.getProgressions(uuid).add(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Quest getQuest() {
        return QuestManager.getQuest(quest);
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
        progress = getQuest().getCount();
        completed = new Date();
        ProgressionManager.save(this);
        Bukkit.getScheduler().runTask(GamePlugin.getPlugin(), () ->
                Bukkit.getPluginManager().callEvent(new PlayerQuestComplete(Bukkit.getPlayer(uuid), getQuest(), this)));
    }

    public boolean isCompleted() {
        return completed!=null;
    }
}
