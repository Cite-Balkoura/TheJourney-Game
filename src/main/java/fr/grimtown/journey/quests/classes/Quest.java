package fr.grimtown.journey.quests.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import org.bson.types.ObjectId;

@Entity(value = "quest")
public class Quest {
    @Id
    private ObjectId id;
    private String universe;
    @Indexed(options = @IndexOptions(unique = true))
    private String name;
    private String lore;
    private int pos;
    private Type type;
    private int count;
    private String payload;
    private boolean bonus;

    public Quest() {}

    public enum Type {
        Break,// TODO: 25/10/2021 Multiples/Any ?
        Craft,// TODO: 25/10/2021 Multiples/Any ? // TODO: 25/10/2021 Recipe name space ?
        Death,
        Eat,
        Experience,
        Explore,// TODO: 25/10/2021 Any
        Health,
        Hit,
        Inventory,// TODO: 25/10/2021 Multiples/Any
        Kill,// TODO: 25/10/2021 Multiples/Any
        Place,
        Saturation,
        Travel,
        Liquid,
        Equip,
        Tamed,
        Breed,
        Smelt,
        // TODO: 25/10/2021 la suite :)
        Enchant,
        Shoot,
        Brew,
        Trade,
        Repair,
        Sleep,
        Spawn,
        MobDamage,// TODO: 25/10/2021 Goat Llama ?
        Effect,
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

    public int getPos() {
        return pos;
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
}
