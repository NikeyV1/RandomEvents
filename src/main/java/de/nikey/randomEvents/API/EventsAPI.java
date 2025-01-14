package de.nikey.randomEvents.API;

import de.nikey.randomEvents.RandomEvents;

import java.util.List;

public class EventsAPI {
    public static int getTreasureHuntMinPlayer() {
        return RandomEvents.getPlugin().getConfig().getInt("Treasure Hunt.Min-Players");
    }

    public static int getTreasureHuntMaxAttempts() {
        return RandomEvents.getPlugin().getConfig().getInt("Treasure Hunt.Max-Attempts");
    }

    public static int getTreasureHuntSpawnRadius() {
        return RandomEvents.getPlugin().getConfig().getInt("Treasure Hunt.Spawn-Radius");
    }

    public static int getAbilityBoostMinPlayer() {
        return RandomEvents.getPlugin().getConfig().getInt("Ability Boost.Min-Players");
    }

    public static int getAbilityBoostDuration() {
        int duration;
        duration = RandomEvents.getPlugin().getConfig().getInt("Ability Boost.Duration");
        duration = duration*20;
        return duration;
    }

    public static int getFFAMinPlayer() {
        return RandomEvents.getPlugin().getConfig().getInt("FFA.Min-Players");
    }

    public static int getFFAStartCountdown() {
        return RandomEvents.getPlugin().getConfig().getInt("FFA.Start-Countdown");
    }

    public static String getFFAWorld() {
        return RandomEvents.getPlugin().getConfig().getString("FFA.World");
    }

    public static List<String> getFFATeleportLocations() {
        return RandomEvents.getPlugin().getConfig().getStringList("FFA.TeleportLocations");
    }

    public static int getFishingContestMinPlayer() {
        return RandomEvents.getPlugin().getConfig().getInt("FishingContest.Min-Players");
    }

    public static int getFishingContestTime() {
        return RandomEvents.getPlugin().getConfig().getInt("FishingContest.Time");
    }
    public static int getFishingContestChance() {
        return RandomEvents.getPlugin().getConfig().getInt("FishingContest.Drop-Chance");
    }
    public static int getTaskMinPlayers() {
        return RandomEvents.getPlugin().getConfig().getInt("TaskEvent.Min-Players");
    }

    public static int getTaskMinItems() {
        return RandomEvents.getPlugin().getConfig().getInt("TaskEvent.Min-Items");
    }
    public static int getTaskMaxItems() {
        return RandomEvents.getPlugin().getConfig().getInt("TaskEvent.Max-Items");
    }
    public static int getTaskMinMobs() {
        return RandomEvents.getPlugin().getConfig().getInt("TaskEvent.Min-Mobs");
    }
    public static int getTaskMaxMobs() {
        return RandomEvents.getPlugin().getConfig().getInt("TaskEvent.Max-Mobs");
    }
    public static int getTaskAmount() {
        return RandomEvents.getPlugin().getConfig().getInt("TaskEvent.TasksAmount");
    }
    public static int getTaskTime() {
        return RandomEvents.getPlugin().getConfig().getInt("TaskEvent.Time");
    }

    public static int getLootLamaMinPlayers() {
        return RandomEvents.getPlugin().getConfig().getInt("LootLama.Min-Players");
    }

    public static int getLootLamaAmount() {
        return RandomEvents.getPlugin().getConfig().getInt("LootLama.Amount");
    }

    public static double getLootLamaChance() {
        return RandomEvents.getPlugin().getConfig().getDouble("LootLama.Spawn-Chance",0.3);
    }

    public static int getRandomProjectilesMinPlayers() {
        return RandomEvents.getPlugin().getConfig().getInt("RandomProjectiles.Min-Players");
    }

    public static int getRandomProjectilesTime() {
        return RandomEvents.getPlugin().getConfig().getInt("RandomProjectiles.Time");
    }

    public static int getDoubleOreMinPlayers() {
        return RandomEvents.getPlugin().getConfig().getInt("DoubleOre.Min-Players");
    }

    public static int getDoubleOreDuration() {
        return RandomEvents.getPlugin().getConfig().getInt("DoubleOre.Duration");
    }
}
