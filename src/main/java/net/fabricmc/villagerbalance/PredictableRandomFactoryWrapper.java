package net.fabricmc.villagerbalance;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.jetbrains.annotations.Nullable;

public class PredictableRandomFactoryWrapper  implements TradeOffers.Factory {
    private final TradeOffers.Factory base;

    public PredictableRandomFactoryWrapper(TradeOffers.Factory base) {
        this.base = base;
    }
    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        random = new CheckedRandom(entity.getUuid().toString().hashCode());
        return this.base.create(entity, random);
    }
}
