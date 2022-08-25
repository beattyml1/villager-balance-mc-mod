package net.fabricmc.villagerbalance;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.villagerbalance.item.ModItems;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VillagerBalanceMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "villagerbalance";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ModItems.registerModItems();
		setupTrades();
	}

	public void setupTrades() {
		var librarianOffers = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.LIBRARIAN);
		var builder = ImmutableMap.builder();
		for (int i = 1; i <= 5; i++){
			var levelOffers =  librarianOffers.get(i);
			if (i == 1) {
				var modifiedOffers = new TradeOffers.Factory[] { levelOffers[0], levelOffers[2]};
				builder.put(i, modifiedOffers);
			} else {
				final int level = i;
				var offers = Arrays.stream(levelOffers).map((x) -> modifyEnchantedBookFactories(x, level)).toArray(TradeOffers.Factory[]::new);
				builder.put(i, offers);
			}
		}
		var newLibrarianOffers = new Int2ObjectOpenHashMap(builder.build());
		TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(VillagerProfession.LIBRARIAN, newLibrarianOffers);
	}
	private static TradeOffers.Factory modifyEnchantedBookFactories(TradeOffers.Factory factory, int level) {
		var experienceLevels = new int[] {0, 1, 5, 10, 15, 20};

		var isEnchantedBook = factory.getClass().getName().contains("EnchantBookFactory");
		LOGGER.info("isEnchantedBook: " + (isEnchantedBook ? "true" : "false") + ", " + factory.getClass().getName());
		if (isEnchantedBook) {
			return new EnchantedBookFromLecternOrRandom(experienceLevels[level], factory);
		} else return factory;
	}


}

