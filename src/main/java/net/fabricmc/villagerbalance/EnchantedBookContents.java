package net.fabricmc.villagerbalance;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Environment(EnvType.CLIENT)
public class EnchantedBookContents implements BookScreen.Contents {
    public EnchantedBookContents(ItemStack stack) {
    }

    private static List<String> getPages(ItemStack stack) {
        return List.of("");
    }

    public int getPageCount() {
        return 1;
    }

    public StringVisitable getPageUnchecked(int index) {
        return StringVisitable.plain("");
    }
}
