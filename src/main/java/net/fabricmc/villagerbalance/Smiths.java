package net.fabricmc.villagerbalance;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.ArrayList;
import java.util.Arrays;

public class Smiths {
    private static final int[] experienceLevels = new int[] {0, 1, 5, 10, 15, 20};
    public static void setupTrades() {
        setupForSpecificSmith(VillagerProfession.WEAPONSMITH);
        setupForSpecificSmith(VillagerProfession.TOOLSMITH);
        setupForSpecificSmith(VillagerProfession.ARMORER);
    }

    public static void setupForSpecificSmith(VillagerProfession profession) {
        var smithOffers = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(profession);
        var builder = ImmutableMap.builder();
        for (int i = 1; i <= 5; i++){
            var levelOffers =  smithOffers.get(i);
            final int level = i;
            var offers = new ArrayList<>(Arrays
                    .stream(levelOffers)
                    .map(x -> isDiamondTradeFactory(x) ? getDiamondFactory(x) : x)
                    .toList());

            builder.put(i, offers.toArray(TradeOffers.Factory[]::new));
        }
        var newSmithOffers = new Int2ObjectOpenHashMap(builder.build());
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(profession, newSmithOffers);
    }

    private static TradeOffers.Factory getDiamondFactory(TradeOffers.Factory factory) {
        int experience = 20;
        try {
            experience = (int) Access.getPrivateField(factory, "experience");
        } catch (NoSuchFieldException | IllegalAccessException e) { }
        return new BuyOneForEmeraldsFactory(Items.DIAMOND, 6, 12, experience);
    }

    private static boolean isDiamondTradeFactory(TradeOffers.Factory factory) {
        var isBuyForOneEmerald = factory.getClass().getName().contains("BuyForOneEmeraldFactory");
        if (!isBuyForOneEmerald) return false;

        try {
            var isDiamond = Access.getPrivateField(factory, "buy") == Items.DIAMOND;
            return isDiamond;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}

