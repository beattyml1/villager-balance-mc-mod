package net.fabricmc.villagerbalance.mixin;

import net.fabricmc.villagerbalance.VillagerBalanceMod;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class VillagerBalanceMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		VillagerBalanceMod.LOGGER.info("This line is printed by an villagerbalance mod mixin!");
	}
}
