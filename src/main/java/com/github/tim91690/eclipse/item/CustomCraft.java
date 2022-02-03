package com.github.tim91690.eclipse.item;

import com.github.tim91690.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class CustomCraft {

    public CustomCraft() {
        //Lvl 0
        shapeSquareRecipe("moonblade",CustomItems.MOONBLADE.getItem(),new ItemStack(Material.NETHERITE_SWORD),CustomItems.SOUL_MOB.getItem());

        //Lvl 1
        shapeSquareRecipe("moonblade_bless1",CustomItems.MOONBLADE_LVL1.getItem(), CustomItems.MOONBLADE.getItem(), CustomItems.SOUL_SLOTH.getItem());

        //Lvl 2
        shapeSquareRecipe("moonblade_bless2",CustomItems.MOONBLADE_LVL2.getItem(), CustomItems.MOONBLADE_LVL1.getItem(), CustomItems.SOUL_GREED.getItem());

        //Lvl 3
        shapeSquareRecipe("moonblade_bless3",CustomItems.MOONBLADE_LVL3.getItem(), CustomItems.MOONBLADE_LVL2.getItem(), CustomItems.SOUL_LUST.getItem());

        //Lvl 4
        shapeSquareRecipe("moonblade_bless4",CustomItems.MOONBLADE_LVL4.getItem(), CustomItems.MOONBLADE_LVL3.getItem(), CustomItems.SOUL_WRATH.getItem());

        //Lvl 5
        shapeSquareRecipe("moonblade_bless5",CustomItems.MOONBLADE_LVL5.getItem(), CustomItems.MOONBLADE_LVL4.getItem(), CustomItems.SOUL_GLUTTONY.getItem());

        //Lvl 6
        shapeSquareRecipe("moonblade_bless6",CustomItems.MOONBLADE_LVL6.getItem(), CustomItems.MOONBLADE_LVL5.getItem(), CustomItems.SOUL_PRIDE.getItem());

        //Lvl 7
        shapeSquareRecipe("moonblade_bless7",CustomItems.MOONBLADE_TRUE.getItem(), CustomItems.MOONBLADE_LVL6.getItem(), CustomItems.SOUL_ENVY.getItem());
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
