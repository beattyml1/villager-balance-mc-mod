package net.fabricmc.villagerbalance;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class Workstations {
    public static ItemStack getBookFromLectern(BlockEntity blockEntity) {
        try {
            var lectern = (LecternBlockEntity) blockEntity;
            return lectern.getBook();
        } catch (Exception e) {
            return null;
        }
    }

    public static BlockEntity getVillagerJobSite(VillagerEntity villager) {
        var optionalPos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE);
        if (!(optionalPos.isPresent())) return null;
        var blockPos = optionalPos.get().getPos();
        var jobSite = villager.world.getBlockEntity(blockPos);
        return jobSite;
    }

    public static ItemStack removeBookFromLectern(BlockEntity blockEntity) {
        try {
            var lectern = (LecternBlockEntity) blockEntity;
            var inventory = (Inventory) Access.getPrivateField(lectern, "inventory");
            return inventory.removeStack(0);
        } catch (Exception e) {
            return null;
        }

    }
}
