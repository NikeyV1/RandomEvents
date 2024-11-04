package de.nikey.randomEvents.Events;

import de.nikey.randomEvents.General.EventsAPI;
import de.nikey.randomEvents.Loottables.TreasureHuntLootTable;
import de.nikey.randomEvents.RandomEvents;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootContext;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class TreasureHunt implements Listener {

    public static TreasureHuntLootTable lootTable;

    private static void createTreasureChest(Location location) {
        Block block = location.getBlock();
        block.setType(Material.CHEST);

        Inventory chestInventory = ((org.bukkit.block.Chest) block.getState()).getInventory();
        lootTable.fillInventory(chestInventory, new Random(), new LootContext.Builder(location).build());
        lootTable.fillInventory(chestInventory, new Random(), new LootContext.Builder(location).build());
        lootTable.fillInventory(chestInventory, new Random(), new LootContext.Builder(location).build());
        if (chestInventory.isEmpty()) {
            lootTable.fillInventory(chestInventory, new Random(), new LootContext.Builder(location).build());
            lootTable.fillInventory(chestInventory, new Random(), new LootContext.Builder(location).build());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (block.getType() == Material.CHEST) {
                    block.setType(Material.AIR);
                }
            }
        }.runTaskLater(RandomEvents.getPlugin(),20*60*10);
    }

    public static void startTreasureHunt() {
        Location centralLocation = calculateCentralLocation();
        if (centralLocation == null) {
            RandomEvents.getPlugin().getLogger().info("§cKeine geeignete Position für das Schatz-Event gefunden");
            return;
        }

        Random random = new Random();
        World world = centralLocation.getWorld();

        for (int i = 0; i < EventsAPI.getTreasureHuntMaxAttempts(); i++) {
            int x = centralLocation.getBlockX() + random.nextInt(EventsAPI.getTreasureHuntSpawnRadius() * 2) - EventsAPI.getTreasureHuntSpawnRadius();
            int z = centralLocation.getBlockZ() + random.nextInt(EventsAPI.getTreasureHuntSpawnRadius() * 2) - EventsAPI.getTreasureHuntSpawnRadius();
            int y = world.getHighestBlockYAt(x, z);
            Location potentialLocation = new Location(world, x, y+1, z);

            if (isSafeLoc(potentialLocation)) {
                createTreasureChest(potentialLocation.add(0,1,0));
                broadcastTreasureHint(potentialLocation);
                break;
            }
        }
    }

    private static boolean isSafeLoc(Location location) {
        Location loc = location.subtract(0,1,0);
        return loc.getBlock().isSolid() && !loc.getBlock().isLiquid();
    }

    private static void broadcastTreasureHint(Location location) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
        }

        String hint = String.format("§6Ein Schatz wurde in der Nähe von X:%d Z:%d versteckt! §cIhr habt 10 minuten Zeit", location.getBlockX(), location.getBlockZ());
        Bukkit.broadcastMessage(hint);
    }


    private static Location calculateCentralLocation() {
        if (Bukkit.getOnlinePlayers().size() < EventsAPI.getTreasureHuntMinPlayer()) return null;

        double totalX = 0;
        double totalZ = 0;
        World world = null;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Location loc = player.getLocation();
            totalX += loc.getX();
            totalZ += loc.getZ();
            if (world == null) {
                world = loc.getWorld();
            }
        }

        int averageX = (int) (totalX / Bukkit.getOnlinePlayers().size());
        int averageZ = (int) (totalZ / Bukkit.getOnlinePlayers().size());

        return new Location(world, averageX, 0, averageZ);
    }
}
