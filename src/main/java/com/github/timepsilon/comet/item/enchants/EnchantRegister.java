package com.github.timepsilon.comet.item.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class EnchantRegister {

    public static final Enchantment COSMIC_BLESSING = new CosmicEnchant();

    public static void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(COSMIC_BLESSING);

        if(!registered) registerEnchantment();
    }

    public static void unregister() {
        unregisterEnchantment();
    }

    private static void unregisterEnchantment() {
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            if(byKey.containsKey(EnchantRegister.COSMIC_BLESSING.getKey())) {
                byKey.remove(EnchantRegister.COSMIC_BLESSING.getKey());
            }

            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            if(byName.containsKey(EnchantRegister.COSMIC_BLESSING.getName())) {
                byName.remove(EnchantRegister.COSMIC_BLESSING.getName());
            }
        } catch (Exception ignored) { }
    }

    private static void registerEnchantment() {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null,true);
            Enchantment.registerEnchantment(EnchantRegister.COSMIC_BLESSING);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if(!registered) {
            System.out.println("[Error] : Failed to load enchants");
        }
    }
}
