package fr.grimtown.journey.save;

import com.comphenix.protocol.utility.StreamSerializer;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class InventoryUtils {
    public static ItemStack[] getContents(ArrayList<String> content) {
        return content.stream().map(loopItem -> {
            try {
                return new StreamSerializer().deserializeItemStack(loopItem);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).toArray(ItemStack[]::new);
    }

    public static ArrayList<String> getContentsString(ItemStack[] content) {
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
