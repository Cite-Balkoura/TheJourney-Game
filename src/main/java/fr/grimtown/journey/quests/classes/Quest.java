package fr.grimtown.journey.quests.classes;

public class Quest {
    private String name;
    private String lore;
    private int pos;
    private Type type;
    private int count;
    private String payload;
    private boolean bonus;

    public Quest() {}

    public enum Type {
        Inventory,
        Kill,
        Hit,
        Death,
        Level,
        Travel,
        Explore,
        Place,
        Break,
        Craft,
        Eat,
        Health,
        Saturation
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
