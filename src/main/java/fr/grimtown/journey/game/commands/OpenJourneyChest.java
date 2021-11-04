package fr.grimtown.journey.game.commands;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.classes.DataPlayer;
import fr.grimtown.journey.game.managers.DataPlayerManager;
import fr.grimtown.journey.game.utils.JourneyChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenJourneyChest implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==0) new JourneyChest(((Player) sender).getUniqueId()).open((Player) sender);
        else if (sender.hasPermission("mods.chest.see.other")) {
            DataPlayer dataPlayer = DataPlayerManager.getDataPlayer(args[0]);
            if (dataPlayer==null) sender.sendMessage(GamePlugin.getConfigs().getString("game.messages.player-not-found"));
            else new JourneyChest(dataPlayer.getUuid()).open((Player) sender);
        }
        return true;
    }
}
