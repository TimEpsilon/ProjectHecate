package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.EventManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!EventManager.isRunningEvent) return;
        Player p = e.getPlayer();
        if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade"))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade"));
    }
}
