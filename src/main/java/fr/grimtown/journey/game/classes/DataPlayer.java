package fr.grimtown.journey.game.classes;

import com.comphenix.protocol.utility.StreamSerializer;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import org.bson.types.ObjectId;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Entity(value = "dataPlayer")
public class DataPlayer {
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private UUID uuid;
    @Indexed(options = @IndexOptions(unique = true))
    private String lastNameSeen;
    private Universe universe;
    private int questsCompleted;
    private int bonusCompleted;
    private HashMap<String, String> journeyChest;

    public DataPlayer() {}

    public UUID getUuid() {
        return uuid;
    }

    public Universe getCurrentUniverse() {
        return universe;
    }

    /**
     * Return the next universe, if exists, otherwise return last universe
     */
    public Universe getNextUniverse() {
        return Arrays.stream(Universe.values()).filter(universe-> universe.getLevel()==this.universe.getLevel() + 1)
                .findFirst().orElse(Universe.SKY_LAND);
    }

    public int getTotalCompleted() {
        return questsCompleted;
    }

    public int getBonusCompleted() {
        return bonusCompleted;
    }

    public void setBonusCompleted(int bonusCompleted) {
        this.bonusCompleted = bonusCompleted;
    }

    public HashMap<String, String> getJourneyChestB64() {
        return journeyChest;
    }

    public HashMap<Integer, ItemStack> getJourneyChest() {
        if (journeyChest==null) return new HashMap<>();
        HashMap<Integer, ItemStack> itemStacks = new HashMap<>();
        journeyChest.forEach((slot, item) -> {
            try {
                itemStacks.put(Integer.valueOf(slot), new StreamSerializer().deserializeItemStack(item));
            } catch (IOException ignore) {
                itemStacks.put(Integer.valueOf(slot), null);
            }
        });
        return itemStacks;
    }

    public void setJourneyChest(HashMap<Integer, ItemStack> journeyChest) {
        HashMap<String, String> itemStacks = new HashMap<>();
        journeyChest.forEach((slot, item) -> {
            try {
                itemStacks.put(String.valueOf(slot), new StreamSerializer().serializeItemStack(item));
            } catch (IOException ignore) {
                itemStacks.put(String.valueOf(slot), null);
            }
        });
        this.journeyChest = itemStacks;
    }
}
