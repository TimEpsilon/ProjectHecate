package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class Score implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        UUID uuid = e.getEntity().getUniqueId();
        ProjectHecate.getComet().DeathCount.compute(uuid,(k, v) -> (v == null) ? 1 : v+1);
    }

    @EventHandler
    public void onTotemUse(EntityResurrectEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (e.isCancelled()) return;
        UUID uuid = e.getEntity().getUniqueId();
        ProjectHecate.getComet().DeathCount.compute(uuid,(k, v) -> (v == null) ? 1 : v+1);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        if (!e.getEntity().getScoreboardTags().contains("Comet")) return;
        if (e.getEntity().getKiller() == null) return;
        ProjectHecate.getComet().KillCount.compute(e.getEntity().getKiller().getUniqueId(),(k, v) -> (v == null) ? 1 : v+1);
    }

    @EventHandler
    public void onMobDamage(EntityDamageByEntityEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        if (!e.getEntity().getScoreboardTags().contains("Comet")) return;
        Player p = null;
        if (e.getDamager() instanceof Player) p = (Player) e.getDamager();
        if (e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player) p = (Player) ((Projectile) e.getDamager()).getShooter();
        if (p == null) return;
        ProjectHecate.getComet().DamageCount.compute(p.getUniqueId(),(k, v) -> (v == null) ? Math.round(e.getDamage()*1000f)/1000f : v+Math.round(e.getDamage()*1000f)/1000f);
    }

}
