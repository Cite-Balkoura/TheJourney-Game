package fr.grimtown.journey.quests;

import fr.grimtown.journey.quests.classes.Completion;
import fr.grimtown.journey.quests.classes.Quest;
import fr.grimtown.journey.quests.listeners.ProgressionLoader;
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
    public static HashMap<UUID, ArrayList<Completion>> playerCompletion = new HashMap<>();

    public QuestsManager(JavaPlugin plugin) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        QuestManager.getQuests().forEach(quest -> {
            try {// TODO: 15/10/2021 Check that
                pm.registerEvents((Listener) Class.forName(quest.getType().name())
                                .getDeclaredConstructor(Quest.class).newInstance(quest), plugin);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                    InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        pm.registerEvents(new ProgressionLoader(), plugin);
    }
}
