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

public class TreasureHuntLootTable implements LootTable {
    private final List<LootItem> lootItems;
    private final NamespacedKey key;

    public TreasureHuntLootTable(NamespacedKey key) {
        this.lootItems = new ArrayList<>();
        this.key = key;

        // Beispielhafte Beute hinzufügen
        lootItems.add(new LootItem(Material.DIAMOND, 11, 0.02));
        lootItems.add(new LootItem(Material.DIAMOND, 12, 0.02));
        lootItems.add(new LootItem(Material.DIAMOND, 13, 0.02));
        lootItems.add(new LootItem(Material.DIAMOND, 14, 0.02));
        lootItems.add(new LootItem(Material.DIAMOND, 15, 0.02));
        lootItems.add(new LootItem(Material.DIAMOND, 16, 0.02));
        lootItems.add(new LootItem(Material.EMERALD_BLOCK, 2, 0.05));
        lootItems.add(new LootItem(Material.EMERALD_BLOCK, 3, 0.04));
        lootItems.add(new LootItem(Material.EMERALD_BLOCK, 4, 0.03));
        lootItems.add(new LootItem(Material.EMERALD_BLOCK, 5, 0.02));
        lootItems.add(new LootItem(Material.NETHERITE_SCRAP, 2, 0.03));
        lootItems.add(new LootItem(Material.NETHERITE_SCRAP, 3, 0.04));
        lootItems.add(new LootItem(Material.ECHO_SHARD, 3, 0.04));
        lootItems.add(new LootItem(Material.ECHO_SHARD, 4, 0.04));
        lootItems.add(new LootItem(Material.GOLDEN_APPLE, 4, 0.04));
        lootItems.add(new LootItem(Material.GOLDEN_APPLE, 5, 0.05));
        lootItems.add(new LootItem(Material.GOLDEN_APPLE, 6, 0.06));
        lootItems.add(new LootItem(Material.ENCHANTED_GOLDEN_APPLE, 1, 0.02));
        lootItems.add(new LootItem(Material.TOTEM_OF_UNDYING, 1, 0.09));
        lootItems.add(new LootItem(Material.GOLD_INGOT, 17, 0.03));
        lootItems.add(new LootItem(Material.GOLD_INGOT, 18, 0.03));
        lootItems.add(new LootItem(Material.GOLD_INGOT, 19, 0.04));
        lootItems.add(new LootItem(Material.WITHER_SKELETON_SKULL, 1, 0.08));
    }

    @Override
    public @NotNull Collection<ItemStack> populateLoot(Random random, @NotNull LootContext lootContext) {
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
