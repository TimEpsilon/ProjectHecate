package com.github.tim91690.eclipse.mobs.boss;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;

public class NightFairy extends Boss {

    public NightFairy(Location loc) {
        super(loc.getWorld().spawnEntity(loc, EntityType.BAT),300, ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"Night Fairy", BarColor.PINK);

        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);

    }
}
