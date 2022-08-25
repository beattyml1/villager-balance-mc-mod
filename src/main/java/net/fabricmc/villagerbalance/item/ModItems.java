package net.fabricmc.villagerbalance.item;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.impl.datagen.FabricTagBuilder;
import net.fabricmc.villagerbalance.VillagerBalanceMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item RAW_MYTHRIL = registerItem("illuminated_tome",
            new IlluminatedTome(new FabricItemSettings().group(ItemGroup.MISC)));



    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(VillagerBalanceMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        VillagerBalanceMod.LOGGER.info("Registering Mod Items for " + VillagerBalanceMod.MOD_ID);
    }
}
