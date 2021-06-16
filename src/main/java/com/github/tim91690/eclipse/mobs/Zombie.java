package com.github.tim91690.eclipse.mobs;


import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Zombie extends EntityZombie {

    public Zombie(Location loc) {
        //EntityTypes.be représente les zombies
        //qui est le tueur en série qui a décidé ça
        //Pourquoi tout NMS est écrit comme ça
        super(EntityTypes.be,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());

        //15HP au lieu des 8 originaux parce que l'armure est aléatoire
        this.setHealth(15);
        this.setCustomNameVisible(true);
        this.setCustomName(new ChatComponentText("test"));

        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(15);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0.1);

        ((LivingEntity) this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,2000000,1));

        ((LivingEntity) this.getBukkitEntity()).getEquipment().setBoots(randomArmor("boots"));

    }

    private ItemStack randomArmor(String piece) {
        switch (piece) {
            case "boots":


            case "leggings":


            case "chestplate":


            case "helmet":


        }
    }

}
