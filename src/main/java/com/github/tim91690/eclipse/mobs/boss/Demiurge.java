package com.github.tim91690.eclipse.mobs.boss;


import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.misc.WeightCollection;
import com.github.tim91690.eclipse.mobs.*;
import com.github.tim91690.eclipse.mobs.semiboss.DrownedOverlord;
import com.github.tim91690.eclipse.mobs.semiboss.IllusionerMage;
import com.github.tim91690.eclipse.mobs.semiboss.PhantomFurries;
import com.github.tim91690.eclipse.mobs.semiboss.RavagerBeast;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.WorldServer;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


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
                attackMob();
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

    private void attackMob() {
        WorldServer world = ((CraftWorld) this.getEntity().getLocation().getWorld()).getHandle();
        Location loc = this.getEntity().getLocation();
        switch (this.phase) {
            case 1:
            case 2:
                this.getEntity().getWorld().playSound(loc,Sound.BLOCK_LAVA_EXTINGUISH,SoundCategory.HOSTILE,3,0);
                this.getEntity().getWorld().spawnParticle(Particle.SMOKE_NORMAL,loc.clone().add(0,-1,0),30,1,1,1,0,null,true);
                WeightCollection<String> rc;
                rc = new WeightCollection<String>()
                        .add(30,"CreeperBomb")
                        .add(5,"EvokerSorcerer")
                        .add(35,"SkeletonSniper")
                        .add(30,"SpiderCrawler")
                        .add(25,"SpiderHerd")
                        .add(40,"ZombieTank");
                for (int i = 0; i<7;i++) {
                    switch (rc.next()) {
                        case "CreeperBomb":
                            CreeperBomb cb = new CreeperBomb(loc);
                            world.addEntity(cb, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                        case "EvokerSorcerer":
                            EvokerSorcerer es = new EvokerSorcerer(loc);
                            world.addEntity(es, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                        case "SkeletonSniper":
                            SkeletonSniper ss = new SkeletonSniper(loc);
                            world.addEntity(ss, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                        case "SpiderCrawler":
                            SpiderCrawler sc = new SpiderCrawler(loc);
                            world.addEntity(sc, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                        case "SpiderHerd":
                            SpiderHerd sh = new SpiderHerd(loc);
                            world.addEntity(sh, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                        case "ZombieTank":
                            ZombieTank zt = new ZombieTank(loc);
                            world.addEntity(zt, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                    }
                }
                break;
            case 3:
            case 4:
                WeightCollection<String> rcboss;
                rcboss = new WeightCollection<String>()
                        .add(10,"DrownedOverlord")
                        .add(20,"IllusionerMage")
                        .add(15,"PhantomFurries")
                        .add(20,"RavagerBeast");
                for (int i = 0; i<3;i++) {
                    switch (rcboss.next()) {
                        case "DrownedOverlord":
                            DrownedOverlord Do = new DrownedOverlord(loc);
                            world.addEntity(Do, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                        case "IllusionerMage":
                            IllusionerMage im = new IllusionerMage(loc);
                            world.addEntity(im, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                        case "PhantomFurries":
                            PhantomFurries pf = new PhantomFurries(loc);
                            world.addEntity(pf, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                        case "RavagerBeast":
                            RavagerBeast rb = new RavagerBeast(loc);
                            world.addEntity(rb, CreatureSpawnEvent.SpawnReason.NATURAL);
                            break;
                    }
                }
                break;
        }
    }

    private void attackWither(List<Player> proxplayer) {
        switch (this.phase) {
            case 2:
            case 1:
                Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()-> {
                    EulerAngle angle = new EulerAngle(Math.random()*360,Math.random()*360,Math.random()*360);
                    Location loc = this.entity.getLocation().add(new Vector(Math.cos(angle.getX()), 0d, Math.sin(i * 2 * Math.PI / 20)).multiply(0.2));
                    loc.setYaw(i*360f/20f-90f);
                    loc.setPitch(0);
                    WitherSkull skull = (WitherSkull)this.entity.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
                    skull.setDirection(new Vector(Math.cos(i*2*Math.PI/23),0d,Math.sin(i*2*Math.PI/23)).multiply(0.1));
                },0,2);
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
