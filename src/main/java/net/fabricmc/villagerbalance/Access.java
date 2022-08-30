package net.fabricmc.villagerbalance;

import net.minecraft.block.entity.LecternBlockEntity;

import java.lang.reflect.Field;

public class Access {
    public static Object getPrivateField(Object entity, String name) throws NoSuchFieldException, IllegalAccessException {
        // Create Field object
        Field privateField = entity.getClass().getDeclaredField(name);

        // Set the accessibility as true
        privateField.setAccessible(true);

        // Store the value of private field in variable

        return privateField.get(entity);
    }
}
