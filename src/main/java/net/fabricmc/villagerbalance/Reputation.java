package net.fabricmc.villagerbalance;

import net.minecraft.village.VillageGossipType;

public class Reputation {
    public static int getMaxReputation(VillageGossipType type) {
        return switch (type) {
            case MAJOR_POSITIVE -> 20;
            case MINOR_POSITIVE -> 75;
            default -> type.maxValue;
        };
    }
}
