package de.nikey.randomEvents.General;

import de.nikey.randomEvents.RandomEvents;

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
}
