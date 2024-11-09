package de.nikey.randomEvents;

import de.nikey.randomEvents.Commands.EventCommand;
import de.nikey.randomEvents.Events.FishingContest;
import de.nikey.randomEvents.Events.TaskEvent;
import de.nikey.randomEvents.FFA.FFA;
import de.nikey.randomEvents.FFA.FFACommand;
import de.nikey.randomEvents.Events.AbilityBoost;
import de.nikey.randomEvents.Events.TreasureHunt;
import de.nikey.randomEvents.FFA.FFAIngame;
import de.nikey.randomEvents.API.EventsAPI;
import de.nikey.randomEvents.Loottables.FFALootTable;
import de.nikey.randomEvents.Loottables.TreasureHuntLootTable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
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
        FFAIngame.lootTable = new FFALootTable(new NamespacedKey(this,"ffa_loottable"));
        startEvent();
        loadWord();
        getCommand("Event").setExecutor(new EventCommand());
        getCommand("FFA").setExecutor(new FFACommand());
        getCommand("FFA").setTabCompleter(new FFACommand());

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new FFAIngame(),this);
        manager.registerEvents(new FishingContest(),this);
        manager.registerEvents(new TaskEvent(),this);

    }

    @Override
    public void onDisable() {
        
    }

    private void loadWord() {
        String world = EventsAPI.getFFAWorld();
        WorldCreator creator = new WorldCreator(world);

        Bukkit.createWorld(creator);
        getLogger().info("World was created");
    }

    private void startEvent() {
        Random random = new Random();

        new BukkitRunnable() {
            @Override
            public void run() {
                int i = random.nextInt(40);
                if (i == 2 && Bukkit.getOnlinePlayers().size() >= EventsAPI.getTreasureHuntMinPlayer()) {
                    TreasureHunt.startTreasureHunt();
                    getLogger().info("Event: Starting Treasure Hunt");
                }else if (i == 5 && Bukkit.getOnlinePlayers().size() >= EventsAPI.getAbilityBoostMinPlayer()) {
                    AbilityBoost.activateAbilityBoost();
                    getLogger().info("Event: Starting Ability Boost");
                }else if (i == 8 && Bukkit.getOnlinePlayers().size() >= EventsAPI.getFFAMinPlayer()) {
                    FFA.sendInvite();
                    getLogger().info("Event: Starting FFA");
                }else if (i == 9 && Bukkit.getOnlinePlayers().size() >= EventsAPI.getFishingContestMinPlayer()) {
                    FishingContest.startFishingContest();
                    getLogger().info("Event: Starting Fishing Contest");
                }else if (i == 10 && Bukkit.getOnlinePlayers().size() >= EventsAPI.getTaskMinPlayers()) {
                    TaskEvent.startEvent();
                    getLogger().info("Event: Starting Task Event");
                }
            }
        }.runTaskTimer(this, 0L, 20 * 600);
    }

    public static RandomEvents getPlugin() {
        return plugin;
    }
}
