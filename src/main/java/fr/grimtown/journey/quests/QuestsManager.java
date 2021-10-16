package fr.grimtown.journey.quests;

import fr.grimtown.journey.quests.classes.Progression;
import fr.grimtown.journey.quests.classes.Quest;
import fr.grimtown.journey.quests.managers.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class QuestsManager {
    public static HashMap<UUID, ArrayList<Progression>> playerProgression = new HashMap<>();

    public QuestsManager(JavaPlugin plugin) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        QuestManager.getQuests().forEach(quest -> {
            try {// TODO: 15/10/2021 [TEST] Check that
                pm.registerEvents((Listener) Class.forName("fr.grimtown.journey.quests.listeners." + quest.getType().name())
                                .getDeclaredConstructor(Quest.class).newInstance(quest), plugin);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                    InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
