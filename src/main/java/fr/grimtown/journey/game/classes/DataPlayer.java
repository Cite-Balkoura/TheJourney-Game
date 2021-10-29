package fr.grimtown.journey.game.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import org.bson.types.ObjectId;

import java.util.Arrays;
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
                .findFirst().orElse(Universe.APOCALYPSE);
    }

    public int getTotalCompleted() {
        return questsCompleted;
    }
}
