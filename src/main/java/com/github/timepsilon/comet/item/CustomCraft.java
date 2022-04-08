package com.github.timepsilon.comet.item;

import com.github.timepsilon.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class CustomCraft {

    public CustomCraft() {
        //Lvl 0
        shapeSquareRecipe("cosmicblade",CustomItems.COSMICBLADE.getItem(),new ItemStack(Material.NETHERITE_SWORD),CustomItems.SOUL_MOB.getItem());

        //Lvl 1
        shapeSquareRecipe("cosmicblade_bless1",CustomItems.COSMICBLADE_LVL1.getItem(), CustomItems.COSMICBLADE.getItem(), CustomItems.SOUL_SLOTH.getItem());

        //Lvl 2
        shapeSquareRecipe("cosmicblade_bless2",CustomItems.COSMICBLADE_LVL2.getItem(), CustomItems.COSMICBLADE_LVL1.getItem(), CustomItems.SOUL_GREED.getItem());

        //Lvl 3
        shapeSquareRecipe("cosmicblade_bless3",CustomItems.COSMICBLADE_LVL3.getItem(), CustomItems.COSMICBLADE_LVL2.getItem(), CustomItems.SOUL_LUST.getItem());

        //Lvl 4
        shapeSquareRecipe("cosmicblade_bless4",CustomItems.COSMICBLADE_LVL4.getItem(), CustomItems.COSMICBLADE_LVL3.getItem(), CustomItems.SOUL_WRATH.getItem());

        //Lvl 5
        shapeSquareRecipe("cosmicblade_bless5",CustomItems.COSMICBLADE_LVL5.getItem(), CustomItems.COSMICBLADE_LVL4.getItem(), CustomItems.SOUL_GLUTTONY.getItem());

        //Lvl 6
        shapeSquareRecipe("cosmicblade_bless6",CustomItems.COSMICBLADE_LVL6.getItem(), CustomItems.COSMICBLADE_LVL5.getItem(), CustomItems.SOUL_PRIDE.getItem());

        //Lvl 7
        shapeSquareRecipe("cosmicblade_bless7",CustomItems.COSMICBLADE_TRUE.getItem(), CustomItems.COSMICBLADE_LVL6.getItem(), CustomItems.SOUL_ENVY.getItem());
    }

    private void shapeSquareRecipe(String name, ItemStack result, ItemStack middle, ItemStack soul) {
        NamespacedKey key = new NamespacedKey(EventManager.getPlugin(),name);
        ShapedRecipe rc = new ShapedRecipe(key,result);
        rc.shape("020","010","000");
        rc.setIngredient('0',new RecipeChoice.ExactChoice(CustomItems.SOUL_MOB.getItem()));
        rc.setIngredient('1',new RecipeChoice.ExactChoice(middle));
        rc.setIngredient('2',new RecipeChoice.ExactChoice(soul));
        Bukkit.addRecipe(rc);
    }
}
