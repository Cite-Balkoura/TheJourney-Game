package fr.grimtown.journey.game.commands;

import fr.grimtown.journey.GamePlugin;
import fr.grimtown.journey.game.GameUtils;
import fr.grimtown.journey.game.classes.Progression;
import fr.grimtown.journey.game.managers.DataPlayerManager;
import fr.grimtown.journey.game.managers.ProgressionManager;
import fr.grimtown.journey.quests.classes.Quest;
import fr.milekat.utils.McTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

public class QuestManage implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length==2 && args[0].equalsIgnoreCase("top")) {
            Bukkit.getScheduler().runTaskAsynchronously(GamePlugin.getPlugin(), ()-> {
                ArrayList<Progression> progressions = ProgressionManager.getProgressions();
                TreeMap<UUID, Integer> tops = new TreeMap<>();
                progressions.stream().filter(Progression::isCompleted).forEach(progression ->
                        tops.put(progression.getUuid(), tops.getOrDefault(progression.getUuid(),0)+1));
                int loop = 0;
                sender.sendMessage("Top players :");
                for (Map.Entry<UUID, Integer> entry : entriesSortedByValues(tops)) {
                    if (loop>= Integer.parseInt(args[1])) return;
                    loop++;
                    sender.sendMessage("-" + loop + " => " + DataPlayerManager.getDataPlayer(entry.getKey()).getLastNameSeen()
                            + " with " + entry.getValue() + " Quests.");
                }
            });
        } else if (args.length>=3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target==null) {
                sender.sendMessage("§6Player not found");
                return true;
            }
            Quest quest = GameUtils.getQuest(args[2].replaceAll("_", " "));
            if (quest==null) {
                sender.sendMessage("§6Quest not found or not loaded in this universe");
                return true;
            }
            Progression progression = GameUtils.getProgression(target.getUniqueId(), quest);
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "complete" -> progression.setCompleted();
                case "reset" -> GameUtils.reset(target.getUniqueId(), progression);
                default -> {
                    if (args.length==4) {
                        switch (args[0].toLowerCase(Locale.ROOT)) {
                            case "add" -> IntStream.range(0, Integer.parseInt(args[3])).forEach(loop -> progression.addProgress());
                            case "set" -> progression.setProgress(Integer.parseInt(args[3]));
                            default -> sendHelp(sender);
                        }
                    } else sendHelp(sender);
                }
            }
        } else sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6Progression manager help");
        sender.sendMessage("§6/mq complete <player> <quest>: §rComplete the quest for the player");
        sender.sendMessage("§6/mq reset <player> <quest>:§rReset the quest for the player");
        sender.sendMessage("§6/mq add <player> <quest> <int>:§rAdd progression point(s) for the player in given quest");
        sender.sendMessage("§6/mq set <player> <quest> <int>:§rSet progression point(s) for the player in given quest");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        if (args.length<=1) return McTools.getTabArgs(args[0], List.of("complete", "add", "set", "reset"));
        else if (args.length <= 2) return null;
        else if (args.length<=3) {
            return McTools.getTabArgs(args[2], GamePlugin.getManager().getLoadedQuests().stream()
                    .map(Quest::getName)
                    .map(s -> s.replaceAll(" ", "_"))
                    .toList());
        }
        return null;
    }

    static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
