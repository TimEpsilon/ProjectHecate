package com.github.tim91690.eclipse.structure;

import com.github.tim91690.eclipse.misc.ConfigManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockProtection implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent b) {
        Location loc = ConfigManager.getLoc();

        if (!b.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ) return;
        if (b.getBlock().getLocation().distance(loc) > 60) return;

        b.setCancelled(true);

    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e) {
        Location loc = ConfigManager.getLoc();

        if (e.getBlock().getLocation().distance(loc) > 60) return;

        e.setCancelled(true);

        e.getBlock().setType(Material.RESPAWN_ANCHOR);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Location loc = ConfigManager.getLoc();

        if (!e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ) return;
        if (e.getBlock().getLocation().distance(loc) > 60) return;

        e.setCancelled(true);
    }
}
