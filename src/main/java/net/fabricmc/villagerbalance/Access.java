package net.fabricmc.villagerbalance;

import net.minecraft.block.entity.LecternBlockEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Access {
    public static Object getPrivateField(Object entity, String name) throws NoSuchFieldException, IllegalAccessException {
        // Create Field object
        Field privateField = entity.getClass().getDeclaredField(name);

        // Set the accessibility as true
        privateField.setAccessible(true);

        // Store the value of private field in variable

        return privateField.get(entity);
    }
    static void setFinalStatic(Object entity, String name, Object newValue) throws Exception {
        var field = entity.getClass().getDeclaredField(name);
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(entity, newValue);
    }
}
