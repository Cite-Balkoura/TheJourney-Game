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
        Break,
        Craft,
        Death,
        Eat,// TODO: 28/10/2021 Test !
        Experience,
        Explore,
        Health,
        Hit,
        Inventory,
        Kill,
        Place,// TODO: 28/10/2021 Check seeds
        Saturation,
        Travel,
        Liquid,
        Equip,
        Tamed,
        Breed,
        Smelt,
        Enchant,
        Shoot,
        Brew,
        Trade,
        Repair,
        Sleep,
        Invoke,
        Effect,
        Beacon,
        MobDamage,
        StandOn,
        Upgrade,// TODO: 16/10/2021 Multiples

        Growth
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
