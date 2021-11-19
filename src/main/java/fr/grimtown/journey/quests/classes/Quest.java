package fr.grimtown.journey.quests.classes;

import dev.morphia.annotations.*;
import fr.grimtown.journey.game.classes.Universe;
import org.bson.types.ObjectId;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity(value = "quest")
public class Quest {
    @Id
    private ObjectId id;
    private Universe universe;
    @Indexed(options = @IndexOptions(unique = true))
    private String name;
    private String lore;
    private int slot;
    private String item;
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

    public Universe getUniverse() { return universe; }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public int getSlot() {
        return slot;
    }

    public ArrayList<Material> getItem() {
        if (item.contains(",")) {
            return Arrays.stream(item.split(",")).map(Material::valueOf).collect(Collectors.toCollection(ArrayList::new));
        } else {
            return new ArrayList<>(List.of(Material.valueOf(item)));
        }
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
