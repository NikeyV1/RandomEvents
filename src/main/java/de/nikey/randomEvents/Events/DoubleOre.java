package de.nikey.randomEvents.Events;

import de.nikey.randomEvents.API.EventsAPI;
import de.nikey.randomEvents.RandomEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class DoubleOre implements Listener {
    private static boolean isDoubleOreActive = false;
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isDoubleOreActive) return;

        Material blockType = event.getBlock().getType();

        if (blockType.toString().contains("ORE")) {
            event.setDropItems(false);
            for (ItemStack drop : event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand())) {
                drop.setAmount(drop.getAmount() * 2);
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
        }
    }

    public static void activateDoubleOre() {
        isDoubleOreActive = true;
        Bukkit.broadcastMessage("§aDouble Ore Event is now active for !" +EventsAPI.getDoubleOreDuration() + "min");
        Bukkit.broadcastMessage("§aOres now drop double amount");
        Bukkit.getScheduler().runTaskLater(RandomEvents.getPlugin(), DoubleOre::deactivateDoubleOre, 20L * 60 * EventsAPI.getDoubleOreDuration());
    }

    public static void deactivateDoubleOre() {
        isDoubleOreActive = false;
        Bukkit.broadcastMessage("§cDouble Ore Event has ended");
    }
}
