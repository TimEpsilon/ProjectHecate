package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.misc.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

public class CustomDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        Component message = e.deathMessage().color(NamedTextColor.GRAY).decorate(TextDecoration.ITALIC);
        message = Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC + "[DEATH] ").append(message);
        e.deathMessage(message);

        Location loc = e.getPlayer().getLocation();

        if (loc.distance(ConfigManager.getLoc()) < 160) return;

        loc.add(Vector.getRandom().subtract(new Vector(0.5,0,0.5).multiply(64)));

        while (loc.getBlock().isEmpty()) loc.subtract(0,1,0);
        while (!loc.getBlock().isEmpty()) loc.add(0,1,0);

        e.getPlayer().setBedSpawnLocation(loc,true);
    }
}
