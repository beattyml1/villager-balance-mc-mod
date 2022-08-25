package net.fabricmc.villagerbalance;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;

public class EnchantedBooks {
    public static EnchantmentLevelEntry[] getEnchantments(ItemStack book){
        if (book == null) return new EnchantmentLevelEntry[]{};
        var enchantments = book.getEnchantments();
        return getEnchantmentsFromNbtList(enchantments);
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
