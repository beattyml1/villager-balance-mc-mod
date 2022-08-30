package net.fabricmc.villagerbalance;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;

import java.lang.reflect.Array;
import java.util.Arrays;

public class EnchantedBooks {
    public static EnchantmentLevelEntry[] getEnchantments(ItemStack book){
        if (book == null) return new EnchantmentLevelEntry[]{};
        var isEnchantedBook = book.getItem() == Items.ENCHANTED_BOOK;
        var enchantments = isEnchantedBook ? getEnchantedBookEnchants(book) : book.getEnchantments();
//        VillagerBalanceMod.LOGGER.info(enchantments != null ? enchantments.toString() : "null");
//        VillagerBalanceMod.LOGGER.info(enchantments != null ? String.valueOf(enchantments.toArray().length) : "null");
        return getEnchantmentsFromNbtList(enchantments);
    }

    public static NbtList getEnchantedBookEnchants(ItemStack book) {
        return book.getNbt() != null ? book.getNbt().getList("StoredEnchantments", 10) : new NbtList();
    }

    public static EnchantmentLevelEntry[] getEnchantmentsFromNbtList(NbtList enchantments) {
        var data = EnchantmentHelper.fromNbt(enchantments);
        return data
                .entrySet()
                .stream()
                .map((x) -> new EnchantmentLevelEntry(x.getKey(), x.getValue()))
                .toArray(EnchantmentLevelEntry[]::new);
    }
}
