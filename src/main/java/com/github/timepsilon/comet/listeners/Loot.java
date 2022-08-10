package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.comet.item.CustomItems;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class Loot implements Listener {

    private final static float probaDrop = 0.1f;
    private final static float probaSoul = 0.3f;
    private final static int semiBossBonus = 8; //bonus = [bonus/2 , bonus] + n

    private static Random random = new Random();

    @EventHandler
    public void mobDeath(EntityDeathEvent event) {
        LivingEntity e = event.getEntity();
        if (!e.getScoreboardTags().contains("Comet")) return;
        if (e.getKiller() == null) return;

        if (random.nextFloat() < probaSoul) e.getWorld().dropItem(e.getLocation(),CustomItems.SOUL_MOB.getItem());

        // 1 mcoins par mobs de l'éclipse
        int n = 0;
        if (random.nextFloat()<probaDrop) n += 1;

        // [5-9] mcoins par semi boss
        if(e.getScoreboardTags().contains("SemiBoss")) n += random.nextInt(semiBossBonus/2) + semiBossBonus/2;

        int i = 1;
        ItemStack mcoin;
        for (int s =1; s <= n; s += i) {
            mcoin = CustomItems.MCOIN.getItem();
            mcoin.setAmount(i);
            Item item = e.getLocation().getWorld().dropItem(e.getLocation().add(Vector.getRandom().subtract(new Vector(0.5,0,0.5))), mcoin);
            item.setVelocity(Vector.getRandom().subtract(new Vector(0.5,0,0.5)).multiply(0.3));
            i = random.nextInt(3)+1;
        }
    }
}
