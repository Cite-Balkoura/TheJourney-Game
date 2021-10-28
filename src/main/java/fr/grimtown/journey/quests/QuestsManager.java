package fr.grimtown.journey.quests;

import fr.grimtown.journey.game.GameManager;
import fr.grimtown.journey.quests.classes.Quest;
import fr.grimtown.journey.quests.managers.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.InvocationTargetException;

public class QuestsManager {
    public QuestsManager(Plugin plugin, GameManager game) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        QuestManager.getQuests().forEach(quest -> {
            try {
                pm.registerEvents(getQuestListener(quest), plugin);
                game.addQuest(quest);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                    InstantiationException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
        });
    }

    private Listener getQuestListener(Quest quest) throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        return (Listener) Class.forName("fr.grimtown.journey.quests.listeners." + quest.getType().name())
                        .getDeclaredConstructor(Quest.class)
                        .newInstance(quest);
    }
}
