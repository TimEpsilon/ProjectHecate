package com.github.tim91690.eclipse.mobs;

import com.github.tim91690.misc.WeightCollection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntitySkeleton;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class SkeletonSniper extends EntitySkeleton {

    private ItemStack Bow() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK,2);
        return bow;
    }

    private ItemStack Arrow() {
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

    public SkeletonSniper(Location loc) {
        super(EntityTypes.aB,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());

        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);

        ((LivingEntity) this.getBukkitEntity()).getEquipment().setItemInMainHand(Bow());
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setItemInMainHandDropChance(0f);
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setItemInOffHand(Arrow());
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setItemInOffHandDropChance(0.1f);

        ((LivingEntity) this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,2000000,0,true,true));

        this.setSilent(true);
        this.setHealth(50);

        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("Eclipse");

    }
}