package de.nikey.randomEvents.Events;

import de.nikey.randomEvents.API.EventsAPI;
import de.nikey.randomEvents.RandomEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static de.nikey.randomEvents.API.GeneralAPI.randomAmount;

public class LootLama implements Listener {

    public static final List<Llama> lootLlamas = new ArrayList<>();

    public static void start() {
        tries.clear();
        if (!lootLlamas.isEmpty()) {
            for (Llama llama : lootLlamas) {
                lootLlamas.remove(llama);
                llama.remove();
            }
        }

        new BukkitRunnable() {
            int times = 0;
            final Random random = new Random();
            @Override
            public void run() {
                if (times == EventsAPI.getLootLamaAmount()) {
                    RandomEvents.getPlugin().getLogger().severe("Finished spawning llamas");
                    cancel();
                    return;
                }

                World world = Bukkit.getWorld("world");
                if (world == null)return;

                for (Player player : world.getPlayers()) {
                    if (random.nextDouble() <= EventsAPI.getLootLamaChance()) {
                        spawnLootLlamaNearPlayer(player);
                    }
                }
                times++;
            }
        }.runTaskTimer(RandomEvents.getPlugin(),0,20*12);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!lootLlamas.isEmpty()) {
                    for (Llama llama : lootLlamas) {
                        lootLlamas.remove(llama);
                        llama.remove();
                    }
                }
            }
        }.runTaskLater(RandomEvents.getPlugin(),20*60*10);

        Bukkit.broadcast(Component.text("The Loot Llama Event has started! Llamas are appearing ...").color(NamedTextColor.AQUA));
    }

    private static HashMap<Player, Integer> tries = new HashMap<>();

    private static void spawnLootLlamaNearPlayer(Player player) {
        Random random = new Random();
        World world = player.getWorld();
        double x = player.getLocation().getX() + random.nextInt(151) - 50;
        double z = player.getLocation().getZ() + random.nextInt(151) - 50;
        double y = world.getHighestBlockYAt((int) x, (int) z) + 1;

        Location location = new Location(world, x, y, z);
        tries.putIfAbsent(player,1);
        if (tries.get(player) >= 10)return;
        if (!location.subtract(0,1,0).getBlock().isSolid()) {
            tries.put(player,tries.get(player)+1);
            spawnLootLlamaNearPlayer(player);
            return;
        }
        tries.remove(player);
        location.add(0,1,0);

        Llama llama = (Llama) world.spawnEntity(location, EntityType.LLAMA);
        llama.getPersistentDataContainer().set(new NamespacedKey(RandomEvents.getPlugin(),"lootllama"), PersistentDataType.BOOLEAN,true);
        llama.customName(Component.text("Loot Lama").color(NamedTextColor.GOLD));
        llama.setCustomNameVisible(true);
        llama.setCarryingChest(true);
        llama.setGlowing(true);
        lootLlamas.add(llama);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Llama llama) {
            if (llama.getPersistentDataContainer().has(new NamespacedKey(RandomEvents.getPlugin(),"lootllama"))) {
                llama.getWorld().spawnParticle(Particle.POOF,llama.getLocation().add(0,1,0),0);
                lootLlamas.remove(llama);
                llama.remove();
                giveRandomLoot(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }

    public static void giveRandomLoot(Player player) {
        // Loot-Listen (Material, Menge, Chance)
        List<Material> lootMaterials = Arrays.asList(
                Material.DIAMOND,
                Material.EMERALD,
                Material.NETHERITE_SCRAP,
                Material.ENCHANTED_GOLDEN_APPLE,
                Material.TOTEM_OF_UNDYING,
                Material.GOLD_INGOT,
                Material.IRON_INGOT,
                Material.EXPERIENCE_BOTTLE,
                Material.ENCHANTED_BOOK
        );

        List<Integer> lootAmounts = Arrays.asList(randomAmount(6, 16),randomAmount(12, 32),randomAmount(1, 3),1,1, randomAmount(10, 20), randomAmount(15, 25),randomAmount(10, 18), randomAmount(1,2));
        List<Double> lootChances = Arrays.asList(0.15, 0.15, 0.1, 0.05, 0.05, 0.15, 0.15, 0.1, 0.1);

        Random random = new Random();
        double totalWeight = lootChances.stream().mapToDouble(Double::doubleValue).sum();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0;

        // Auswahl eines zufälligen Items basierend auf den Wahrscheinlichkeiten
        for (int i = 0; i < lootMaterials.size(); i++) {
            cumulativeWeight += lootChances.get(i);
            if (randomValue <= cumulativeWeight) {
                ItemStack reward;

                if (lootMaterials.get(i) == Material.ENCHANTED_BOOK){
                    reward = createBook();
                }else {
                    reward = new ItemStack(lootMaterials.get(i), lootAmounts.get(i));
                }
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItem(player.getLocation(),reward);
                }else {
                    player.getInventory().addItem(reward);
                }
                String itemName = reward.getType().name().replace("_", " ").toLowerCase();
                Component message = Component.text("You have received: "  + itemName + "!")
                        .color(TextColor.color(184, 130, 44));
                player.sendMessage(message);
                break;
            }
        }
    }

    public static ItemStack createBook() {
        Random random = new Random();
        ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();

        if (meta != null) {
            Enchantment[] enchantments = Enchantment.values();
            Enchantment randomEnchantment = enchantments[random.nextInt(enchantments.length)];
            int level = randomEnchantment.getMaxLevel();
            meta.addStoredEnchant(randomEnchantment, level,false);
            enchantedBook.setItemMeta(meta);
        }

        return enchantedBook;
    }

}
