package de.nikey.randomEvents.Commands;

import de.nikey.randomEvents.Events.AbilityBoost;
import de.nikey.randomEvents.Events.TreasureHunt;
import de.nikey.randomEvents.FFA.FFA;
import de.nikey.randomEvents.General.EventsAPI;
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
                    FFA.sendInvite();
                }
            }
            return true;
        }
        return true;
    }
}
