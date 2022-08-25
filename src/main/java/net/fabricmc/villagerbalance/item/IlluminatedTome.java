package net.fabricmc.villagerbalance.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WritableBookItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class IlluminatedTome extends WritableBookItem {
    public IlluminatedTome(Settings settings) {
        super(settings);
    }

    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    public int getEnchantability() {
        return 1;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
