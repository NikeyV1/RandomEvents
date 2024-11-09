package de.nikey.randomEvents.Events;

import de.nikey.randomEvents.API.EventsAPI;
import de.nikey.randomEvents.API.FishingAPI;
import de.nikey.randomEvents.RandomEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FishingContest implements Listener {

    public static void startFishingContest() {
        if (FishingAPI.isFishingContestActive) return;
        if (Bukkit.getOnlinePlayers().size() < EventsAPI.getFishingContestMinPlayer())return;

        FishingAPI.isFishingContestActive = true;
        Bukkit.broadcast(Component.text("The Fishing Contest has started! Catch as many rare fish and treasures as you can!", NamedTextColor.GOLD));
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                FishingAPI.isFishingContestActive = false;
                Bukkit.broadcast(Component.text("The Fishing Contest has ended!", NamedTextColor.BLUE));
            }
        }.runTaskLater(RandomEvents.getPlugin(), (long) EventsAPI.getFishingContestTime() * 60 * 20);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Random random = new Random();
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && FishingAPI.isFishingContestActive) {
            int i = random.nextInt(100);
            if (i <= 4) {
                event.getPlayer().sendMessage(Component.text("You caught a rare drop!").color(NamedTextColor.DARK_GRAY));
                ItemStack itemStack = giveRandomLoot();
                Item item = event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), itemStack);
                event.getHook().setHookedEntity(item);
            }
        }
    }

    public static ItemStack giveRandomLoot() {
        // Loot-Listen (Material, Menge, Chance)
        List<Material> lootMaterials = Arrays.asList(
                Material.DIAMOND_BLOCK,
                Material.DIAMOND_BLOCK,
                Material.EMERALD_BLOCK,
                Material.EMERALD_BLOCK,
                Material.EMERALD_BLOCK,
                Material.GOLD_BLOCK,
                Material.GOLD_BLOCK,
                Material.TRIDENT,
                Material.HEAVY_CORE,
                Material.TOTEM_OF_UNDYING,
                Material.ELYTRA,
                Material.SHULKER_SHELL,
                Material.SHULKER_SHELL,
                Material.ENDER_PEARL,
                Material.ENDER_PEARL,
                Material.ECHO_SHARD,
                Material.ECHO_SHARD,
                Material.NETHER_STAR,
                Material.EXPERIENCE_BOTTLE,
                Material.EXPERIENCE_BOTTLE,
                Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Material.IRON_BLOCK,
                Material.IRON_BLOCK,
                Material.ANCIENT_DEBRIS
        );

        List<Integer> lootAmounts = Arrays.asList(1,2,1,2,3,1,2,1,1,1,1,1,1,16,14,2,3,1,8,10,1,2,3,1);
        List<Double> lootChances = Arrays.asList(0.05,0.02,0.05,0.05,0.03,0.05,0.05,0.01,0.01,0.03,0.05,0.05,0.05,0.05,0.05,0.05,0.05,0.01,0.05,0.05,0.04,0.05,0.05,0.05);

        Random random = new Random();
        double totalWeight = lootChances.stream().mapToDouble(Double::doubleValue).sum();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0;

        // Auswahl eines zufälligen Items basierend auf den Wahrscheinlichkeiten
        for (int i = 0; i < lootMaterials.size(); i++) {
            cumulativeWeight += lootChances.get(i);
            if (randomValue <= cumulativeWeight) {

                return new ItemStack(lootMaterials.get(i), lootAmounts.get(i));
            }
        }
        return null;  // Falls keine Auswahl getroffen wird, null zurückgeben
    }

}
