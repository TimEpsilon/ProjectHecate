package com.github.tim91690.eclipse.structure;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.github.tim91690.eclipse.misc.ConfigManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Objects;

public class BlockProtection implements Listener {
    private static Location loc = ConfigManager.getLoc();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent b) {

        if (!b.getPlayer().getWorld().equals(loc.getWorld())) return;

        if (!b.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ) return;
        if (b.getBlock().getLocation().distance(loc) > 60) return;

        b.setCancelled(true);
    }

    @EventHandler
    public void onAnchorInteract(PlayerInteractEvent e) {
        if (!e.getPlayer().getWorld().equals(loc.getWorld())) return;
        if (!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ) return;
        if (e.getPlayer().getLocation().distance(loc) > 60) return;
        if (!Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.RESPAWN_ANCHOR)) return;
        e.setCancelled(true);
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

        p.setVelocity(p.getVelocity().add(new Vector(0,1/Math.pow(distance,0.5)*0.3,0)));
        p.playSound(p.getLocation(),Sound.BLOCK_LAVA_EXTINGUISH,SoundCategory.PLAYERS,1,2);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ) return;
        if (!e.getPlayer().getWorld().equals(loc.getWorld())) return;
        if (e.getBlock().getLocation().distance(loc) > 60) return;

        e.setCancelled(true);
    }
}
