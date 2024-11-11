package de.nikey.randomEvents.Events;

import de.nikey.randomEvents.API.EventsAPI;
import de.nikey.randomEvents.API.TaskAPI;
import de.nikey.randomEvents.RandomEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TaskEvent implements Listener {

    private static BukkitTask runnable;

    public static void startEvent() {
        TaskAPI.tasksCompleted = 0;
        initializeRandomTask();
    }

    public static void initializeRandomTask() {
        Random random = new Random();

        TaskAPI.requiredItem = null;
        TaskAPI.requiredItems = 0;
        TaskAPI.requiredMob = null;
        TaskAPI.requiredMobKills = 0;
        TaskAPI.playerMobKills.clear();
        TaskAPI.isRunning = true;
        int i = random.nextInt(2);
        if (i == 0) {
            TaskAPI.requiredItem = getValidMaterial(Material.values(),random);
            TaskAPI.requiredItems = random.nextInt(EventsAPI.getTaskMaxItems()) + EventsAPI.getTaskMinItems();
            Component message = Component.text("New Task: Collect ",NamedTextColor.AQUA)
                    .append(Component.text(TaskAPI.requiredItems).color(NamedTextColor.WHITE))
                    .append(Component.text(" " + TaskAPI.requiredItem.name()).color(NamedTextColor.WHITE));
            Bukkit.broadcast(message);
        }else {
            TaskAPI.requiredMob = getValidEntityType(EntityType.values(),random);
            TaskAPI.requiredMobKills = random.nextInt(EventsAPI.getTaskMaxMobs()) + EventsAPI.getTaskMinMobs();
            Component message = Component.text("New Task: Kill ",NamedTextColor.AQUA)
                    .append(Component.text(TaskAPI.requiredMobKills).color(NamedTextColor.WHITE))
                    .append(Component.text(" " + TaskAPI.requiredMob.name()).color(NamedTextColor.WHITE));
            Bukkit.broadcast(message);
        }
        Bukkit.broadcast(Component.text("You have " + EventsAPI.getTaskTime()+ " minutes time").color(NamedTextColor.YELLOW));
        if (runnable != null) {
            runnable.cancel();
        }

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                TaskAPI.isRunning = false;
                Bukkit.broadcast(Component.text("Task event ended").color(NamedTextColor.GOLD));
            }
        }.runTaskLater(RandomEvents.getPlugin(), 20L *60*EventsAPI.getTaskTime());
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player player && TaskAPI.isRunning) {
            if (event.getEntityType() == TaskAPI.requiredMob) {
                String playerName = player.getName();
                int kills = TaskAPI.playerMobKills.getOrDefault(playerName, 0) + 1;
                TaskAPI.playerMobKills.put(playerName, kills);

                if (kills >= TaskAPI.requiredMobKills) {
                    Bukkit.broadcast(Component.text("Mob-Kill-Task completed by "+ player.getName()).color(NamedTextColor.GRAY));
                    player.getWorld().dropItem(player.getLocation(),giveRandomLoot());
                    giveRandomExp(player);
                    TaskAPI.tasksCompleted = TaskAPI.tasksCompleted+1;
                    if (TaskAPI.tasksCompleted < EventsAPI.getTaskAmount()) {
                        initializeRandomTask();
                    }else {
                        TaskAPI.isRunning = false;
                        runnable.cancel();
                        Bukkit.broadcast(Component.text("Task event completed!").color(NamedTextColor.GOLD));
                    }
                } else {
                    player.sendActionBar(Component.text("You killed "+ kills + "/" + TaskAPI.requiredMobKills + " " + TaskAPI.requiredMob.name()));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!TaskAPI.isRunning)return;
        Player player = (Player) event.getWhoClicked();
        if (TaskAPI.requiredItem != null) {
            int itemCount = getItemCount(player, TaskAPI.requiredItem);

            if (itemCount >= TaskAPI.requiredItems) {
                Bukkit.broadcast(Component.text("Item-Collect-Task completed by "+ player.getName()).color(NamedTextColor.GRAY));
                player.getWorld().dropItem(player.getLocation(),giveRandomLoot());
                giveRandomExp(player);
                TaskAPI.tasksCompleted = TaskAPI.tasksCompleted+1;
                if (TaskAPI.tasksCompleted < EventsAPI.getTaskAmount()) {
                    initializeRandomTask();
                }else {
                    TaskAPI.isRunning = false;
                    runnable.cancel();
                    Bukkit.broadcast(Component.text("Task event completed!").color(NamedTextColor.GOLD));
                }
            } else {
                player.sendActionBar(Component.text("You have "+ itemCount + "/" + TaskAPI.requiredItems + " " + TaskAPI.requiredItem.name()));
            }
        }
    }

    public static void giveRandomExp(Player player) {
        Random random = new Random();
        player.giveExp(random.nextInt(300),false);
    }


    public static ItemStack giveRandomLoot() {
        // Loot-Listen (Material, Menge, Chance)
        List<Material> lootMaterials = Arrays.asList(
                Material.DIAMOND_BLOCK,
                Material.DIAMOND_BLOCK,
                Material.EMERALD_BLOCK,
                Material.EMERALD_BLOCK,
                Material.GOLD_BLOCK,
                Material.GOLD_BLOCK,
                Material.TOTEM_OF_UNDYING,
                Material.ECHO_SHARD,
                Material.ECHO_SHARD,
                Material.EXPERIENCE_BOTTLE,
                Material.EXPERIENCE_BOTTLE,
                Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Material.IRON_BLOCK,
                Material.IRON_BLOCK,
                Material.NETHERITE_SCRAP,
                Material.NETHERITE_SCRAP
        );

        List<Integer> lootAmounts = Arrays.asList(1,2,2,3,1,2,1,1,2,8,11,1,2,3,1,2);
        List<Double> lootChances = Arrays.asList(0.05,0.05,0.075,0.075,0.075,0.075,0.05,0.05,0.05,0.075,0.075,0.05,0.075,0.075,0.05,0.05);

        Random random = new Random();
        double totalWeight = lootChances.stream().mapToDouble(Double::doubleValue).sum();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0;

        for (int i = 0; i < lootMaterials.size(); i++) {
            cumulativeWeight += lootChances.get(i);
            if (randomValue <= cumulativeWeight) {

                return new ItemStack(lootMaterials.get(i), lootAmounts.get(i));
            }
        }
        return null;
    }

    private int getItemCount(Player player, Material material) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }


    public static Material getValidMaterial(Material[] materials, Random random) {
        Material material = materials[random.nextInt(materials.length)];
        if (!material.isItem()) {
            return getValidMaterial(materials, random);
        }
        return material;
    }

    public static EntityType getValidEntityType(EntityType[] entityTypes, Random random) {
        EntityType entityType = entityTypes[random.nextInt(entityTypes.length)];
        if (!entityType.isAlive() || entityType == EntityType.PLAYER) {
            return getValidEntityType(entityTypes, random);  // Rekursive Methode
        }
        return entityType;
    }
}
