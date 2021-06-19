package com.github.tim91690.eclipse.mobs.semiboss;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityIllagerIllusioner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class IllusionerMage extends EntityIllagerIllusioner {

    private ItemStack Bow() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK,2);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE,4);
        return bow;
    }

    public IllusionerMage(Location loc) {
        super(EntityTypes.O,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());

        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(150);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(30);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.9);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(8);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7);

        this.setHealth(150);

        ((LivingEntity) this.getBukkitEntity()).getEquipment().setItemInMainHand(Bow());
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setItemInMainHandDropChance(0.06f);

        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("Eclipse");
        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("SemiBoss");


    }
}