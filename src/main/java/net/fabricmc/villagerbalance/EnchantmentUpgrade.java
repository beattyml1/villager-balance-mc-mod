package net.fabricmc.villagerbalance;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;

import java.util.Arrays;

public class EnchantmentUpgrade {
    public static EnchantmentLevelEntry[] getPreviousBookTrades(VillagerEntity villager) {
        var offers = villager.getOffers();
        var itemsOffered = Arrays.stream(offers.toArray(TradeOffer[]::new)).map(TradeOffer::getSellItem);
        var booksOffered = itemsOffered.filter(x -> x.getItem() == Items.ENCHANTED_BOOK);
        var enchants = booksOffered.map(EnchantedBooks::getEnchantments).map(x ->x[0]);
        return enchants.toArray(EnchantmentLevelEntry[]::new);
    }

    public static EnchantmentLevelEntry[] getPotentialEnchantsToUpgrade(VillagerEntity villager, int level) {
        var potentials = Arrays.stream(getPreviousBookTrades(villager));
        return potentials
                .filter(x -> x.enchantment.getMaxLevel() > x.level)
                .filter(x -> x.level < EnchantmentRandomization.getMaxEnchantLevel(level + 1, x.enchantment))
                .toArray(EnchantmentLevelEntry[]::new);

    }

    public static EnchantmentLevelEntry[] getPotentialUpgradedEnchants(VillagerEntity villager, int level, Random random) {
        var potentials = Arrays.stream(getPotentialEnchantsToUpgrade(villager, level));
        return potentials
                .map(x -> {
                    var newLevel = EnchantmentRandomization.getEnchantLevel(level + 1, x.enchantment, random);
                    return new EnchantmentLevelEntry(x.enchantment, newLevel > x.level ? newLevel : 0);
                })
                .filter(x -> x.level > 0)
                .toArray(EnchantmentLevelEntry[]::new);
    }
}
