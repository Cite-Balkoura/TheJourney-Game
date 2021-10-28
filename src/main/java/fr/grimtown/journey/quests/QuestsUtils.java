package fr.grimtown.journey.quests;

import org.bukkit.Bukkit;

import javax.annotation.Nullable;

public class QuestsUtils {
    /**
     * Console log a new quest loaded, and indicate the payload
     */
    public static void questLoadLog(String questName, @Nullable String payload) {
        Bukkit.getLogger().info(String.format("Listeners for quest '%s' loaded with payload: {%s}.", questName, payload));
    }
}
