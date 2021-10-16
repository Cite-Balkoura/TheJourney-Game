package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class Travel implements Listener {
    private final Quest quest;
    private final HashMap<UUID, Location> travel = new HashMap<>();
    public Travel(Quest quest) {
        this.quest = quest;


    }

    @EventHandler
    public void onPlayerTravel() {

    }
}
