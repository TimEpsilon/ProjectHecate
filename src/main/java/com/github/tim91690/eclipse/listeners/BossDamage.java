package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.mobs.boss.Boss;
import com.github.tim91690.eclipse.mobs.boss.Demiurge;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class BossDamage implements Listener {

    @EventHandler
    public void onBossDamage(EntityDamageEvent e) {
        if (!e.getEntity().getScoreboardTags().contains("Boss")) return;
        Boss boss = Boss.getBossInList(e.getEntity());
        boss.getBossbar().setProgress(((LivingEntity)boss.getEntity()).getHealth() / boss.getMaxHealth());

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
        //300 xp par boss
        e.setDroppedExp(300);
    }
}
