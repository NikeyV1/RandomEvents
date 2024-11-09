package de.nikey.randomEvents.API;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class TaskAPI {
    public static boolean isRunning;
    public static int tasksCompleted;
    public static Material requiredItem;
    public static EntityType requiredMob;
    public static int requiredMobKills;
    public static int requiredItems;
    public static final HashMap<String , Integer> playerMobKills = new HashMap<>();
}
