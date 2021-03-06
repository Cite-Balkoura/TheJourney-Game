package fr.grimtown.journey.game;

import fr.grimtown.journey.game.classes.Progression;
import fr.grimtown.journey.game.commands.OpenJourneyChest;
import fr.grimtown.journey.game.commands.OpenMainGui;
import fr.grimtown.journey.game.commands.OpenQuestGui;
import fr.grimtown.journey.game.commands.QuestManage;
import fr.grimtown.journey.game.listeners.*;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameManager {
    private final ArrayList<Quest> loadedQuests = new ArrayList<>();
    private final HashMap<UUID, ArrayList<Progression>> playerProgression = new HashMap<>();

    public GameManager(JavaPlugin plugin) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new RemoveJoinMsg(), plugin);
        pm.registerEvents(new EndChest(), plugin);
        pm.registerEvents(new PlayerSpawn(), plugin);
        pm.registerEvents(new ProgressionLoader(this), plugin);
        pm.registerEvents(new QuestComplete(this), plugin);
        pm.registerEvents(new LavaGenerator(), plugin);
        pm.registerEvents(new DeadBushSeeds(), plugin);
        plugin.getCommand("journey").setExecutor(new OpenMainGui());
        plugin.getCommand("quest").setExecutor(new OpenQuestGui());
        plugin.getCommand("manage").setExecutor(new QuestManage());
        plugin.getCommand("chest").setExecutor(new OpenJourneyChest());
        gameRules();
    }

    private void gameRules() {
        Bukkit.getWorlds().forEach(world -> {
            world.setDifficulty(Difficulty.HARD);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        });
    }

    public ArrayList<Quest> getLoadedQuests() {
        return loadedQuests;
    }

    public void addQuest(Quest quest) {
        loadedQuests.add(quest);
    }

    public HashMap<UUID, ArrayList<Progression>> getPlayerProgression() {
        return playerProgression;
    }
}
