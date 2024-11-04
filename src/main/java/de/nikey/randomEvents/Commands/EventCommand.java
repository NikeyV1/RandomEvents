package de.nikey.randomEvents.Commands;

import de.nikey.randomEvents.Events.TreasureHunt;
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
            // Überprüfen, ob der Befehl von einem Spieler oder der Konsole kommt
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                if (args[0].equalsIgnoreCase("TreasureHunt")) {
                    if (Bukkit.getOnlinePlayers().size() < EventsAPI.getTreasureHuntMinPlayer()) {
                        sender.sendMessage("§cEs sind nicht genügend Spieler online, um das Schatz-Event zu starten!");
                        return true;
                    }
                    TreasureHunt.startTreasureHunt();
                    sender.sendMessage("§aSchatz-Event gestartet!");
                }
            } else {
                sender.sendMessage("§cNur Spieler oder die Konsole können diesen Befehl ausführen.");
            }
            return true;
        }

        return true;
    }


}
