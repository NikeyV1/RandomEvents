package de.nikey.randomEvents.FFA;

import de.nikey.randomEvents.API.EventsAPI;
import de.nikey.randomEvents.Loottables.FFALootTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

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
        if (FFA_API.ingame.contains(event.getPlayer().getName())) {
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
        FFA_API.ingame.remove(player.getName());

        World mainWorld = Bukkit.getWorld("world");
        if (mainWorld != null) {
            Location location = FFA_API.location.get(player.getName());
            player.teleport(Objects.requireNonNullElseGet(location, () -> Bukkit.getWorld("world").getSpawnLocation()));
        }
        player.setGameMode(GameMode.SURVIVAL);
        if (FFA_API.playerItems.get(player.getName()) != null) {
            player.getInventory().clear();
            player.getInventory().setContents(FFA_API.playerItems.get(player.getName()));
            FFA_API.playerItems.remove(player.getName());
        }else {
            player.sendMessage(Component.text("Error: items aren't able to be loaded!").color(NamedTextColor.RED));
            player.sendMessage(Component.text("Please contact an admin").color(NamedTextColor.RED));
        }
        player.setHealth(20);
        FFA_API.location.remove(player.getName());
        if (FFA_API.playerEffects.containsKey(player.getName())) {
            for (PotionEffect effect : FFA_API.playerEffects.get(player.getName())) {
                player.addPotionEffect(effect);
            }
            FFA_API.playerEffects.remove(player.getName());
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
                        String first = FFA_API.ingame.getFirst();
                        Player p = Bukkit.getPlayer(first);
                        removePlayerFromFFA(p);
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
                Material.GOLDEN_APPLE,
                Material.GOLDEN_APPLE,
                Material.GOLDEN_APPLE,
                Material.DIAMOND_BLOCK,
                Material.DIAMOND_BLOCK,
                Material.TOTEM_OF_UNDYING,
                Material.NETHERITE_SCRAP,
                Material.NETHERITE_SCRAP,
                Material.ENDER_PEARL,
                Material.WITHER_SKELETON_SKULL,
                Material.ECHO_SHARD,
                Material.ECHO_SHARD,
                Material.BLAZE_ROD,
                Material.BLAZE_ROD,
                Material.SHULKER_SHELL,
                Material.SHULKER_SHELL,
                Material.GOLD_BLOCK,
                Material.GOLD_BLOCK,
                Material.TURTLE_HELMET
        );

        List<Integer> lootAmounts = Arrays.asList(1, 1, 5, 6, 3, 7, 1, 2, 1, 2, 3, 16, 1, 3, 2, 8, 6, 1, 2, 2, 3, 1);
        List<Double> lootChances = Arrays.asList(0.05, 0.05, 0.02, 0.02, 0.02, 0.02, 0.05, 0.04, 0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.03, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05);

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
                String itemName = reward.getType().name().replace("_", " ").toLowerCase(); // Formatierung des Item-Namens
                Component message = Component.text("You have received "  + itemName + "!")
                        .color(TextColor.color(184, 130, 44));
                player.sendMessage(message);
                break;
            }
        }
    }
}
