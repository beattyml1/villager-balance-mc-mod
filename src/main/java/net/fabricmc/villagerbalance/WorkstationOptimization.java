package net.fabricmc.villagerbalance;

import net.minecraft.block.*;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WorkstationOptimization {
    public static boolean isOptimized(VillagerEntity villager) {
        return WorkstationOptimization.isOptimized(Workstations.getJobSiteLocation(villager), villager.getWorld());
    }

    public static boolean isOptimized(BlockPos pos, World world) {
        var blockState = world.getBlockState(pos);
        var block = blockState.getBlock();

        if (block instanceof BlastFurnaceBlock || block instanceof SmithingTableBlock || block instanceof GrindstoneBlock) {
            return countMatchingSurroundingBlocks(pos, world, WorkstationOptimization::smithOptimized) > 2;
        } else if (block instanceof ComposterBlock) {
            return countMatchingSurroundingBlocks(pos, world, WorkstationOptimization::farmerOptimized) > 5;
        } else if (block instanceof LecternBlock) {
            return countMatchingSurroundingBlocks(pos, world, WorkstationOptimization::librarianOptimize) > 3;
        } else if (block instanceof CartographyTableBlock) {
            return cartographerOptimized(pos, world);
        }
        return true;
    }

    private static long countMatchingSurroundingBlocks(BlockPos pos, World world, Predicate<BlockState> predicate) {
        var surroundingBlocks = world.getStatesInBox(surroundingBox(pos));
        return surroundingBlocks.filter(predicate).count();
    }

    @NotNull
    private static Box surroundingBox(BlockPos pos) {
        return new Box(
                pos.add(5, 3, 5),
                pos.add(-5, 3, -5)
        );
    }

    public static boolean smithOptimized(BlockState blockState) {
        return Arrays.stream(smithBlocks).anyMatch(x -> x == blockState.getBlock());
    }
    public static boolean farmerOptimized(BlockState blockState) {
        return blockState.getRegistryEntry().isIn(BlockTags.CROPS);
    }
    public static boolean librarianOptimize(BlockState blockState) {
        return Arrays.stream(librarianBlocks).anyMatch(x -> x == blockState.getBlock());
    }
    public static boolean clericOptimized(BlockState blockState) {
        return Arrays.stream(clericBlocks).anyMatch(x -> x == blockState.getBlock());
    }
    public static boolean cartographerBlockOptimized(BlockState blockState) {
        return Arrays.stream(cartographerBlocks).anyMatch(x -> x == blockState.getBlock());
    }
    public static boolean cartographerOptimized(BlockPos pos, World world) {
        var box = surroundingBox(pos);
        var blocks = countMatchingSurroundingBlocks(pos, world, WorkstationOptimization::cartographerBlockOptimized);
        var maps = getMaps(box, world).stream().count();
        return maps > 1 && maps + blocks > 2;
    }

    public static final Block[] smithBlocks = {
        Blocks.LAVA,
        Blocks.LAVA_CAULDRON,
        Blocks.FURNACE,
        Blocks.ANVIL,
        Blocks.FIRE,
        Blocks.SOUL_FIRE,
        Blocks.CAMPFIRE,
        Blocks.SOUL_CAMPFIRE,
        Blocks.ENCHANTING_TABLE
    };

    public static final Block[] librarianBlocks = {
        Blocks.BOOKSHELF,
        Blocks.ENCHANTING_TABLE
    };

    public static final Block[] clericBlocks = {
        Blocks.WATER_CAULDRON,
        Blocks.NETHER_WART,
        Blocks.BOOKSHELF,
    };

    public static final Block[] cartographerBlocks = {
        Blocks.BOOKSHELF,
    };

    public static List<ItemFrameEntity> getMaps(Box box, World world) {
        return world.getEntitiesByClass(ItemFrameEntity.class, box, ItemFrameEntity::containsMap);
    }


}
