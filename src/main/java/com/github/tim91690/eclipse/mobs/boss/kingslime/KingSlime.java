package com.github.tim91690.eclipse.mobs.boss.kingslime;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KingSlime {
    BossBar bar;
    Slime slime;

    public KingSlime(Location loc) {
        this.bar = createBossbar();
        this.slime = (Slime) loc.getWorld().spawnEntity(loc, EntityType.SLIME);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&eUn &2&lKing Slime &ea spawn en &a< "+(int)loc.getX()+" , "+(int)loc.getY()+" , "+(int)loc.getZ()+" >"));


        this.slime.setSize(15);

        this.slime.setCustomName(ChatColor.translateAlternateColorCodes('&',"&2&lKing Slime"));
        this.slime.setCustomNameVisible(true);

        this.slime.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,2000000,3));
        this.slime.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,2000000,2));

        this.slime.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(60);
        this.slime.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
        this.slime.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7);
        this.slime.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(12);
        this.slime.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(2);
        this.slime.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(250);

        this.slime.setHealth(250);

        this.slime.addScoreboardTag("Eclipse");
        this.slime.addScoreboardTag("Boss");

        showBossbar();
    }

    private BossBar createBossbar() {
        BossBar bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&',"&2&lKing Slime"), BarColor.BLUE, BarStyle.SOLID);
        return bar;
    }

    public BossBar getBossbar() {
        return this.bar;
    }

    private void showBossbar() {
        for (Entity e : this.slime.getNearbyEntities(48,48,48)) {
            if (!(e instanceof Player)) continue;
            this.bar.addPlayer((Player)e);
        }
    }

}
