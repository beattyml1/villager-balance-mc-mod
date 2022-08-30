package net.fabricmc.villagerbalance;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;

public class EnchantmentRandomization {
    public static Enchantment getRandomEnchant(int level, Random random) {
        var allEnchants = Registry.ENCHANTMENT.stream();
        var availableEnchants = allEnchants.filter(x -> !x.isTreasure() || level > 1).toList();
        var availableCount = availableEnchants.size();
        return availableEnchants.get(random.nextInt(availableCount));
    }

    public static int getMinEnchantLevel(int villagerLevel, Enchantment enchantment) {
        var min = villagerLevel - 1;
        if (min < 1) return 1;
        var enchantMin = enchantment.getMinLevel();
        var enchantMax = enchantment.getMinLevel();
        if (min < enchantMin) return enchantMin;
        if (min > enchantMax) return enchantMax;
        return min;
    }

    public static int getMaxEnchantLevel(int villagerLevel, Enchantment enchantment) {
        var max = villagerLevel + 1;
        var enchantMin = enchantment.getMinLevel();
        var enchantMax = enchantment.getMinLevel();
        if (max < enchantMin) return enchantMin;
        if (max > enchantMax) return enchantMax;
        if (villagerLevel <= 2 && enchantMax == 3) {
            return villagerLevel;
        }
        return max;
    }

    public static int getEnchantLevel(int villagerLevel, Enchantment enchantment, Random random) {
        var min = getMinEnchantLevel(villagerLevel, enchantment);
        var max = getMaxEnchantLevel(villagerLevel, enchantment);
        return random.nextBetween(min, max);
    }
    public static EnchantmentLevelEntry getRandomLeveledEnchant(int villagerLevel, Random random) {
        var enchant = getRandomEnchant(villagerLevel, random);
        var level = getEnchantLevel(villagerLevel, enchant, random);
        return new EnchantmentLevelEntry(enchant, level);
    }
}
