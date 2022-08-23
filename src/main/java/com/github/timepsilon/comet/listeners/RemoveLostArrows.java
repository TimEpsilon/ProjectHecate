package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Random;

public class RemoveLostArrows implements Listener {

    private static final Random random = new Random();

    @EventHandler
    public void onArrowLand(ProjectileHitEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        if (e.getHitBlock() == null) return;
        Projectile projectile = e.getEntity();
        if (projectile.getShooter() instanceof Player) return;

        projectile.remove();

        if (projectile.getShooter() instanceof Skeleton skeleton) {
            if (random.nextFloat() >0.3) return;
            skeleton.getWorld().spawnParticle(Particle.PORTAL,skeleton.getLocation(),10,0.5,1,0.5);
            skeleton.teleport(projectile.getLocation());
            skeleton.getWorld().playSound(skeleton.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE,1,1.5f);
        }
    }
}
