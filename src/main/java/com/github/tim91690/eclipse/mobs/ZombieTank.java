package com.github.tim91690.eclipse.mobs;


import com.github.tim91690.eclipse.misc.WeightCollection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZombieTank extends EclipseMobs {
    int lvl;

    public ZombieTank(LivingEntity entity,int lvl) {
        super(entity,25);
        this.lvl = lvl;

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);
        this.entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0.1);

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,2000000,1,true,true));

        this.entity.getEquipment().setBoots(randomArmor(EquipmentSlot.FEET));
        this.entity.getEquipment().setLeggings(randomArmor(EquipmentSlot.LEGS));
        this.entity.getEquipment().setChestplate(randomArmor(EquipmentSlot.CHEST));
        this.entity.getEquipment().setHelmet(randomArmor(EquipmentSlot.HEAD));

        this.entity.getEquipment().setBootsDropChance(0.005F);
        this.entity.getEquipment().setLeggingsDropChance(0.005F);
        this.entity.getEquipment().setChestplateDropChance(0.005F);
        this.entity.getEquipment().setHelmetDropChance(0.005F);
    }

    public ZombieTank(Location loc, int lvl) {
        this((Zombie)loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE),lvl);
    }

    private ItemStack randomArmor(EquipmentSlot e) {
        WeightCollection<ItemStack> rc;
        float[] weight = getWeightByLevel();
        switch (e) {
            case FEET -> {
                rc = new WeightCollection<ItemStack>().add(weight[0], new ItemStack(Material.LEATHER_BOOTS)).add(weight[1], new ItemStack(Material.IRON_BOOTS)).add(weight[2], new ItemStack(Material.DIAMOND_BOOTS)).add(weight[3], new ItemStack(Material.NETHERITE_BOOTS));
                return rc.next();
            }
            case LEGS -> {
                rc = new WeightCollection<ItemStack>().add(weight[0], new ItemStack(Material.LEATHER_LEGGINGS)).add(weight[1], new ItemStack(Material.IRON_LEGGINGS)).add(weight[2], new ItemStack(Material.DIAMOND_LEGGINGS)).add(weight[3], new ItemStack(Material.NETHERITE_LEGGINGS));
                return rc.next();
            }
            case CHEST -> {
                rc = new WeightCollection<ItemStack>().add(weight[0], new ItemStack(Material.LEATHER_CHESTPLATE)).add(weight[1], new ItemStack(Material.IRON_CHESTPLATE)).add(weight[2], new ItemStack(Material.DIAMOND_CHESTPLATE)).add(weight[3], new ItemStack(Material.NETHERITE_CHESTPLATE));
                return rc.next();
            }
            case HEAD -> {
                rc = new WeightCollection<ItemStack>().add(weight[0], new ItemStack(Material.LEATHER_HELMET)).add(weight[1], new ItemStack(Material.IRON_HELMET)).add(weight[2], new ItemStack(Material.DIAMOND_HELMET)).add(weight[3], new ItemStack(Material.NETHERITE_HELMET));
                return rc.next();
            }
        }
        return null;
    }

    private float[] getWeightByLevel() {
        float[] weight;

        switch (this.lvl) {
            case 1:
                weight = new float[] {90, 9, 0.9f, 0.1f};
                break;
            case 2:
                weight = new float[] {0.1f,90,9,0.9f};
                break;
            case 3:
                weight = new float[] {0.1f,0.9f,90,9};
                break;
            case 4:
                weight = new float[] {0.1f,0.9f,9,90};
                break;
            default:
                weight = new float[] {1,1,1,1};
        }
        return weight;
    }

}