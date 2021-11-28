package com.github.tim91690.eclipse.structure;

import com.github.tim91690.eclipse.misc.ConfigManager;
import com.sk89q.worldedit.event.platform.BlockInteractEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockProtection implements Listener {
    private static Location loc = ConfigManager.getLoc();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent b) {

        if (!b.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ) return;
        if (b.getBlock().getLocation().distance(loc) > 60) return;

        b.setCancelled(true);

    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {

        if (e.getBlock().getLocation().distance(loc) > 60) return;

        e.setCancelled(true);

        e.getBlock().setType(Material.RESPAWN_ANCHOR);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        if (!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ) return;
        if (e.getBlock().getLocation().distance(loc) > 60) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void fire(BlockIgniteEvent e) {
        if (e.getBlock().getLocation().distance(loc) > 60 ) return;

        e.setCancelled(true);
    }

}
