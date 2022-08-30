package net.fabricmc.villagerbalance;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.Arrays;

public class Librarian {
    private static final int[] experienceLevels = new int[] {0, 1, 5, 10, 15, 20};
    public static void setupTrades() {
        var librarianOffers = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.LIBRARIAN);
        var builder = ImmutableMap.builder();
        for (int i = 1; i <= 5; i++){
            var levelOffers =  librarianOffers.get(i);
            final int level = i;
            var offers = Arrays.stream(levelOffers).map((x) -> modifyEnchantedBookFactories(x, level)).toArray(TradeOffers.Factory[]::new);
            builder.put(i, offers);
        }
        var newLibrarianOffers = new Int2ObjectOpenHashMap(builder.build());
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(VillagerProfession.LIBRARIAN, newLibrarianOffers);
    }
    private static TradeOffers.Factory modifyEnchantedBookFactories(TradeOffers.Factory factory, int level) {
        var isEnchantedBook = isEnchantBookFactory(factory);
        VillagerBalanceMod.LOGGER.info("isEnchantedBook: " + (isEnchantedBook ? "true" : "false") + ", " + factory.getClass().getName());
        if (isEnchantedBook) {
            var bookFactory = new BalancedEnchantedBookFactory(experienceLevels[level], level);
            return level > 1 ? bookFactory : new PredictableRandomFactoryWrapper(bookFactory);
        } else return factory;
    }

    private static boolean isEnchantBookFactory(TradeOffers.Factory factory) {
        return factory.getClass().getName().contains("EnchantBookFactory");
    }
}
