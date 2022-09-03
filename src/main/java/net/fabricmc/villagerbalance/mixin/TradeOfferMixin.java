package net.fabricmc.villagerbalance.mixin;

import net.fabricmc.villagerbalance.Happiness;
import net.minecraft.entity.Entity;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TradeOffer.class)
public class TradeOfferMixin {
    @Shadow
    private int maxUses;
    @Shadow
    private int uses;

    public Entity target;

    @Overwrite
    public int getMaxUses() {
        return Happiness.scaleMaxUses(this.maxUses, this.target);
    }

    @Overwrite
    public boolean isDisabled() {
        return this.uses >= this.getMaxUses();
    }

    @Overwrite
    public void disable() {
        this.uses = this.getMaxUses();
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
