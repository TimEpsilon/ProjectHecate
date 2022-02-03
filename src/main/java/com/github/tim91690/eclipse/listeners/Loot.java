package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.item.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Loot implements Listener {

    @EventHandler
    public void mobDeath(EntityDeathEvent event) {
        LivingEntity e = event.getEntity();
        if (!e.getScoreboardTags().contains("Eclipse")) return;
        if ((int)(Math.random()*9) != 0) return;
        //10% de chance de drop

        // 1 mcoins par mobs de l'éclipse
        int n = 1;

        // [6-10] + 1 mcoins par semi boss
        if(e.getScoreboardTags().contains("SemiBoss")) n += new Random().nextInt(7) + 4;

        // [20 - 30] + 1 mcoins par boss
        if(e.getScoreboardTags().contains("Boss")) {
            n += new Random().nextInt(11) + 20;

            // 5 + [20 - 30] + 1 mcoins par phantom
            if(e.getCustomName().equals(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Phantom Overlord")) n += 5;

            // 20 + [20 - 30] + 1 mcoins par phantom
            if(e.getCustomName().equals(ChatColor.translateAlternateColorCodes('&',"&4&lScarlet Devil"))) n += 20;
        }

        ItemStack mcoin = CustomItems.MCOIN.getItem();
        mcoin.setAmount(n);
        e.getLocation().getWorld().dropItem(e.getLocation(), mcoin);
        if ((int)(Math.random()*30) == 0) e.getWorld().dropItem(e.getLocation(),CustomItems.SOUL_MOB.getItem());
    }
}
