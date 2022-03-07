package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.mobs.boss.Boss;
import com.github.tim91690.eclipse.mobs.boss.Demiurge;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        if (
                e.getCause().equals(EntityDamageEvent.DamageCause.FALL)
                || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
                || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
        ) e.setCancelled(true);

        boss.getBossbar().setProgress(((LivingEntity)boss.getEntity()).getHealth() / boss.getMaxHealth());

        if (random.nextInt(10) == 0) boss.attack(boss.getBossbar().getPlayers());

        if (boss instanceof Demiurge) {
            double health = ((LivingEntity) boss.getEntity()).getHealth();
            if (health > boss.getMaxHealth()/3) {
                if (health > boss.getMaxHealth()*2/3) {
                    ((Demiurge) boss).setPhase(1);
                } else {
                    ((Demiurge) boss).setPhase(2);
                }
            } else {
                ((Demiurge) boss).setPhase(3);
            }
        }
    }

    @EventHandler
    public void onDemiurgeHit(EntityDamageByEntityEvent e) {
        if (!e.getEntity().getScoreboardTags().contains("Boss")) return;
        Boss boss = Boss.getBossInList(e.getEntity());
        if (boss == null) return;
        if (!(boss instanceof Demiurge)) return;
        Player p = null;
        if(e.getDamager() instanceof Player) p = (Player)e.getDamager();
        if(e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player) p = (Player)((Projectile)e.getDamager()).getShooter();
        if(p == null) return;

        if (((Demiurge)boss).getPhase() == 2) {
            e.setCancelled(true);
            p.playSound(boss.getEntity().getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, SoundCategory.HOSTILE,30,2);
        } else {
            p.playSound(boss.getEntity().getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.HOSTILE,30,2);
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
