package de.nikey.randomEvents;

import de.nikey.randomEvents.Commands.EventCommand;
import de.nikey.randomEvents.Events.TreasureHunt;
import de.nikey.randomEvents.General.EventsAPI;
import de.nikey.randomEvents.Loottables.TreasureHuntLootTable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public final class RandomEvents extends JavaPlugin {
    private static RandomEvents plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        TreasureHunt.lootTable = new TreasureHuntLootTable(new NamespacedKey(this, "treasure_hunt_loot"));
        startHuntEvent();
        getCommand("Event").setExecutor(new EventCommand());

    }

    @Override
    public void onDisable() {
        
    }

    private void startHuntEvent() {
        Random random = new Random();

        new BukkitRunnable() {
            @Override
            public void run() {
                int i = random.nextInt(30);
                if (i == 2) {
                    if (Bukkit.getOnlinePlayers().size() >= EventsAPI.getTreasureHuntMinPlayer()) {
                        TreasureHunt.startTreasureHunt();
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L * 300);
    }

    public static RandomEvents getPlugin() {
        return plugin;
    }
}
