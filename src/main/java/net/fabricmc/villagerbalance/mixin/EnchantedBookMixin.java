package net.fabricmc.villagerbalance.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.swing.*;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookMixin extends Item {
    public EnchantedBookMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var result = useOnLectern(context);
        return result != null ? result : ActionResult.PASS;
    }
    public ActionResult useOnLectern(ItemUsageContext context) {
        var world = context.getWorld();
        var blockPos = context.getBlockPos();
        var blockState = world.getBlockState(blockPos);
        var stack = context.getStack();
        var player = context.getPlayer();
        addNbtPagesData(stack);
        if (blockState.isOf(Blocks.LECTERN)) {
            var putOnLecternSucceeded = LecternBlock.putBookIfAbsent(player, world, blockPos, blockState, stack);
            return putOnLecternSucceeded ? ActionResult.success(world.isClient) : ActionResult.PASS;
        } else {
            return null;
        }
    }

    private static void addNbtPagesData(ItemStack stack) {
        var nbt = stack.getNbt();
        var pages = new NbtList();
        pages.add(0, NbtString.of(""));
        nbt.put("pages", pages);
    }
}
