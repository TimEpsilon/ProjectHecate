package com.github.tim91690.eclipse.mobs.semiboss;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityRavager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

public class RavagerBeast extends EntityRavager {

    public RavagerBeast(Location loc) {
        super(EntityTypes.av,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());

        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(120);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.8);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(5);

        this.setHealth(120);

        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("Eclipse");
        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("SemiBoss");

        Team scarlet;
        //team
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet") == null) {
            scarlet = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("Scarlet");
            scarlet.setColor(ChatColor.DARK_RED);
        }
        else scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");
        scarlet.addEntry(((LivingEntity)this.getBukkitEntity()).getUniqueId().toString());

        ((LivingEntity) this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,2000000,1,true,true));

    }
}