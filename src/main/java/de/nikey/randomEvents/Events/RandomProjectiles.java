package de.nikey.randomEvents.Events;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import de.nikey.randomEvents.API.EventsAPI;
import de.nikey.randomEvents.RandomEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomProjectiles implements Listener {

    private static boolean randomProjectilesEnabled = false;
    private static final List<EntityType> projectileTypes = new ArrayList<>();
    private final Random random = new Random();

    public static void start() {
        Bukkit.broadcast(Component.text("The Random Projectiles event has started. All shot projektiles are now randomized").color(TextColor.color(177, 24, 219)));
        randomProjectilesEnabled = true;

        projectileTypes.clear();
        projectileTypes.add(EntityType.ARROW);
        projectileTypes.add(EntityType.SPECTRAL_ARROW);
        projectileTypes.add(EntityType.POTION);
        projectileTypes.add(EntityType.SNOWBALL);
        projectileTypes.add(EntityType.EGG);
        projectileTypes.add(EntityType.FIREBALL);
        projectileTypes.add(EntityType.DRAGON_FIREBALL);
        projectileTypes.add(EntityType.SHULKER_BULLET);
        projectileTypes.add(EntityType.TRIDENT);
        projectileTypes.add(EntityType.LLAMA_SPIT);
        projectileTypes.add(EntityType.WITHER_SKULL);
        projectileTypes.add(EntityType.FIREWORK_ROCKET);

        new BukkitRunnable() {
            @Override
            public void run() {
                randomProjectilesEnabled = false;
                Bukkit.broadcast(Component.text("The Random Projectiles event has ended").color(TextColor.color(177, 24, 219)));
            }
        }.runTaskLater(RandomEvents.getPlugin(), 20L *60* EventsAPI.getRandomProjectilesTime());
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!randomProjectilesEnabled) return;

        Projectile original = event.getEntity();

        // Ignore projectiles we already replaced
        if (original.getCustomName() != null && original.getCustomName().equalsIgnoreCase("pa1"))return;

        if (original.getShooter() != null) {
            if (original.getShooter() instanceof LivingEntity living) {
                EntityType randomType = projectileTypes.get(random.nextInt(projectileTypes.size()));

                Projectile newProjectile = (Projectile) living.getWorld().spawnEntity(original.getLocation(), randomType);
                newProjectile.setCustomName("pa1");
                newProjectile.setVelocity(living.getLocation().getDirection().multiply(2.4));
                original.remove();
            }
        }
    }
}
