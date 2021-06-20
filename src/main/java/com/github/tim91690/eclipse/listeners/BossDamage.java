package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.mobs.boss.Boss;
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
