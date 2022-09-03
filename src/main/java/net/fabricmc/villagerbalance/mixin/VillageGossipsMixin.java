package net.fabricmc.villagerbalance.mixin;

import net.fabricmc.villagerbalance.Reputation;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(VillagerGossips.class)
public class VillageGossipsMixin {
    @Overwrite
    private int mergeReputation(VillageGossipType type, int left, int right) {
        int merged = left + right;
        var max = Reputation.getMaxReputation(type);
        return merged > max ? Math.max(max, left) : merged;
    }
}
