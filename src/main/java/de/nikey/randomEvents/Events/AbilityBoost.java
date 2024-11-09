package de.nikey.randomEvents.Events;

import de.nikey.randomEvents.API.EventsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AbilityBoost {

    private static final List<PotionEffectType> POSITIVE_EFFECTS = Arrays.asList(
            PotionEffectType.SPEED,             // Speed
            PotionEffectType.HASTE,      // Haste
            PotionEffectType.STRENGTH,   // Strength
            PotionEffectType.JUMP_BOOST,              // Jump Boost
            PotionEffectType.REGENERATION,      // Regeneration
            PotionEffectType.RESISTANCE, // Resistance
            PotionEffectType.FIRE_RESISTANCE,   // Fire Resistance
            PotionEffectType.WATER_BREATHING,   // Water Breathing
            PotionEffectType.INVISIBILITY,      // Invisibility
            PotionEffectType.NIGHT_VISION,      // Night Vision
            PotionEffectType.HEALTH_BOOST,      // Health Boost
            PotionEffectType.ABSORPTION,        // Absorption
            PotionEffectType.SATURATION,        // Saturation
            PotionEffectType.LUCK,              // Luck
            PotionEffectType.SLOW_FALLING,      // Slow Falling
            PotionEffectType.CONDUIT_POWER,     // Conduit Power
            PotionEffectType.DOLPHINS_GRACE,    // Dolphin's Grace
            PotionEffectType.HERO_OF_THE_VILLAGE // Hero of the Village
    );

    public static void activateAbilityBoost() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Choose a random effect from the list
            Random random = new Random();
            PotionEffectType randomEffect = POSITIVE_EFFECTS.get(random.nextInt(POSITIVE_EFFECTS.size()));
            int effectLevel = 1; // Effect level (e.g., level 1 corresponds to level II in Minecraft)

            // Apply the random effect for the specified duration
            player.addPotionEffect(new PotionEffect(randomEffect, EventsAPI.getAbilityBoostDuration(), effectLevel));
            player.sendMessage("§6You've received a §f" + randomEffect.getName() + "§6 boost!");
        }
    }
}
