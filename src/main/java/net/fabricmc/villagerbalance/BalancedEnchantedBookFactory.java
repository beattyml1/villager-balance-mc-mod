package net.fabricmc.villagerbalance;

import com.mojang.serialization.ListBuilder;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BalancedEnchantedBookFactory implements TradeOffers.Factory {
    private final int experience;
    private final int level;

    public BalancedEnchantedBookFactory(int experience, int level) {
        this.experience = experience;
        this.level = level;
    }

    public static EnchantmentLevelEntry getEnchantmentOnLecternJobSite(VillagerEntity villager) {
        var lectern = Workstations.getVillagerJobSite(villager);
        var book = Workstations.getBookFromLectern(lectern);
        var enchantments = EnchantedBooks.getEnchantments(book);
        if (enchantments.length > 0) {
            Workstations.removeBookFromLectern(lectern);
            return enchantments[0];
        } else return null;
    }

    private static int getPrice(EnchantmentLevelEntry leveledEnchant, Random random) {
        var enchantment = leveledEnchant.enchantment;
        var level = leveledEnchant.level;

        int price = 2 + random.nextInt(5 + level * 10) + 3 * level;
        if (enchantment.isTreasure()) {
            price *= 2;
        }

        if (price > 64) {
            price = 64;
        }
        return price;
    }

    public TradeOffer create(Entity entity, Random random) {
        var villager = (VillagerEntity) entity;

        var leveledEnchant = getEnchantmentOnLecternJobSite(villager);

        if (leveledEnchant == null) {
            var upgrades = EnchantmentUpgrade.getPotentialUpgradedEnchants(villager, level, random);
            if (upgrades.length > 0) {
                leveledEnchant = upgrades[random.nextInt(upgrades.length)];
            } else {
                leveledEnchant = EnchantmentRandomization.getRandomLeveledEnchant(this.level, random);
            }

        }

        ItemStack itemStack = EnchantedBookItem.forEnchantment(leveledEnchant);

        int price = getPrice(leveledEnchant, random);

        return new TradeOffer(new ItemStack(Items.EMERALD, price), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F);
    }
}
