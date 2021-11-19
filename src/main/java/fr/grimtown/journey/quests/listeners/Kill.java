package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class Kill implements Listener {
    private final Quest quest;
    private EntityType entity = null;
    private final ArrayList<EntityType> entities = new ArrayList<>();

    public Kill(Quest quest) {
        this.quest = quest;
        quest.setListeners(this);
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            QuestsUtils.questLoadLog(quest.getName(), "ANY");
        } else if (quest.getPayload().contains(",")) {
            Arrays.stream(quest.getPayload().split(",")).toList()
                    .forEach(materialType -> {
                        try {
                        EntityType entity = EntityType.valueOf(materialType.toUpperCase(Locale.ROOT));
                        entities.add(entity);
                        } catch (IllegalArgumentException exception) {
                            Bukkit.getLogger().warning("Can't load: " + quest.getName());
                            exception.printStackTrace();
                            HandlerList.unregisterAll(this);
                        }
                    });
            QuestsUtils.questLoadLog(quest.getName(), "Whitelist=" +
                    entities.stream().map(Enum::toString).collect(Collectors.joining(",")));
        } else {
            try {
                entity = EntityType.valueOf(quest.getPayload().toUpperCase(Locale.ROOT));
                QuestsUtils.questLoadLog(quest.getName(), entity.toString());
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning("Can't load: " + quest.getName());
                exception.printStackTrace();
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerMobKill(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) return;
        if (entity!=null && !event.getEntity().getType().equals(entity)) return;
        if (entity==null && !entities.isEmpty() && !entities.contains(event.getEntity().getType())) return;
        if (event.getEntity().getKiller()==null) return;
        if (GameUtils.hasCompleted(event.getEntity().getKiller().getUniqueId(), quest)) return;
        GameUtils.getProgression(event.getEntity().getKiller().getUniqueId(), quest).addProgress();
    }
}
