package de.nikey.randomEvents.FFA;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FFA_API {
    public static ArrayList<Player> invited = new ArrayList<>();
    public static ArrayList<Player> queue = new ArrayList<>();
    public static ArrayList<Player> ingame = new ArrayList<>();
    public static HashMap<Player, ItemStack[]> playerItems = new HashMap<>();
    public static HashMap<Player, Location> location = new HashMap<>();
    public static Map<Player, Collection<PotionEffect>> playerEffects = new HashMap<>();
}