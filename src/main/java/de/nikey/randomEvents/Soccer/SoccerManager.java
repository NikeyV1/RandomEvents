package de.nikey.randomEvents.Soccer;

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

public class SoccerManager {
    final static String baseMessage = "A soccer game will start in ";

    public static void sendInvite() {
        if (Bukkit.getOnlinePlayers().size() < EventsAPI.getFFAMinPlayer())return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            SoccerAPI.invited.add(player);
            Component message = Component.text("A soccer game has started click here to join").color(NamedTextColor.BLUE).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/soccer join"));
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
            player.sendMessage(message);
        }

        new BukkitRunnable() {
            int timeLeft = EventsAPI.getFFAStartCountdown();

            @Override
            public void run() {
                // Countdown-Benachrichtigung nur bei spezifischen Zeiten senden
                if (timeLeft == 30 || timeLeft == 10 || timeLeft == 5 || timeLeft == 3) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        SoccerAPI.invited.add(player);
                        Component countdownMessage = Component.text(baseMessage + timeLeft + " seconds! Click here to join")
                                .color(NamedTextColor.BLUE)
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/soccer join"));
                        player.sendMessage(countdownMessage);
                    }
                }

                if (timeLeft <= 0) {

                    SoccerAPI.invited.clear();
                    cancel();
                }

                timeLeft--;
            }
        }.runTaskTimer(RandomEvents.getPlugin(), 0, 20);
    }

    private static void teleportPlayersToSoccerWorld() {
        String soccerWorldName = EventsAPI.getFFAWorld();
        World soccerWorld = Bukkit.getWorld(soccerWorldName);

        if (soccerWorld == null) {
            RandomEvents.getPlugin().getLogger().warning("FFA world '" + soccerWorldName + "' not found!");
            return;
        }

        List<String> teleportLocations = EventsAPI.getFFATeleportLocations();
        if (teleportLocations.isEmpty()) {
            RandomEvents.getPlugin().getLogger().warning("No teleport locations configured for FFA world!");
            return;
        }

        if (SoccerAPI.queue.size() < EventsAPI.getFFAMinPlayer()){
            Component message = Component.text("Not enough players in the queue to start the tournament").color(NamedTextColor.RED);
            SoccerAPI.queue.clear();
            Bukkit.broadcast(message);
            return;
        }


        for (Player player : SoccerAPI.queue) {
            if (SoccerAPI.location.containsKey(player.getName()) || SoccerAPI.playerItems.containsKey(player.getName())) {
                TextComponent message = Component.text("An Error occurred: Already ingame");
                TextComponent m = Component.text("Please contact an admin");
                player.sendMessage(message);
                player.sendMessage(m);
                return;
            }
            SoccerAPI.location.put(player.getName(),player.getLocation());

            SoccerAPI.playerEffects.put(player.getName(), player.getActivePotionEffects());
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            int randomIndex = (int) (Math.random() * teleportLocations.size());
            String[] locData = teleportLocations.remove(randomIndex).split(",");

            double x = Double.parseDouble(locData[0]);
            double y = Double.parseDouble(locData[1]);
            double z = Double.parseDouble(locData[2]);
            Location teleportLocation = new Location(soccerWorld, x, y, z);
            player.teleport(teleportLocation);
            player.sendMessage(Component.text("FFA is starting. Good luck!").color(NamedTextColor.GREEN));
            SoccerAPI.ingame.add(player.getName());

            SoccerAPI.playerItems.put(player.getName(),player.getInventory().getContents());
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,100,4));
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            addEquipment(player);
        }
        SoccerAPI.queue.clear();
    }

    private static void addEquipment(Player player) {
        // Diamanthelm mit Verzauberungen
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION, 4);
        helmet.addEnchantment(Enchantment.UNBREAKING, 3);

        // Diamantbrustplatte mit Verzauberungen
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
        ItemStack food = new ItemStack(Material.COOKED_BEEF);

        // Spieler die Ausrüstung geben
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.getInventory().addItem(sword);
        player.getInventory().addItem(food);
    }
}
