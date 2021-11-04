package fr.grimtown.journey.data.classes;

import com.comphenix.protocol.utility.StreamSerializer;
import dev.morphia.annotations.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
public class SavedInventory {
    private ArrayList<String> invB64;
    private ArrayList<String> armorB64;
    private int levels;

    public SavedInventory() {}

    public ItemStack[] getInvB64() {
        return getContents(invB64);
    }

    public void setInvB64(ItemStack[] invB64) {
        this.invB64 = getContentsString(invB64);
    }

    public ItemStack[] getArmor() {
        return getContents(armorB64);
    }

    public void setArmor(ItemStack[] armor) {
        this.armorB64 = getContentsString(armor);
    }

    public int getLevelsB64() {
        return levels;
    }

    public void setLevelsB64(int levels) {
        this.levels = levels;
    }

    private ItemStack[] getContents(ArrayList<String> content) {
        return content.stream().map(loopItem -> {
            try {
                return new StreamSerializer().deserializeItemStack(loopItem);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).toArray(ItemStack[]::new);
    }

    private ArrayList<String> getContentsString(ItemStack[] content) {
        return Arrays.stream(content).map(loopItem -> {
            try {
                return new StreamSerializer().serializeItemStack(loopItem);
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
