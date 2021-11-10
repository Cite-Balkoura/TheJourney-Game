package fr.grimtown.journey.game.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.managers.DataPlayerManager;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class SwitchGui extends FastInv {
    public SwitchGui(UUID uuid) {
        super(GamePlugin.getConfigs().getInt("game.switch-gui.size"),
                GamePlugin.getConfigs().getString("game.switch-gui.title"));
        setItems(0, getInventory().getSize()-1, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
        setItem(11,
                new ItemBuilder(Material.GREEN_CONCRETE)
                        .name(GamePlugin.getConfigs().getString("game.switch-gui.accept.name"))
                        .lore(GamePlugin.getConfigs().getStringList("game.switch-gui.accept.lore"))
                        .build()
                , event -> {
                    sendPlayerToServer((Player) event.getWhoClicked(),
                            "Journey_Universe_" + GamePlugin.getUniverse().getNext().level);
                    DataPlayerManager.nextUniverse(uuid);
                }
        );
        setItem(15,
                new ItemBuilder(Material.RED_CONCRETE)
                        .name(GamePlugin.getConfigs().getString("game.switch-gui.deny.name"))
                        .lore(GamePlugin.getConfigs().getStringList("game.switch-gui.deny.lore"))
                        .build()
                , event -> event.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN));
    }

    /**
     *      Send player to server
     */
    private void sendPlayerToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(GamePlugin.getPlugin(), "BungeeCord", out.toByteArray());
    }
}
