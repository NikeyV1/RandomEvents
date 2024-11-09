package de.nikey.randomEvents.Commands;

import de.nikey.randomEvents.Events.AbilityBoost;
import de.nikey.randomEvents.Events.FishingContest;
import de.nikey.randomEvents.Events.TaskEvent;
import de.nikey.randomEvents.Events.TreasureHunt;
import de.nikey.randomEvents.FFA.FFA;
import de.nikey.randomEvents.API.EventsAPI;
import de.nikey.randomEvents.FFA.FFA_API;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EventCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("Event")) {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                if (!sender.isOp())return true;

                if (args[0].equalsIgnoreCase("TreasureHunt")) {
                    if (Bukkit.getOnlinePlayers().size() < EventsAPI.getTreasureHuntMinPlayer()) {
                        sender.sendMessage("§cNot enough players online");
                        return true;
                    }
                    TreasureHunt.startTreasureHunt();
                    sender.sendMessage("§aTreasure Hunt event started!");
                }else if (args[0].equalsIgnoreCase("AbilityBoost")) {
                    if (Bukkit.getOnlinePlayers().size() < EventsAPI.getAbilityBoostMinPlayer()) {
                        sender.sendMessage("§cNot enough players online");
                        return true;
                    }
                    AbilityBoost.activateAbilityBoost();
                    sender.sendMessage("§aAbility Boost event started!");
                }else if (args[0].equalsIgnoreCase("FFA")) {
                    if (Bukkit.getOnlinePlayers().size() < EventsAPI.getFFAMinPlayer()) {
                        sender.sendMessage("§cNot enough players online");
                        return true;
                    }
                    if (args.length == 3 && args[1].equalsIgnoreCase("load")) {
                        Player player = Bukkit.getPlayer(args[2]);
                        player.getInventory().setContents(FFA_API.playerItems.get(player.getName()));
                    }
                    FFA.sendInvite();
                }else if (args[0].equalsIgnoreCase("FishingContest")) {
                    if (Bukkit.getOnlinePlayers().size() < EventsAPI.getFishingContestMinPlayer()) {
                        sender.sendMessage("§cNot enough players online");
                        return true;
                    }
                    FishingContest.startFishingContest();
                }else if (args[0].equalsIgnoreCase("Task")) {
                    if (Bukkit.getOnlinePlayers().size() < EventsAPI.getTaskMinPlayers()) {
                        sender.sendMessage("§cNot enough players online");
                        return true;
                    }
                    TaskEvent.startEvent();
                }
            }
            return true;
        }
        return true;
    }
}
