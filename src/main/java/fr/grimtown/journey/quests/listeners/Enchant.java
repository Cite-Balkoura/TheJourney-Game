package fr.grimtown.journey.quests.listeners;

import fr.grimtown.journey.quests.QuestsUtils;
import fr.grimtown.journey.quests.classes.Quest;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.Locale;

public class Enchant implements Listener {
    private final Quest quest;
    private final Enchantment enchantment;

    public Enchant(Quest quest) {
        this.quest = quest;
        if (quest.getPayload().equalsIgnoreCase("ANY")) {
            enchantment = null;
        } else {
            enchantment = Enchantment.getByKey(NamespacedKey.minecraft(quest.getPayload().toLowerCase(Locale.ROOT)));
            if (enchantment != null) {
                QuestsUtils.questLoadLog(quest.getName(), enchantment.getKey().asString());
            } else {
                HandlerList.unregisterAll(this);
            }
        }
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event) {
        if (enchantment!=null && !event.getEnchantsToAdd().containsKey(enchantment)) return;
        if (QuestsUtils.hasCompleted(event.getEnchanter().getUniqueId(), quest)) return;
        QuestsUtils.getProgression(event.getEnchanter().getUniqueId(), quest).addProgress();
    }
}
