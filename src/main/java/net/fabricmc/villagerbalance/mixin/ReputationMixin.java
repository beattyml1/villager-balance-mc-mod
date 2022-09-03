package net.fabricmc.villagerbalance.mixin;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.villagerbalance.Reputation;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.UUID;

@Mixin(targets = "net.minecraft.village.VillagerGossips.Reputation")
public abstract class ReputationMixin {
    @Shadow
    Object2IntMap<VillageGossipType> associatedGossip = new Object2IntOpenHashMap();

    @Overwrite
    public void clamp(VillageGossipType gossipType) throws NoSuchFieldException, IllegalAccessException {
        var gossipLevel = this.associatedGossip.getInt(gossipType);
        var maxRep = Reputation.getMaxReputation(gossipType);
        if (gossipLevel > maxRep) {
            this.associatedGossip.put(gossipType,maxRep);
        }

        if (gossipLevel < 2) {
            this.remove(gossipType);
        }
    }

    public abstract void remove(VillageGossipType gossipType);
}
