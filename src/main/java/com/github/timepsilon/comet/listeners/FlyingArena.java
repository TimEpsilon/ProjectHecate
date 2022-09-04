package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.misc.ConfigManager;
import com.github.timepsilon.comet.structure.AvianBoost;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class FlyingArena implements Listener {

    private final static Location loc = ConfigManager.getLoc();

    @EventHandler
    public void onRingPass(PlayerMoveEvent e) {
        //TODO : remove boosts when event stops
        if (!e.getPlayer().getWorld().equals(loc.getWorld())) return;
        Player p = e.getPlayer();
        if (p.getLocation().distance(loc) > BlockProtection.protectionDistance) return;

        for (AvianBoost boost : AvianBoost.BOOST_LIST) {
            if (!boost.isActive()) continue;
            if (!boost.isInZone(p.getLocation())) continue;

            p.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
            p.getWorld().spawnParticle(Particle.CLOUD,p.getLocation(),20,0.7,0.7,0.7,0,null,true);
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS,1,1.1f);
            break;
        }
    }

    @EventHandler
    public void onElytraClose(EntityToggleGlideEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        if (!(e.getEntity() instanceof Player p)) return;
        if (p.getLocation().distance(loc) > BlockProtection.protectionDistance) return;
        //TODO : check if Demiurge in phase 4 and 5
        if (e.isGliding()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onVoidCrossing(PlayerMoveEvent e) {
        if (!e.getPlayer().getWorld().equals(loc.getWorld())) return;
        Player p = e.getPlayer();
        if (p.getLocation().distance(loc) > BlockProtection.protectionDistance) return;

        if (loc.clone().subtract(p.getLocation()).getY() < 65) return;

        p.setVelocity(p.getVelocity().add(new Vector(0,3,0)));
        p.getWorld().spawnParticle(Particle.SWEEP_ATTACK,p.getLocation(),30,2,0.5,2,0,null,true);
        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS,1,1.2f);
    }
    
    @EventHandler
    public void onElytraDamage(EntityDamageEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) return;
        //TODO : check if Demiurge in phase 4 and 5
        e.setCancelled(true);
    }
}
