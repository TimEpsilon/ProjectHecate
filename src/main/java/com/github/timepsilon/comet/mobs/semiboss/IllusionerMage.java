package com.github.timepsilon.comet.mobs.semiboss;

import com.github.timepsilon.comet.mobs.CometMobs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class IllusionerMage extends CometMobs {

    private static final ItemStack IllusionerBow = Bow();

    private static ItemStack Bow() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK,1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE,3);
        return bow;
    }

    public IllusionerMage(Location loc) {
        super((LivingEntity)loc.getWorld().spawnEntity(loc, EntityType.ILLUSIONER),150);

        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.7);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(8);
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7);

        this.entity.getEquipment().setItemInMainHand(IllusionerBow);
        this.entity.getEquipment().setItemInMainHandDropChance(0.06f);

        this.entity.addScoreboardTag("SemiBoss");
    }
}