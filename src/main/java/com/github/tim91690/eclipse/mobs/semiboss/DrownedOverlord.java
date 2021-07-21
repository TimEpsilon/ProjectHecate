package com.github.tim91690.eclipse.mobs.semiboss;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.horse.EntityHorseZombie;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;


public class DrownedOverlord extends EntityHorseZombie {

    public DrownedOverlord(Location loc) {
        super(EntityTypes.bf,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(), loc.getZ());

        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(15);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);

        Drowned e = DrownedOverlordRider((Drowned) loc.getWorld().spawnEntity(loc, EntityType.DROWNED));
        ((ZombieHorse) this.getBukkitEntity()).addPassenger(e);

        this.setHealth(30);

        Team scarlet;
        //team
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet") == null) {
            scarlet = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("Scarlet");
            scarlet.setColor(ChatColor.DARK_RED);
        }
        else scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");
        scarlet.addEntry(((LivingEntity)this.getBukkitEntity()).getUniqueId().toString());

        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("Eclipse");
        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("SemiBoss");
    }

    private Drowned DrownedOverlordRider(Drowned e) {

        e.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(120);
        e.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        e.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        e.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(-6);

        e.getEquipment().setItemInMainHand(Trident());
        e.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
        e.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        e.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        e.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));

        e.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,2000000,0,true,true));

        Team scarlet;
        //team
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet") == null) {
            scarlet = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("Scarlet");
            scarlet.setColor(ChatColor.DARK_RED);
        }
        else scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");
        scarlet.addEntry(e.getUniqueId().toString());

        e.addScoreboardTag("Eclipse");
        e.addScoreboardTag("SemiBoss");
        e.setHealth(120);

        return e;
    }

    private ItemStack Trident() {
        ItemStack trident = new ItemStack(Material.TRIDENT);
        trident.addEnchantment(Enchantment.IMPALING,3);
        return trident;
    }
}