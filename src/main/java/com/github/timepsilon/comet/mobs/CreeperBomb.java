package com.github.timepsilon.comet.mobs;

import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.misc.WeightCollection;
import com.github.timepsilon.comet.mobs.semiboss.DrownedOverlordHorse;
import com.github.timepsilon.comet.mobs.semiboss.IllusionerMage;
import com.github.timepsilon.comet.mobs.semiboss.PhantomFurries;
import com.github.timepsilon.comet.mobs.semiboss.RavagerBeast;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

public class CreeperBomb extends CometMobs implements Listener {


    public CreeperBomb(LivingEntity creeper) {
        super(creeper,40);
        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);

        ((Creeper)this.entity).setPowered(true);
    }

    public CreeperBomb(Location loc) {
        this((Creeper)loc.getWorld().spawnEntity(loc, EntityType.CREEPER));
    }

}