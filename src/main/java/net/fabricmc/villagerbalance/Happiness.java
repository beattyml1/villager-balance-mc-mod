package net.fabricmc.villagerbalance;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;

public class Happiness {
    public static float getHappinessMultiplier(VillagerEntity villager) {
        var isFree = isFreeRange(villager);
        var hasSlept = passesSleepCheck(villager);
        return isFree && hasSlept ? 1.5f : hasSlept || isFree ? 1 : 0.75f;
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

    private boolean hasSocialLife(VillagerEntity villager) {
        try {
            var lastGossipTime = (long) Access.getPrivateField(villager, "gossipStartTime");
            return lastGossipTime + 24000L >= villager.getWorld().getTime();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            VillagerBalanceMod.LOGGER.error(String.format("%s: %s", e, e.getMessage()));
        }
        return true;
    }

    private static boolean isFreeRange(VillagerEntity villager) {
        var currentBlock = villager.getBlockPos();
        var workstationBlock = Workstations.getVillagerJobSite(villager);
        var world = villager.getWorld();
        return positionUnconstrained(currentBlock, world) && positionUnconstrained(workstationBlock.getPos(), world);
    }

    private static boolean positionUnconstrained(BlockPos currentBlock, World world) {
        var adjacentColumns = columnsAboveBlocks(getAdjacentBlocks(currentBlock));
        var columns5by5 = columnsAboveBlocks(get5by5Around(currentBlock));
        var navigableAdjacent = Arrays.stream(adjacentColumns).filter(x -> columnIsNavigable(world, x)).count();
        var navigable5by5 = Arrays.stream(columns5by5).filter(x -> columnIsNavigable(world, x)).count();
        return navigableAdjacent >= 1 && (navigable5by5 >= 10 || navigableAdjacent >= 2 && navigable5by5 >= 8);
    }


    private static BlockPos[] getAdjacentBlocks(BlockPos currentBlock){
        return new BlockPos[]{
            currentBlock.east(),
            currentBlock.west(),
            currentBlock.north(),
            currentBlock.south(),
        };
    }

    private static boolean columnIsNavigable(World world, BlockPos[] column) {
        var a = world.getBlockState(column[0]).isAir();
        var b = world.getBlockState(column[0]).isAir();
        var c = world.getBlockState(column[0]).isAir();
        return a && b || b && c;
    }

    private static BlockPos[][] columnsAboveBlocks(BlockPos[] columnBases) {
        return Arrays
            .stream(columnBases)
            .map(base -> new BlockPos[] {
                    base,
                    base.up(),
                    base.up(2)
            })
            .toArray(BlockPos[][]::new);
    }

    private static BlockPos[] get5by5Around(BlockPos currentBlock) {
        var eastWestLine = new BlockPos[]{
                currentBlock.east(),
                currentBlock.west(),
                currentBlock.east(2),
                currentBlock.west(2),
        };
        return Arrays.stream(eastWestLine).flatMap(x -> Arrays.stream(new BlockPos[]{
            x,
            x.south(),
            x.south(2),
            x.north(),
            x.north(1)
        })).toArray(BlockPos[]::new);
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
