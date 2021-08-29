package com.github.tim91690.commands;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.CustomItems;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveMoonBlade implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.getInventory().addItem(CustomItems.getMoonBlade());
            p.getInventory().addItem(CustomItems.getLustSoul());
            p.getInventory().addItem(CustomItems.getSlothSoul());
            p.getInventory().addItem(CustomItems.getPrideSoul());
            p.getInventory().addItem(CustomItems.getWrathSoul());
            p.getInventory().addItem(CustomItems.getGreedSoul());
            p.getInventory().addItem(CustomItems.getGluttonySoul());
            p.getInventory().addItem(CustomItems.getEnvySoul());
            p.getInventory().addItem(CustomItems.getMobSoul());
            p.sendMessage(p.getPing()+"");
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless1"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless1"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless2"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless2"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless3"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless3"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless4"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless4"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless5"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless5"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless6"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless6"));
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless7"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless7"));
            p.sendMessage(p.getWorld().toString());
            return true;
        }
        return false;
    }
}
