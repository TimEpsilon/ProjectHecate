package com.github.tim91690.eclipse.item;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.moonblade.MoonBlade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class CustomCraft {

    public CustomCraft() {
        MoonBlade blade = new MoonBlade();
        ItemStack sword = blade.getBlade();

        NamespacedKey key = new NamespacedKey(EventManager.getPlugin(),"moonblade");

        ShapedRecipe rc = new ShapedRecipe(key,sword);
        rc.shape("111","101","111");
        rc.setIngredient('1',new RecipeChoice.ExactChoice(MoonSoul.getSoul(1)));
        rc.setIngredient('0',Material.NETHERITE_SWORD);

        Bukkit.addRecipe(rc);

    }

}
