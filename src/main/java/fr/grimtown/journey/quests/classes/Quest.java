package fr.grimtown.journey.quests.classes;

import dev.morphia.annotations.*;
import org.bson.types.ObjectId;
import org.bukkit.event.Listener;

@Entity(value = "quest")
public class Quest {
    @Id
    private ObjectId id;
    private String universe;
    @Indexed(options = @IndexOptions(unique = true))
    private String name;
    private String lore;
    private int slot;
    private Type type;
    private int count;
    private String payload;
    private boolean bonus;
    @Transient
    private Listener listeners;

    public Quest() {}

    public enum Type {
        Beacon,
        Break,
        Breed,
        Brew,
        Craft,
        Death,
        Eat,
        Effect,
        Enchant,
        Equip,
        Experience,
        Explore,
        Growth,
        Hit,
        Inventory,
        Invoke,
        Kill,
        Liquid,
        MobDamage,
        Place,
        Repair,
        Saturation,
        Shoot,
        Sleep,
        Smelt,
        StandOn,
        Tamed,
        Trade,
        Travel,
        Upgrade
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public int getSlot() {
        return slot;
    }

    public Type getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isBonus() {
        return bonus;
    }

    public boolean notBonus() {
        return !bonus;
    }

    public Listener getListeners() {
        return listeners;
    }

    public void setListeners(Listener listeners) {
        this.listeners = listeners;
    }
}
