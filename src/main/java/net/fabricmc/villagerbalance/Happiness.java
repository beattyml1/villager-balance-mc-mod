package net.fabricmc.villagerbalance;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Happiness {
    public static float getHappinessMultiplier(VillagerEntity villager) {
        var isFree = isFreeRange(villager) ? 1 : 0;
        var hasSlept = passesSleepCheck(villager) ? 1 : 0;
        var hasAdequateHousing = housingIsAdequate(villager) ? 1 : 0;
        var socialLife = hasSocialLife(villager) ? 1 : 0;

        var score = isFree + hasAdequateHousing + hasSlept + socialLife;
        return (float) (((score - 2) * .25) + 1);
    }

    public static int scaleMaxUses(int baseMaxUses, Entity target) {
        VillagerBalanceMod.LOGGER.info(String.format("target: %s, baseMaxUses: %s", target, baseMaxUses));
        if (target != null && target.getClass() == VillagerEntity.class)
            return baseMaxUses * Math.round(getHappinessMultiplier((VillagerEntity) target));
        else return baseMaxUses;
    }

    private static boolean passesSleepCheck(VillagerEntity villager) {
        return villager.getSleepingPosition().isPresent();
    }

    private static boolean hasSocialLife(VillagerEntity villager) {
        try {
            var lastGossipTime = (long) Access.getPrivateField(villager, "gossipStartTime");
            return lastGossipTime + 24000L >= villager.getWorld().getTime();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            VillagerBalanceMod.LOGGER.error(String.format("%s: %s", e, e.getMessage()));
        }
        return true;
    }

    private boolean workstationIsOptimized(VillagerEntity villager) {
        return WorkstationOptimization.isOptimized(villager);
    }

    private static boolean isFreeRange(VillagerEntity villager) {
        var currentBlock = villager.getBlockPos();
        var workstationBlock = Workstations.getVillagerJobSite(villager);
        var world = villager.getWorld();
        return positionUnconstrained(currentBlock, world) && positionUnconstrained(workstationBlock.getPos(), world);
    }

    private static boolean positionUnconstrained(BlockPos currentBlock, World world) {
        var adjacentColumns = columnsAboveBlocks(Geometry.getAdjacentBlocks(currentBlock));
        var columns5by5 = columnsAboveBlocks(Geometry.getBlocksInSquareAround(currentBlock, 2, false));
        var navigableAdjacent = Arrays.stream(adjacentColumns).filter(x -> columnIsNavigable(world, x)).count();
        var navigable5by5 = Arrays.stream(columns5by5).filter(x -> columnIsNavigable(world, x)).count();
        return navigableAdjacent >= 1 && navigable5by5 >= 6;
    }

    public static boolean housingIsAdequate(VillagerEntity villager) {
        var bed = villager.getSleepingPosition();
        if (bed.isEmpty()) return false;
        return bedAreaIsAdequate(villager.getWorld(), bed.get());
    }

    public static boolean bedAreaIsAdequate(World world, BlockPos bedBlock) {
        var adjacentBlocks = Geometry.getBlocksInSquareAround(bedBlock, 1, false);
        var adequateBlocks = Arrays.stream(adjacentBlocks).filter(block -> isCoveredAndNavigable(world, block));
        return adequateBlocks.count() > 3;
    }

    private static boolean isCoveredAndNavigable(World world, BlockPos block) {
        var column = Geometry.getColumn(block, 0, 2, true);
        var isNavigable = columnIsNavigable(world, column);
        var isCovered = !world.isSkyVisible(block);
        return isNavigable && !isCovered;
    }


    private static boolean columnIsNavigable(World world, BlockPos[] column) {
        var a = world.getBlockState(column[0]).isAir();
        var b = world.getBlockState(column[1]).isAir();
        var c = world.getBlockState(column[2]).isAir();
        return a && b || b && c;
    }

    private static BlockPos[][] columnsAboveBlocks(BlockPos[] columnBases) {
        return Arrays
            .stream(columnBases)
            .map(base -> Geometry.getColumn(base, 0, 2, true))
            .toArray(BlockPos[][]::new);
    }
}


// private static long countAir(BlockPos[] blocks, World world) {
//        return Arrays.stream(blocks)
//                .map(world::getBlockState)
//                .filter(AbstractBlock.AbstractBlockState::isAir)
//                .count();
//    }
//
//    private static long countPassable(BlockPos[] blocks, World world) {
//        return Arrays.stream(blocks)
//                .map(x -> new Pair<>(world.getBlockState(x), x))
//                .filter(x -> x.getLeft().isSolidBlock(world, x.getRight()))
//                .count();
//    }
//
//    @NotNull
//    private static BlockPos[] getSurroundingBlocks(BlockPos currentBlock) {
//        return new BlockPos[]{
//                currentBlock.east(),
//                currentBlock.west(),
//                currentBlock.north(),
//                currentBlock.south(),
//                currentBlock.up().east(),
//                currentBlock.up().west(),
//                currentBlock.up().north(),
//                currentBlock.up().south(),
//        };
//    }
//
//    @NotNull
//    private static BlockPos[] getDiagonalBlocks(BlockPos currentBlock) {
//        return new BlockPos[]{
//                currentBlock.east().north(),
//                currentBlock.east().south(),
//                currentBlock.west().north(),
//                currentBlock.west().south(),
//                currentBlock.up().east().north(),
//                currentBlock.up().east().south(),
//                currentBlock.up().west().north(),
//                currentBlock.up().west().south(),
//        };
//    }
// var surroundingBlocks = getSurroundingBlocks(currentBlock);
//    var diagonalBlocks = getDiagonalBlocks(currentBlock);
//    var directAir = countAir(surroundingBlocks, world);
//    var diagonalAir = countAir(diagonalBlocks, world);
//    var directPassable = countPassable(surroundingBlocks, world);
//    var diagonalPassable = countPassable(diagonalBlocks, world);
//        return (directAir * 4 + directPassable * 2 + diagonalAir * 2 + diagonalPassable) / 16.0f;
