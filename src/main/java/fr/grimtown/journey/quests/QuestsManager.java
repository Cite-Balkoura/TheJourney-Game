package fr.grimtown.journey.quests;

import fr.grimtown.journey.quests.classes.Quest;
import fr.grimtown.journey.quests.managers.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class QuestsManager {
    public QuestsManager(JavaPlugin plugin) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        QuestManager.getQuests().forEach(quest -> {
            try {
                pm.registerEvents((Listener)
                        Class.forName("fr.grimtown.journey.quests.listeners." + quest.getType().name())
                                .getDeclaredConstructor(Quest.class)
                                .newInstance(quest), plugin);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                    InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
