package de.nikey.randomEvents.FFA;

import de.nikey.randomEvents.General.EventsAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

public class FFAIngame implements Listener {
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
        removePlayerFromFFA(event.getPlayer());
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
                    onlinePlayer.sendActionBar(playersMessage);
                }
            }
        }
    }
}
