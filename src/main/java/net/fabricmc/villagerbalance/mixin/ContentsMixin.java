package net.fabricmc.villagerbalance.mixin;

import net.fabricmc.villagerbalance.EnchantedBookContents;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(BookScreen.Contents.class)
public interface ContentsMixin {
    /**
     * @author beattyml1
     * @reason VillagerBalance
     */
    @Overwrite
    static BookScreen.Contents create(ItemStack stack) {
        if (stack.isOf(Items.ENCHANTED_BOOK)) {
            return new EnchantedBookContents(stack);
        } else if (stack.isOf(Items.WRITTEN_BOOK)) {
            return new BookScreen.WrittenBookContents(stack);
        } else if (stack.isOf(Items.WRITABLE_BOOK)){
            return new BookScreen.WritableBookContents(stack);
        } else {
            return BookScreen.EMPTY_PROVIDER;
        }
    }
}
