package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.mobs.boss.Boss;
import com.github.tim91690.eclipse.mobs.boss.Demiurge;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class BossDamage implements Listener {

    private final int bossXp = 600;
    private static final Random random = new Random();

    @EventHandler
    public void onBossDamage(EntityDamageEvent e) {
        if (!e.getEntity().getScoreboardTags().contains("Boss")) return;
        Boss boss = Boss.getBossInList(e.getEntity());
        if (boss == null) return;
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) e.setCancelled(true);

        boss.getBossbar().setProgress(((LivingEntity)boss.getEntity()).getHealth() / boss.getMaxHealth());

        if (random.nextInt(10) == 0) boss.attack(boss.getBossbar().getPlayers());

        if (boss instanceof Demiurge) {
            double health = ((LivingEntity) boss.getEntity()).getHealth();
            if (health > boss.getMaxHealth()/2) {
                if (health > boss.getMaxHealth()*3/4) {
                    ((Demiurge) boss).setPhase(1);
                } else {
                    ((Demiurge) boss).setPhase(2);
                }
            } else {
                if (health > boss.getMaxHealth()/4) {
                    ((Demiurge) boss).setPhase(3);
                } else {
                    ((Demiurge) boss).setPhase(4);
                }
            }
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent e) {
        if (!e.getEntity().getScoreboardTags().contains("Boss")) return;
        Boss boss = Boss.getBossInList(e.getEntity());
        boss.death();

        int i = 1;
        for (int n =1; n <= bossXp; n += i) {
            ExperienceOrb xp = (ExperienceOrb) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation().add(Vector.getRandom().subtract(new Vector(0.5,0,0.5))), EntityType.EXPERIENCE_ORB);
            xp.setExperience(i);
            xp.setVelocity(Vector.getRandom().subtract(new Vector(0.5,0,0.5)));
            i = random.nextInt(30)+1;
        }
        //600 xp par boss
    }
}
