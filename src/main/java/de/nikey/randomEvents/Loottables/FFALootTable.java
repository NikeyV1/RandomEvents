package de.nikey.randomEvents.Loottables;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FFALootTable implements LootTable {
    private final List<LootItem> lootItems;
    private final NamespacedKey key;

    public FFALootTable(NamespacedKey key) {
        this.lootItems = new ArrayList<>();
        this.key = key;

        // Beispielhafte Beute hinzufügen
        lootItems.add(new LootItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1, 0.05));
        lootItems.add(new LootItem(Material.ENCHANTED_GOLDEN_APPLE, 1, 0.05));
        lootItems.add(new LootItem(Material.GOLDEN_APPLE, 5, 0.02));
        lootItems.add(new LootItem(Material.GOLDEN_APPLE, 6, 0.02));
        lootItems.add(new LootItem(Material.GOLDEN_APPLE, 3, 0.02));
        lootItems.add(new LootItem(Material.GOLDEN_APPLE, 7, 0.02));
        lootItems.add(new LootItem(Material.DIAMOND_BLOCK, 1, 0.05));
        lootItems.add(new LootItem(Material.DIAMOND_BLOCK, 2, 0.04));
        lootItems.add(new LootItem(Material.TOTEM_OF_UNDYING, 1, 0.1));
        lootItems.add(new LootItem(Material.NETHERITE_SCRAP, 2, 0.05));
        lootItems.add(new LootItem(Material.NETHERITE_SCRAP, 3, 0.05));
        lootItems.add(new LootItem(Material.ENDER_PEARL, 16, 0.05));
        lootItems.add(new LootItem(Material.WITHER_SKELETON_SKULL, 1, 0.05));
        lootItems.add(new LootItem(Material.ECHO_SHARD, 3, 0.05));
        lootItems.add(new LootItem(Material.ECHO_SHARD, 2, 0.05));
        lootItems.add(new LootItem(Material.BLAZE_ROD, 8, 0.03));
        lootItems.add(new LootItem(Material.BLAZE_ROD, 6, 0.05));
        lootItems.add(new LootItem(Material.SHULKER_SHELL, 1, 0.05));
        lootItems.add(new LootItem(Material.SHULKER_SHELL, 2, 0.05));
        lootItems.add(new LootItem(Material.GOLD_BLOCK, 2, 0.05));
        lootItems.add(new LootItem(Material.GOLD_BLOCK, 3, 0.05));
        lootItems.add(new LootItem(Material.TURTLE_HELMET, 1, 0.05));
    }

    @Override
    public @NotNull Collection<ItemStack> populateLoot(Random random, LootContext lootContext) {
        List<ItemStack> loot = new ArrayList<>();

        for (LootItem lootItem : lootItems) {
            if (random.nextDouble() < lootItem.getChance()) {
                loot.add(new ItemStack(lootItem.getMaterial(), lootItem.getAmount()));
            }
        }

        return loot;
    }

    @Override
    public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext lootContext) {
        Collection<ItemStack> loot = populateLoot(random, lootContext);
        for (ItemStack item : loot) {
            inventory.addItem(item);
        }
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    private static class LootItem {
        private final Material material;
        private final int amount;
        private final double chance;

        public LootItem(Material material, int amount, double chance) {
            this.material = material;
            this.amount = amount;
            this.chance = chance;
        }

        public Material getMaterial() {
            return material;
        }

        public int getAmount() {
            return amount;
        }

        public double getChance() {
            return chance;
        }
    }
}
