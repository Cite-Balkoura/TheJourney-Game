package fr.grimtown.journey.game.commands;

import fr.grimtown.journey.game.utils.MainGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenMainGui implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        new MainGui((Player) sender).open((Player) sender);
        return true;
    }
}
