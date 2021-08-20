package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.item.CustomItems;
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

    @EventHandler
    public void mobDeath(EntityDeathEvent event) {
        LivingEntity e = event.getEntity();
        if (!e.getScoreboardTags().contains("Eclipse")) return;

        // [0-2] mcoins par mobs de l'éclipse
        int n = new Random().nextInt(3);

        // [5-10] + [0-2] mcoins par semi boss
        if(e.getScoreboardTags().contains("SemiBoss")) n += new Random().nextInt(6) + 5;

        // [20 - 30] + [0-2] mcoins par boss
        if(e.getScoreboardTags().contains("Boss")) {
            n += new Random().nextInt(11) + 20;

            // 5 + [20 - 30] + [0-2] mcoins par phantom
            if(e.getCustomName().equals(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Phantom Overlord")) n += 5;

            // 20 + [20 - 30] + [0-2] mcoins par phantom
            if(e.getCustomName().equals(ChatColor.translateAlternateColorCodes('&',"&4&lScarlet Devil"))) n += 20;
        }



        if (n==0) return;

        e.getLocation().getWorld().dropItem(e.getLocation(), CustomItems.mcoin(n));
        return;
    }
}
