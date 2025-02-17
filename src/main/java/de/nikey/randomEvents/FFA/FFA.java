package de.nikey.randomEvents.FFA;

import de.nikey.randomEvents.API.EventsAPI;
import de.nikey.randomEvents.RandomEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FFA {
    final static String baseMessage = "A FFA tournament will start in ";

    public static void sendInvite() {
        if (Bukkit.getOnlinePlayers().size() < EventsAPI.getFFAMinPlayer())return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            FFA_API.invited.add(player);
            Component message = Component.text("A FFA tournament has started click here to join").color(NamedTextColor.GOLD).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/ffa join"));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
            player.sendMessage(message);
        }

        new BukkitRunnable() {
            int timeLeft = EventsAPI.getFFAStartCountdown();

            @Override
            public void run() {
                if (timeLeft == 30 || timeLeft == 10 || timeLeft == 5 || timeLeft == 3) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        FFA_API.invited.add(player);
                        Component countdownMessage = Component.text(baseMessage + timeLeft + " seconds! Click here to join (" + FFA_API.queue.size() +"/" + EventsAPI.getFFAMinPlayer() + ")")
                                .color(NamedTextColor.GOLD)
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ffa join"));
                        player.sendMessage(countdownMessage);
                    }
                }

                if (timeLeft <= 0) {
                    teleportPlayersToFFAWorld();
                    FFA_API.invited.clear();
                    cancel();
                }

                timeLeft--;
            }
        }.runTaskTimer(RandomEvents.getPlugin(), 0, 20);
    }

    private static void teleportPlayersToFFAWorld() {
        String ffaWorldName = EventsAPI.getFFAWorld();
        World ffaWorld = Bukkit.getWorld(ffaWorldName);

        if (ffaWorld == null) {
            RandomEvents.getPlugin().getLogger().warning("FFA world '" + ffaWorldName + "' not found!");
            return;
        }

        List<String> teleportLocations = EventsAPI.getFFATeleportLocations();
        if (teleportLocations.isEmpty()) {
            RandomEvents.getPlugin().getLogger().warning("No teleport locations configured for FFA world!");
            return;
        }

        if (FFA_API.queue.size() < EventsAPI.getFFAMinPlayer()){
            Component message = Component.text("Not enough players in the queue to start the tournament").color(NamedTextColor.RED);
            FFA_API.queue.clear();
            Bukkit.broadcast(message); 
            return;
        }


        for (Player player : FFA_API.queue) {
            if (!player.isOnline()) continue;

            if (FFA_API.location.containsKey(player.getName()) || FFA_API.playerItems.containsKey(player.getName())) {
                TextComponent message = Component.text("An Error occurred: Already ingame");
                TextComponent m = Component.text("Please contact an admin");
                player.sendMessage(message);
                player.sendMessage(m);
                return;
            }
            FFA_API.location.put(player.getName(),player.getLocation());

            FFA_API.playerEffects.put(player.getName(), player.getActivePotionEffects());
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            int randomIndex = (int) (Math.random() * teleportLocations.size());
            String[] locData = teleportLocations.remove(randomIndex).split(",");

            double x = Double.parseDouble(locData[0]);
            double y = Double.parseDouble(locData[1]);
            double z = Double.parseDouble(locData[2]);
            Location teleportLocation = new Location(ffaWorld, x, y, z);
            player.teleport(teleportLocation);
            player.sendMessage(Component.text("FFA is starting. Good luck!").color(NamedTextColor.GREEN));
            FFA_API.ingame.add(player.getName());

            FFA_API.playerItems.put(player.getName(),player.getInventory().getContents());
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,100,4));
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            addEquipment(player);
        }
        FFA_API.queue.clear();
    }

    private static void addEquipment(Player player) {
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION, 4);
        helmet.addEnchantment(Enchantment.UNBREAKING, 3);

        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION, 4);
        chestplate.addEnchantment(Enchantment.UNBREAKING, 3);

        // Diamanthose mit Verzauberungen
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION, 4);
        leggings.addEnchantment(Enchantment.UNBREAKING, 3);

        // Diamantstiefel mit Verzauberungen
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION, 4);
        boots.addEnchantment(Enchantment.UNBREAKING, 3);
        boots.addEnchantment(Enchantment.FEATHER_FALLING, 4);

        // Diamantschwert mit Verzauberungen
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.SHARPNESS, 5);
        sword.addEnchantment(Enchantment.SWEEPING_EDGE, 3);

        //Essen
        ItemStack food = new ItemStack(Material.GOLDEN_APPLE);

        // Spieler die Ausrüstung geben
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.getInventory().addItem(sword);
        player.getInventory().addItem(food);
    }

}
