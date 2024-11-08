package de.nikey.randomEvents.FFA;

import de.nikey.randomEvents.General.EventsAPI;
import de.nikey.randomEvents.Loottables.FFALootTable;
import de.nikey.randomEvents.Loottables.TreasureHuntLootTable;
import de.nikey.randomEvents.RandomEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FFAIngame implements Listener {

    public static FFALootTable lootTable;
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        World world = Bukkit.getWorld(EventsAPI.getFFAWorld());
        if (world == null)return;
        if (player.getWorld() == world && player.getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (FFA_API.ingame.contains(event.getPlayer())) {
            removePlayerFromFFA(event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDamager() instanceof Player damager)) return;

        World ffaWorld = Bukkit.getWorld(EventsAPI.getFFAWorld());

        // Überprüfen, ob der Spieler in der FFA-Welt ist
        if (ffaWorld != null && player.getWorld().equals(ffaWorld)) {
            // Berechne die verbleibende Gesundheit nach dem Schaden
            double finalHealth = player.getHealth() - event.getFinalDamage();

            // Schaden nur abbrechen, wenn der Spieler daran sterben würde
            if (finalHealth <= 0) {
                event.setCancelled(true);
                removePlayerFromFFA(player);

                if (damager.getHealth() <= 15) {
                    damager.setHealth(damager.getHealth()+5);
                }
            }
        }
    }


    private static void removePlayerFromFFA(Player player) {
        FFA_API.ingame.remove(player);

        World mainWorld = Bukkit.getWorld("world");
        if (mainWorld != null) {
            Location location = FFA_API.location.get(player);
            player.teleport(Objects.requireNonNullElseGet(location, () -> Bukkit.getWorld("world").getSpawnLocation()));
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setHealth(20);
        player.getInventory().setContents(FFA_API.playerItems.get(player));
        FFA_API.playerItems.remove(player);
        FFA_API.location.remove(player);
        if (FFA_API.playerEffects.containsKey(player)) {
            for (PotionEffect effect : FFA_API.playerEffects.get(player)) {
                player.addPotionEffect(effect);
            }
            FFA_API.playerEffects.remove(player);
        }
        if (FFA_API.ingame.isEmpty()) {
            giveRandomLoot(player);
            player.sendMessage(Component.text("You have won the FFA!").color(NamedTextColor.GOLD));
            Component eliminationMessage = Component.text(player.getName() + " has won the FFA!").color(NamedTextColor.GOLD);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer != player) {
                    onlinePlayer.sendMessage(eliminationMessage);
                }
            }
        }else {
            player.sendMessage(Component.text("You have been eliminated from the FFA!").color(NamedTextColor.RED));
            Component eliminationMessage = Component.text(player.getName() + " has been eliminated from the FFA!").color(NamedTextColor.GOLD);
            Component playersMessage = Component.text(FFA_API.ingame.size() + " players left").color(NamedTextColor.GOLD);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer != player) {
                    onlinePlayer.sendMessage(eliminationMessage);
                    if (FFA_API.ingame.size() == 1) {
                        removePlayerFromFFA(onlinePlayer);
                    }
                    onlinePlayer.sendActionBar(playersMessage);
                }
            }
        }
    }

    public static void giveRandomLoot(Player player) {
        // Loot-Listen (Material, Menge, Chance)
        List<Material> lootMaterials = Arrays.asList(
                Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Material.ENCHANTED_GOLDEN_APPLE,
                Material.GOLDEN_APPLE,
                Material.DIAMOND_BLOCK,
                Material.TOTEM_OF_UNDYING,
                Material.NETHERITE_SCRAP,
                Material.ENDER_PEARL,
                Material.WITHER_SKELETON_SKULL,
                Material.ECHO_SHARD,
                Material.BLAZE_ROD,
                Material.SHULKER_SHELL,
                Material.GOLD_BLOCK,
                Material.TURTLE_HELMET
        );

        List<Integer> lootAmounts = Arrays.asList(1, 1, 5, 1, 1, 2, 16, 1, 3, 8, 1, 2, 1);
        List<Double> lootChances = Arrays.asList(0.05, 0.05, 0.02, 0.05, 0.1, 0.05, 0.05, 0.05, 0.05, 0.03, 0.05, 0.05, 0.05);

        Random random = new Random();
        double totalWeight = lootChances.stream().mapToDouble(Double::doubleValue).sum();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0;

        // Auswahl eines zufälligen Items basierend auf den Wahrscheinlichkeiten
        for (int i = 0; i < lootMaterials.size(); i++) {
            cumulativeWeight += lootChances.get(i);
            if (randomValue <= cumulativeWeight) {
                ItemStack reward = new ItemStack(lootMaterials.get(i), lootAmounts.get(i));
                player.getInventory().addItem(reward);
                break;
            }
        }
    }
}
