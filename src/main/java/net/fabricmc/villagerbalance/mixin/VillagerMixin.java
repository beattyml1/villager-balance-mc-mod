package net.fabricmc.villagerbalance.mixin;

import net.fabricmc.villagerbalance.VillagerBalanceMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerMixin extends MerchantEntity {
    public VillagerMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

//    @Override
//    public TradeOfferList getOffers() {
//        if (this.offers == null) {
//            this.offers = new TradeOfferList();
//            this.fillRecipes();
//        }
//        this.offers.forEach(x -> {
//            try {
//                x.getClass().getField("target").set(x, this);
//            } catch (IllegalAccessException | NoSuchFieldException e) {
//                VillagerBalanceMod.LOGGER.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
//            }
//        });
//        return this.offers;
//    }

    @Inject(method = "prepareOffersFor()V", at=@At("TAIL"))
    private void prepareOffers(CallbackInfo ci) {
        if (this.offers != null)
            this.offers.forEach(x -> {
                try {
                    x.getClass().getField("target").set(x, this);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    VillagerBalanceMod.LOGGER.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
                }
            });
    }
}
