package com.github.tim91690.eclipse.structure;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.events.EnergyPylon;
import com.github.tim91690.eclipse.misc.ConfigManager;
import com.github.tim91690.eclipse.mobs.boss.Boss;
import com.github.tim91690.eclipse.mobs.boss.Demiurge;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlockProtection implements Listener {
    private final static Location loc = ConfigManager.getLoc();
    private final static ArrayList<PotionEffectType> debuffList = new ArrayList<>(List.of(
            PotionEffectType.WITHER,
            PotionEffectType.SLOW,
            PotionEffectType.WITHER,
            PotionEffectType.POISON,
            PotionEffectType.WEAKNESS));

    @EventHandler
    public void onBlockBreak(BlockBreakEvent b) {
        if (!b.getPlayer().getWorld().equals(loc.getWorld())) return;

        if (!b.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ) return;
        if (b.getBlock().getLocation().distance(loc) > 60) return;
        if (b.getBlock().getType().equals(Material.FIRE)) return;

        b.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDebuff(EntityPotionEffectEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (e.getCause().equals(EntityPotionEffectEvent.Cause.PLUGIN)) return;
        if (!p.getWorld().equals(loc.getWorld())) return;
        if (p.getLocation().distance(loc) > 60) return;
        if (e.getNewEffect() == null) return;
        if (!debuffList.contains(e.getNewEffect().getType())) return;
        int duration = e.getNewEffect().getDuration();
        int level = e.getNewEffect().getAmplifier();
        e.setCancelled(true);
        p.addPotionEffect(new PotionEffect(e.getNewEffect().getType(),duration/5,level));
    }

    @EventHandler
    public void onAnchorInteract(PlayerInteractEvent e) {
        if (!e.getPlayer().getWorld().equals(loc.getWorld())) return;
        if (e.getPlayer().getLocation().distance(loc) > 60) return;
        if (e.getClickedBlock() == null) return;
        if (!e.getClickedBlock().getType().equals(Material.RESPAWN_ANCHOR)) return;

        int rest = 4;

        for (EnergyPylon pylon : EnergyPylon.values()) {
            if (pylon.getHasActivated()) rest -=1;
        }
        if (rest > 0) {
            e.getPlayer().sendMessage(Component.text(ChatColor.RED+ "Il reste " + rest + " pylônes à activer"));
        } else {
            RespawnAnchor anchor = (RespawnAnchor) e.getClickedBlock().getBlockData();
            anchor.setCharges(0);
            e.getClickedBlock().setBlockData(anchor);
            loc.getWorld().playSound(loc,Sound.BLOCK_BEACON_POWER_SELECT,SoundCategory.PLAYERS,1,1);
            loc.getWorld().spawnParticle(Particle.TOTEM,loc.clone().add(0.5,5,0.5),1000,0.5,5,0.5,0,null,true);

            for (Boss boss : Boss.getBossList()) {
                if (!(boss instanceof Demiurge)) continue;
                ((LivingEntity)boss.getEntity()).damage(boss.getMaxHealth()/3);
                ((Demiurge) boss).setPhase(3);
                boss.getBossbar().setProgress(((LivingEntity)boss.getEntity()).getHealth()/boss.getMaxHealth());
            }

            for (EnergyPylon pylon : EnergyPylon.values()) {
                pylon.turnOff();
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (!e.getBlock().getWorld().equals(loc.getWorld())) return;
        if (e.getBlock().getLocation().distance(loc) > 60) return;
        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
            e.getBlock().setType(Material.AIR);
        },100);
    }

    @EventHandler
    public void onFireworkUse(PlayerElytraBoostEvent e) {
        if (!e.getPlayer().getWorld().equals(loc.getWorld())) return;
        Player p = e.getPlayer();

        if (p.getLocation().distance(loc) > 80) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onElytraAboveVent(PlayerMoveEvent e) {
        if (!e.getPlayer().getWorld().equals(loc.getWorld())) return;
        Player p = e.getPlayer();
        if (!p.isGliding()) return;
        if (p.getLocation().distance(loc) > 80) return;

        boolean isAboveVent = false;
        int distance = 6;
        for (int i = 1; i < 6; i++) {
            isAboveVent =  (p.getLocation().subtract(0,i,0).getBlock().getType().equals(Material.SOUL_CAMPFIRE));
            if (isAboveVent) {
                distance = i;
                break;
            }
        }
        if (!isAboveVent) return;

        p.setVelocity(p.getVelocity().add(new Vector(0,1/Math.pow(distance,0.3)*0.3,0)));
        p.playSound(p.getLocation(),Sound.BLOCK_LAVA_EXTINGUISH,SoundCategory.PLAYERS,1,2);
    }

    @EventHandler
    public void onJumppadWalk(PlayerMoveEvent e) {
        if (!e.getPlayer().getWorld().equals(loc.getWorld())) return;
        Player p = e.getPlayer();
        if (p.getLocation().distance(loc) > 80) return;

        if (!p.getLocation().subtract(0,1,0).getBlock().getType().equals(Material.EMERALD_BLOCK)) return;

        p.setVelocity(p.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(3.5));
        p.playSound(p.getLocation(),Sound.ENTITY_ENDER_DRAGON_FLAP,SoundCategory.PLAYERS,1,0.5f);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ) return;
        if (!e.getPlayer().getWorld().equals(loc.getWorld())) return;
        if (e.getBlock().getLocation().distance(loc) > 60) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onPylonMine(BlockBreakEvent b) {
        if (!b.getPlayer().getWorld().equals(loc.getWorld())) return;
        if (b.getBlock().getLocation().distance(loc) > 60) return;
        if (!b.getBlock().getType().equals(Material.BUDDING_AMETHYST)) return;
        if (b.getPlayer().getGameMode().equals(GameMode.CREATIVE)) EnergyPylon.getClosestPylon(b.getBlock().getLocation()).addProgress(100);
        EnergyPylon.getClosestPylon(b.getBlock().getLocation()).addProgress(1);
        b.getPlayer().playSound(b.getBlock().getLocation(),Sound.BLOCK_NOTE_BLOCK_BELL,SoundCategory.PLAYERS,1,2);
        b.getBlock().getWorld().spawnParticle(Particle.ELECTRIC_SPARK,b.getBlock().getLocation(),100,0.6,0.6,0.6,0,null,true);
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        if (!e.getEntity().getWorld().equals(loc.getWorld())) return;
        if (e.getEntity().getLocation().distance(loc) > 60) return;
        e.blockList().clear();
    }
}
