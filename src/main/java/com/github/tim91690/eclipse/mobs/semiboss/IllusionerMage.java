package com.github.tim91690.eclipse.mobs.semiboss;

import com.github.tim91690.eclipse.mobs.EclipseMobs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class IllusionerMage extends EclipseMobs {

    private ItemStack IllusionerBow = Bow();

    private ItemStack Bow() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK,2);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE,4);
        return bow;
    }

    public IllusionerMage(Location loc) {
        super((LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.ILLUSIONER),150);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(30);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.9);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(8);
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7);

        this.entity.getEquipment().setItemInMainHand(IllusionerBow);
        this.entity.getEquipment().setItemInMainHandDropChance(0.06f);

        this.entity.addScoreboardTag("SemiBoss");
    }
}