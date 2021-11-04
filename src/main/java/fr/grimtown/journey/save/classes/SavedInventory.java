package fr.grimtown.journey.save.classes;

import dev.morphia.annotations.Entity;
import fr.grimtown.journey.save.InventoryUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@Entity
public class SavedInventory {
    private ArrayList<String> invB64;
    private ArrayList<String> armorB64;
    private int levels;

    public SavedInventory() {}

    public ItemStack[] getInvB64() {
        return InventoryUtils.getContents(invB64);
    }

    public void setInvB64(ItemStack[] invB64) {
        this.invB64 = InventoryUtils.getContentsString(invB64);
    }

    public ItemStack[] getArmor() {
        return InventoryUtils.getContents(armorB64);
    }

    public void setArmor(ItemStack[] armor) {
        this.armorB64 = InventoryUtils.getContentsString(armor);
    }

    public int getLevelsB64() {
        return levels;
    }

    public void setLevelsB64(int levels) {
        this.levels = levels;
    }
}
