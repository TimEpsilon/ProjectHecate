package com.github.tim91690.eclipse.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Loot implements Listener {
    @EventHandler
    public void mobDeath(EntityDeathEvent event) {
        LivingEntity e = event.getEntity();

        if (!e.getScoreboardTags().contains("Eclipse")) return;

        ItemStack mcoin = new ItemStack(Material.EMERALD,(new Random()).nextInt(2));
        ItemMeta Mmeta = mcoin.getItemMeta();
        Mmeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin");
        Mmeta.setCustomModelData(42);
        mcoin.setItemMeta(Mmeta);

        if (e instanceof Zombie) {
            e.getLocation().getWorld().dropItem(e.getLocation(),mcoin);
        }
    }
}
