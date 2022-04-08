package com.github.timepsilon.commands;

import com.github.timepsilon.EventManager;
import com.github.timepsilon.comet.item.CustomItems;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class GiveCosmicBlade {

    public static void getItems(Player p) {
        for (CustomItems ci : CustomItems.values()) {
            p.getInventory().addItem(ci.getItem());
        }
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless1"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless1"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless2"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless2"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless3"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless3"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless4"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless4"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless5"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless5"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless6"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless6"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless7"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade_bless7"));
    }
}
