package net.fabricmc.villagerbalance;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

public class EnchantedBookFromLecternOrRandom implements TradeOffers.Factory {
    private final int experience;
    private TradeOffers.Factory fallback;

    public EnchantedBookFromLecternOrRandom(int experience, TradeOffers.Factory fallback) {
        this.experience = experience;
        this.fallback = fallback;
    }

    public static EnchantmentLevelEntry getEnchantmentOnLecternJobSite(VillagerEntity villager) {
        var lectern = Workstations.getVillagerJobSite(villager);
        VillagerBalanceMod.LOGGER.info(lectern != null ? lectern.toString() : "null");
        var book = Workstations.getBookFromLectern(lectern);
        VillagerBalanceMod.LOGGER.info(book != null ? book.toString() : "null");
        var enchantments = EnchantedBooks.getEnchantments(book);
        VillagerBalanceMod.LOGGER.info(enchantments != null ? enchantments.toString() : "null");
        return enchantments.length > 0 ? enchantments[0] : null;
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
        VillagerBalanceMod.LOGGER.info(leveledEnchant != null ? leveledEnchant.toString() : "null");

        if (leveledEnchant == null)
            return fallback.create(entity, random);

        ItemStack itemStack = EnchantedBookItem.forEnchantment(leveledEnchant);

        int price = getPrice(leveledEnchant, random);

        return new TradeOffer(new ItemStack(Items.EMERALD, price), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F);
    }
}
