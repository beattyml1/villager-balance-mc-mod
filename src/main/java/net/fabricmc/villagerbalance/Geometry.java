package net.fabricmc.villagerbalance;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Geometry {
    @NotNull
    public static BlockPos[] getColumn(BlockPos base, int down, int up, boolean includeSelf) {
        var column = new ArrayList<BlockPos>();
        for (int d = down; d > 0; d--) {
            column.add(base.down(d));
        }
        if (includeSelf) column.add(base);

        for (int u = 1; u <= up; u++) {
            column.add(base.up(u));
        }
        return column.toArray(BlockPos[]::new);
    }

    public static BlockPos[] getBlocksInSquareAround(BlockPos pos, int distance, boolean includeSelf) {
        var eastWestLine = getEastWestLine(pos, distance);
        return eastWestLine
                .stream()
                .flatMap(x -> getNorthSouthLine(pos, distance).stream())
                .filter(x -> includeSelf || x != pos)
                .toArray(BlockPos[]::new);
    }

    public static ArrayList<BlockPos> getNorthSouthLine(BlockPos pos, int distance) {
        var northSouthLine = new ArrayList<BlockPos> ();
        northSouthLine.add(pos);
        for (int z = 1; z <= distance; z++) {
            northSouthLine.add(pos.north(z));
            northSouthLine.add(pos.south(z));
        }
        return northSouthLine;
    }

    @NotNull
    public static ArrayList<BlockPos> getEastWestLine(BlockPos pos, int distance) {
        var eastWestLine = new ArrayList<BlockPos> ();
        eastWestLine.add(pos);
        for (int x = 1; x <= distance; x++) {
            eastWestLine.add(pos.east(x));
            eastWestLine.add(pos.west(x));
        }
        return eastWestLine;
    }

    public static BlockPos[] getAdjacentBlocks(BlockPos currentBlock){
        return new BlockPos[]{
            currentBlock.east(),
            currentBlock.west(),
            currentBlock.north(),
            currentBlock.south(),
        };
    }
}
