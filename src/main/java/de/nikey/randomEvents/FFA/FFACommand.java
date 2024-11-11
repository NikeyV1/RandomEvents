package de.nikey.randomEvents.FFA;

import de.nikey.randomEvents.API.EventsAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class FFACommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (command.getName().equalsIgnoreCase("ffa")) {
                if (args[0].equalsIgnoreCase("join")) {
                    if (FFA_API.invited.contains(player)) {
                        int size = EventsAPI.getFFATeleportLocations().size();
                        if (FFA_API.queue.size() < size) {
                            if (!FFA_API.queue.contains(player))FFA_API.queue.add(player);
                            player.sendMessage(Component.text("You joined the queue").color(NamedTextColor.YELLOW));
                        }else {
                            player.sendMessage(Component.text("The queue is full").color(NamedTextColor.RED));
                        }
                    }
                }else if (args[0].equalsIgnoreCase("leave")) {
                    boolean remove = FFA_API.queue.remove(player);
                    if (remove) {
                        player.sendMessage(Component.text("You left the queue").color(NamedTextColor.YELLOW));
                    }else {
                        player.sendMessage(Component.text("You weren't in the queue").color(NamedTextColor.YELLOW));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("join", "leave");
        }else {
            return null;
        }
    }
}
