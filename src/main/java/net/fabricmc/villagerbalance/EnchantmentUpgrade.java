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
                .toArray(EnchantmentLevelEntry[]::new);

    }

    public static EnchantmentLevelEntry[] getPotentialUpgradedEnchants(VillagerEntity villager, int level, Random random) {
        if (level <= 3) return new EnchantmentLevelEntry[]{};
        var potentials = Arrays.stream(getPotentialEnchantsToUpgrade(villager, level));
        return potentials
                .map(x -> new EnchantmentLevelEntry(x.enchantment, x.enchantment.getMaxLevel()))
                .toArray(EnchantmentLevelEntry[]::new);
    }
}
