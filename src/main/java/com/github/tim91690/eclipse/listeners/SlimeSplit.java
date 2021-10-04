package com.github.tim91690.eclipse.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;

public class SlimeSplit implements Listener {

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent e) {
        if (!e.getEntity().getScoreboardTags().contains("Boss")) return;
        e.setCancelled(true);
    }
}
