package com.github.tim91690.eclipse.mobs.boss;


import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.misc.WeightCollection;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;

import java.util.List;


public class Demiurge extends Boss {

    private ArmorStand ring1;
    private ArmorStand ring2;
    private ArmorStand core;
    private ArmorStand shell;
    private ArmorStand wings;
    private int phase;
    private int tick;

    /** 4 phases
     * 1) Le demiurge se téléporte autour de l'arène, lançant divers projectiles et spawnant des mobs + attaques spéciales, -> 75% HP
     * 2) Le demiurge augmente son armure et devient quasi invinsible, il faut alors activer chacun des piliers en tuant le boss qui lui est associé.
     *      Chaque pilier le blesse -> 50% HP
     * 3) Le demiurge se téléporte plus souvent et brusquement, spawn des mini-boss, + d'attaques spéciales -> 25% HP
     * 4) Le demiurge perd ses anneaux et passe en mode vitesse, les joueurs subissent de l'antigravité et sont éjectés
     */
    public Demiurge(Location loc) {
        super(loc.getWorld().spawnEntity(loc, EntityType.MAGMA_CUBE),2040,ChatColor.BOLD+""+ChatColor.AQUA+"Demiurge", BarColor.GREEN);

        Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', "&eLe &k&bDemiurge &ea spawn en &a<" + (int) loc.getX() + " , " + (int) loc.getY() + " , " + (int) loc.getZ() + ">")));

        this.entity.setCustomName(this.name);
        this.entity.setAI(false);
        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100000000,0,false,false));
        ((MagmaCube)this.entity).setSize(2);

        this.phase=1;

        this.ring1 = modelConstruct(1);
        this.ring2 = modelConstruct(1);
        this.core = modelConstruct(2);
        this.shell = modelConstruct(3);
        this.wings = modelConstruct(4,true);

        tick();
    }

    private void tick() {
        //période d'oscillation/rotation de chaque composante
        float Tcore = 300;
        float Tshell = 800;
        float Tring1 = 1600;
        float Tring2 = 2000;
        float Twings = 1000;
        this.tick = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
            int t = this.entity.getTicksLived();

            //model position
            this.ring1.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.ring2.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.core.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.shell.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.wings.teleport(this.entity.getLocation().clone().add(0,-0.7,0));

            //model rotation
            this.core.setHeadPose(new EulerAngle(0,t/Tcore*2*Math.PI,0));
            this.shell.setHeadPose(new EulerAngle(0,-t/Tshell*2*Math.PI,0));
            this.ring1.setHeadPose(new EulerAngle(Math.PI/2,0,t/Tring1*2*Math.PI));
            this.ring2.setHeadPose(new EulerAngle(0,-t/Tring2*2*Math.PI,0));
            double theta = 20*Math.PI/180*Math.cos(t / Twings * 2 * Math.PI);
            this.wings.setRightArmPose(new EulerAngle(theta,0,Math.PI/2));
            this.wings.setLeftArmPose(new EulerAngle(theta,0,-Math.PI/2));

        },0,1).getTaskId();
    }

    @Override
    public void death() {
        bossList.remove(this);
        this.bossbar.removeAll();
        this.ring1.remove();
        this.ring2.remove();
        this.core.remove();
        this.shell.remove();
        this.wings.remove();
        Bukkit.getScheduler().cancelTask(this.tick);
    }

    @Override
    public void attack(List<Player> proxPlayer) {
        WeightCollection<String> rc;
        rc = new WeightCollection<String>()
                .add(50,"mob")
                .add(50,"laser")
                .add(40,"witherwave")
                .add(50,"fireball")
                .add(30,"vortex")
                .add(20,"dash")
                .add(10,"swap")
                .add(40,"meteorshower")
                .add(5,"assimilation");
        String attack = rc.next();

        switch (attack) {
            case "mob":
                //to do
                break;
            case "laser":
                //to do
                break;
            case "witherwave":
                //to do
                break;
            case "fireball":
                //to do
                break;
            case "vortex":
                //to do
                break;
            case "dash":
                //to do
                break;
            case "swap":
                //to do
                break;
            case "meteorshower":
                //to do
                break;
            case "assimilation":
                //to do
                break;
        }
    }

    private ArmorStand modelConstruct(int cmd) {
       return modelConstruct(cmd,false);
    }

    private ArmorStand modelConstruct(int cmd,boolean isArms) {
        ItemStack modelItem = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = modelItem.getItemMeta();
        ArmorStand model = (ArmorStand) this.entity.getWorld().spawnEntity(this.entity.getLocation(), EntityType.ARMOR_STAND);

        model.setMarker(true);
        model.setInvisible(true);
        meta.setCustomModelData(cmd);
        modelItem.setItemMeta(meta);
        if (!isArms) {
            model.getEquipment().setHelmet(modelItem);
        } else {
            model.getEquipment().setItemInMainHand(modelItem);
            model.getEquipment().setItemInOffHand(modelItem);
            model.setArms(true);
        }
        return model;
    }




}
