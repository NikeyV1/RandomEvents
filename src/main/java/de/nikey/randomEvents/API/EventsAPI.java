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
}
