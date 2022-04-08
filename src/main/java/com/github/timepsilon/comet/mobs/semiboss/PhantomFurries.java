package com.github.timepsilon.comet.mobs.semiboss;

import com.github.timepsilon.comet.mobs.CometMobs;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PhantomFurries extends CometMobs {

    public PhantomFurries(Location loc) {
        super((LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM),100);

        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(3);

        ((Phantom)this.entity).setSize(10);

        this.entity.addScoreboardTag("SemiBoss");

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,2000000,0,true,true));
    }
}