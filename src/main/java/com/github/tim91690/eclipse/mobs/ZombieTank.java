package com.github.tim91690.eclipse.mobs;


import com.github.tim91690.eclipse.misc.WeightCollection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

public class ZombieTank extends EntityZombie {

    public ZombieTank(Location loc) {
        //EntityTypes.be représente les zombies
        //qui est le tueur en série qui a décidé ça
        //Pourquoi tout NMS est écrit comme ça
        super(EntityTypes.be,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());

        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(25);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0.1);

        ((LivingEntity) this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,2000000,1,true,true));

        ((LivingEntity) this.getBukkitEntity()).getEquipment().setBoots(randomArmor("boots"));
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setLeggings(randomArmor("leggings"));
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setChestplate(randomArmor("chestplate"));
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setHelmet(randomArmor("helmet"));

        ((LivingEntity) this.getBukkitEntity()).getEquipment().setBootsDropChance(0.005F);
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setLeggingsDropChance(0.005F);
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setChestplateDropChance(0.005F);
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setHelmetDropChance(0.005F);

        //25HP au lieu des 8 originaux parce que l'armure est aléatoire
        this.setHealth(25);

        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("Eclipse");

        Team scarlet;
        //team
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet") == null) {
            scarlet = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("Scarlet");
            scarlet.setColor(ChatColor.DARK_RED);
        }
        else scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");
        scarlet.addEntry(((LivingEntity)this.getBukkitEntity()).getUniqueId().toString());
    }

    private ItemStack randomArmor(String piece) {
        WeightCollection<ItemStack> rc;
        switch (piece) {
            case "boots":
                rc = new WeightCollection<ItemStack>().add(90,new ItemStack(Material.LEATHER_BOOTS)).add(9,new ItemStack(Material.IRON_BOOTS)).add(0.9,new ItemStack(Material.DIAMOND_BOOTS)).add(0.1,new ItemStack(Material.NETHERITE_BOOTS));
                return rc.next();
            case "leggings":
                rc = new WeightCollection<ItemStack>().add(90,new ItemStack(Material.LEATHER_LEGGINGS)).add(9,new ItemStack(Material.IRON_LEGGINGS)).add(0.9,new ItemStack(Material.DIAMOND_LEGGINGS)).add(0.1,new ItemStack(Material.NETHERITE_LEGGINGS));
                return rc.next();
            case "chestplate":
                rc = new WeightCollection<ItemStack>().add(90,new ItemStack(Material.LEATHER_CHESTPLATE)).add(9,new ItemStack(Material.IRON_CHESTPLATE)).add(0.9,new ItemStack(Material.DIAMOND_CHESTPLATE)).add(0.1,new ItemStack(Material.NETHERITE_CHESTPLATE));
                return rc.next();
            case "helmet":
                rc = new WeightCollection<ItemStack>().add(90,new ItemStack(Material.LEATHER_HELMET)).add(9,new ItemStack(Material.IRON_HELMET)).add(0.9,new ItemStack(Material.DIAMOND_HELMET)).add(0.1,new ItemStack(Material.NETHERITE_HELMET));
                return rc.next();
        }
        return null;
    }

}