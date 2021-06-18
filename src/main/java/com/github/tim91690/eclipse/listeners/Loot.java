package com.github.tim91690.eclipse.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Loot implements Listener {

    private ItemStack mcoin(int n) {
        ItemStack mcoin = new ItemStack(Material.EMERALD,n);
        ItemMeta Mmeta = mcoin.getItemMeta();
        Mmeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin");
        Mmeta.setCustomModelData(42);
        mcoin.setItemMeta(Mmeta);
        return mcoin;
    }


    @EventHandler
    public void mobDeath(EntityDeathEvent event) {
        LivingEntity e = event.getEntity();

        if (!e.getScoreboardTags().contains("Eclipse")) return;
        int n = new Random().nextInt(3);

        if(e.getScoreboardTags().contains("SemiBoss")) n += new Random().nextInt(15) + 5;

        if (n==0) return;

        e.getLocation().getWorld().dropItem(e.getLocation(),mcoin(n));
        return;
    }
}
