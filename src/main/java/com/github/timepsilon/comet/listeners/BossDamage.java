package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.item.CustomItems;
import com.github.timepsilon.comet.misc.TextManager;
import com.github.timepsilon.comet.mobs.boss.Boss;
import com.github.timepsilon.comet.mobs.boss.Demiurge;
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
        Boss boss = Boss.getBossInList(e.getEntity().getUniqueId());
        if (boss == null) return;
        if (
                e.getCause().equals(EntityDamageEvent.DamageCause.FALL)
                || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
                || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
        ) e.setCancelled(true);

        boss.resync();
        boss.getBossbar().setProgress(((LivingEntity)boss.getEntity()).getHealth() / boss.getMaxHealth());

        if (random.nextInt(10) == 0) boss.attack(boss.proxPlayer);

        if (boss instanceof Demiurge) {
            double health = ((LivingEntity) boss.getEntity()).getHealth();
            double max = boss.getMaxHealth();
            if (health > max/3) {
                if (health > max*2/3) {
                    ((Demiurge) boss).setPhase(1);
                    int line = (int) Math.round(27*(max-health)/max);
                    if (line != ((Demiurge) boss).lastLine) {
                        TextManager.demiurgeLore(line);
                        ((Demiurge) boss).lastLine = line;
                    }
                } else {
                    ((Demiurge) boss).setPhase(2);
                }
            } else {
                ((Demiurge) boss).setPhase(3);
                int line = 10+(int) Math.round(7*(max-3*health)/max);
                if (line != ((Demiurge) boss).lastLine) {
                    TextManager.demiurgeLore(line);
                    ((Demiurge) boss).lastLine = line;
                }
            }
        }
    }

    @EventHandler
    public void onDemiurgeHit(EntityDamageByEntityEvent e) {
        if (!e.getEntity().getScoreboardTags().contains("Boss")) return;
        Boss boss = Boss.getBossInList(e.getEntity().getUniqueId());
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
            p.playSound(boss.getEntity().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.HOSTILE,30,1);
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent e) {
        if (!e.getEntity().getScoreboardTags().contains("Boss")) return;
        Boss boss = Boss.getBossInList(e.getEntity().getUniqueId());
        if (boss == null) return;
        boss.death();
        e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), CustomItems.POCKET_BANK.getItem());

        int i = 1;
        for (int n =1; n <= bossXp; n += i) {
            ExperienceOrb xp = (ExperienceOrb) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation().add(Vector.getRandom().subtract(new Vector(0.5,0,0.5))), EntityType.EXPERIENCE_ORB);
            xp.setExperience(i);
            xp.setVelocity(Vector.getRandom().subtract(new Vector(0.5,0,0.5)));
            i = random.nextInt(30)+1;
        }
        //600 xp par boss

        if (ProjectHecate.getComet().getPhase() == 5 && !(ProjectHecate.getComet().finalBoss)) {
            ProjectHecate.getComet().initRitual();
            //Check if in phase 5 and if every boss is dead before starting ritual
        }

    }
}
