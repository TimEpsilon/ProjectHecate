package com.github.tim91690.eclipse.item;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.enchants.EnchantRegister;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomCraft {

    public CustomCraft() {
        //Lvl 0
        ItemStack blade = CustomItems.getMoonBlade();
        shapeSquareRecipe("moonblade",blade,new ItemStack(Material.NETHERITE_SWORD),CustomItems.getMobSoul());

        //Lvl 1
        ItemStack nextblade = CustomItems.getMoonBlade();
        ItemMeta meta = nextblade.getItemMeta();
        List<Component> lore = meta.lore();
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.GRAY+"Moon's Blessing I"));
        meta.lore(lore);
        meta.setCustomModelData(2);
        nextblade.setItemMeta(meta);
        nextblade.addUnsafeEnchantment(EnchantRegister.MOON_BLESSING,1);
        shapeSquareRecipe("moonblade_bless1",nextblade,blade,CustomItems.getSlothSoul());

        //Lvl 2
        blade=nextblade.clone();
        lore.set(5,Component.text(ChatColor.GRAY+"Moon's Blessing II"));
        meta.lore(lore);
        meta.setCustomModelData(3);
        nextblade.setItemMeta(meta);
        nextblade.addUnsafeEnchantment(EnchantRegister.MOON_BLESSING,2);
        shapeSquareRecipe("moonblade_bless2",nextblade,blade,CustomItems.getGreedSoul());

        //Lvl 3
        blade=nextblade.clone();
        lore.set(5,Component.text(ChatColor.GRAY+"Moon's Blessing III"));
        meta.lore(lore);
        meta.setCustomModelData(4);
        nextblade.setItemMeta(meta);
        nextblade.addUnsafeEnchantment(EnchantRegister.MOON_BLESSING,3);
        shapeSquareRecipe("moonblade_bless3",nextblade,blade,CustomItems.getLustSoul());

        //Lvl 4
        blade=nextblade.clone();
        lore.set(5,Component.text(ChatColor.GRAY+"Moon's Blessing IV"));
        meta.lore(lore);
        meta.setCustomModelData(5);
        nextblade.setItemMeta(meta);
        nextblade.addUnsafeEnchantment(EnchantRegister.MOON_BLESSING,4);
        shapeSquareRecipe("moonblade_bless4",nextblade,blade,CustomItems.getWrathSoul());

        //Lvl 5
        blade=nextblade.clone();
        lore.set(5,Component.text(ChatColor.GRAY+"Moon's Blessing V"));
        meta.lore(lore);
        meta.setCustomModelData(6);
        nextblade.setItemMeta(meta);
        nextblade.addUnsafeEnchantment(EnchantRegister.MOON_BLESSING,5);
        shapeSquareRecipe("moonblade_bless5",nextblade,blade,CustomItems.getGluttonySoul());

        //Lvl 6
        blade=nextblade.clone();
        lore.set(5,Component.text(ChatColor.GRAY+"Moon's Blessing VI"));
        meta.lore(lore);
        meta.setCustomModelData(7);
        nextblade.setItemMeta(meta);
        nextblade.addUnsafeEnchantment(EnchantRegister.MOON_BLESSING,6);
        shapeSquareRecipe("moonblade_bless6",nextblade,blade,CustomItems.getPrideSoul());

        //Lvl 7
        blade=nextblade.clone();
        shapeSquareRecipe("moonblade_bless7",CustomItems.getUltimateMoonBlade(),blade,CustomItems.getEnvySoul());

    }

    private void shapeSquareRecipe(String name, ItemStack result, ItemStack middle, ItemStack soul) {
        NamespacedKey key = new NamespacedKey(EventManager.getPlugin(),name);
        ShapedRecipe rc = new ShapedRecipe(key,result);
        rc.shape("020","010","000");
        rc.setIngredient('0',new RecipeChoice.ExactChoice(CustomItems.getMobSoul()));
        rc.setIngredient('1',new RecipeChoice.ExactChoice(middle));
        rc.setIngredient('2',new RecipeChoice.ExactChoice(soul));
        Bukkit.addRecipe(rc);
    }
}
