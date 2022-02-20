package com.github.tim91690.eclipse.mobs;

import com.github.tim91690.eclipse.misc.WeightCollection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class SkeletonSniper extends EclipseMobs {

    public static ItemStack SkeletonBow = Bow();

    public SkeletonSniper(LivingEntity entity) {
        super(entity,50);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);

        this.entity.getEquipment().setItemInMainHand(SkeletonBow);
        this.entity.getEquipment().setItemInMainHandDropChance(0f);
        this.entity.getEquipment().setItemInOffHand(RandomArrow());
        this.entity.getEquipment().setItemInOffHandDropChance(0.1f);

        this.entity.setSilent(true);
    }

    public SkeletonSniper(Location loc) {
        this((Skeleton)loc.getWorld().spawnEntity(loc,EntityType.SKELETON));
    }

    private static ItemStack Bow() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK,2);
        return bow;
    }

    public static ItemStack RandomArrow() {
        ItemStack arrow = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta meta = (PotionMeta) arrow.getItemMeta();
        WeightCollection<PotionData> rc;
        rc = new WeightCollection<PotionData>()
                .add(5,new PotionData(PotionType.SLOWNESS))
                .add(3,new PotionData(PotionType.WEAKNESS))
                .add(2,new PotionData(PotionType.POISON))
                .add(1,new PotionData(PotionType.INSTANT_DAMAGE));
        PotionData effect = rc.next();
        meta.setBasePotionData(effect);

        arrow.setItemMeta(meta);
        return arrow;
    }

}