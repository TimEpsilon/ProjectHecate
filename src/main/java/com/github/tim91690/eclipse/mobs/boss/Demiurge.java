package com.github.tim91690.eclipse.mobs.boss;


import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.misc.ConfigManager;
import com.github.tim91690.eclipse.misc.Laser;
import com.github.tim91690.eclipse.misc.WeightCollection;
import com.github.tim91690.eclipse.mobs.*;
import com.github.tim91690.eclipse.mobs.semiboss.DrownedOverlordHorse;
import com.github.tim91690.eclipse.mobs.semiboss.IllusionerMage;
import com.github.tim91690.eclipse.mobs.semiboss.PhantomFurries;
import com.github.tim91690.eclipse.mobs.semiboss.RavagerBeast;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;


public class Demiurge extends Boss {

    private Location center;
    private final ArmorStand ring1;
    private final ArmorStand ring2;
    private final ArmorStand core;
    private final ArmorStand shell;
    private final ArmorStand wings;
    private int phase;
    private int tick;

    /** 4 phases
     * 1) Le demiurge se téléporte autour de l'arène, lançant divers projectiles et spawnant des mobs + attaques spéciales, -> 75% HP
     * 2) Le demiurge augmente son armure et devient quasi invinsible, il faut alors tuer le boss qui apparait. -> 50% HP
     * 3) Le demiurge se téléporte plus souvent et brusquement, spawn des mini-boss, + d'attaques spéciales -> 25% HP
     * 4) Le demiurge perd ses anneaux et passe en mode vitesse, les joueurs subissent de l'antigravité et sont éjectés
     */
    public Demiurge(Location loc) {
        super(loc.getWorld().spawnEntity(loc, EntityType.MAGMA_CUBE),2040,ChatColor.BOLD+""+ChatColor.AQUA+"Demiurge", BarColor.GREEN);

        Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', "&eLe &k&bDemiurge &ea spawn en &a<" + (int) loc.getX() + " , " + (int) loc.getY() + " , " + (int) loc.getZ() + ">")));

        this.center = ConfigManager.getLoc();

        this.entity.setCustomName(this.name);
        this.entity.setAI(false);
        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100000000,0,false,false));
        ((MagmaCube)this.entity).setSize(2);
        this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getMaxHealth());
        this.entity.setHealth(this.getMaxHealth());

        this.phase=1;

        this.ring1 = modelConstruct(100);
        this.ring2 = modelConstruct(100);
        this.core = modelConstruct(200);
        this.shell = modelConstruct(300);
        this.wings = modelConstruct(400,true);

        tick();
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    private void tick() {
        //période d'oscillation/rotation de chaque composante
        float Tcore = 300;
        float Tshell = 800;
        float Tring1 = 1400;
        float Tring2 = 1600;
        float Twings = 350;
        this.tick = Bukkit.getScheduler().runTaskTimerAsynchronously(EventManager.getPlugin(),() -> {
            int t = this.entity.getTicksLived();

            Vector move = this.getEntity().getLocation().subtract(this.center.clone()).toVector().normalize().rotateAroundY(Math.PI/2).multiply(0.02).setY(0);
            this.getEntity().teleport(this.getEntity().getLocation().add(move));

            //model position
            float yaw = this.getEntity().getLocation().getYaw();
            this.ring1.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.ring1.setRotation(yaw,0);
            this.ring2.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.ring2.setRotation(yaw,0);
            this.core.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.core.setRotation(yaw,0);
            this.shell.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.shell.setRotation(yaw,0);
            this.wings.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.wings.setRotation(yaw,0);

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
        if ((int)(Math.random()*2) == 0) {
            this.getEntity().teleport(this.center.clone().add(Math.random()*20-10,13+Math.random()*4,Math.random()*20-10));
            this.getEntity().getLocation().setYaw((float)Math.random()*360);
            this.getEntity().getWorld().playSound(this.getEntity().getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,SoundCategory.HOSTILE,2,1);
        }
        WeightCollection<String> rc;
        rc = new WeightCollection<String>()
                .add(50,"mob")
                .add(40,"witherwave")
                .add(50,"laser")
                .add(50,"fireball")
                .add(30,"vortex");
                //.add(20,"dash")
                //.add(10,"swap")
                //.add(40,"meteorshower")
                //.add(5,"assimilation");
        String attack = rc.next();

        switch (attack) {
            case "mob":
                attackMob();
                break;
            case "laser":
                attackLaser(proxPlayer);
                break;
            case "witherwave":
                attackWither(proxPlayer);
                break;
            case "fireball":
                attackFireball();
                break;
            case "vortex":
                attackVortex(proxPlayer);
                break;
            case "dash":
                //todo dash
                break;
            case "swap":
                //todo swap
                break;
            case "meteorshower":
                //todo meteorshower
                break;
            case "assimilation":
                //todo assimilation
                break;
        }
    }

    private void attackMob() {
        //TODO abandonner nms si possible
        Location loc = this.getEntity().getLocation().add(0,-1,0);
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
                            //new CreeperBomb(loc.clone().add(Vector.getRandom()));
                            break;
                        case "EvokerSorcerer":
                            //new EvokerSorcerer(loc.clone().add(Vector.getRandom()));
                            break;
                        case "SkeletonSniper":
                            //new SkeletonSniper(loc.clone().add(Vector.getRandom()));
                            break;
                        case "SpiderCrawler":
                            //new SpiderCrawler(loc.clone().add(Vector.getRandom()));
                            break;
                        case "SpiderHerd":
                            //new SpiderHerd(loc.clone().add(Vector.getRandom()));
                            break;
                        case "ZombieTank":
                            //new ZombieTank(loc.clone().add(Vector.getRandom()),4);

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
                for (int i = 0; i<2;i++) {
                    switch (rcboss.next()) {
                        case "DrownedOverlord":
                            new DrownedOverlordHorse(loc.clone().add(Vector.getRandom()));
                            break;
                        case "IllusionerMage":
                            new IllusionerMage(loc.clone().add(Vector.getRandom()));
                            break;
                        case "PhantomFurries":
                            new PhantomFurries(loc.clone().add(Vector.getRandom()));
                            break;
                        case "RavagerBeast":
                            new RavagerBeast(loc.clone().add(Vector.getRandom()));
                            break;
                    }
                }
                break;
        }
    }

    private void attackWither(List<Player> proxPlayer) {
        switch (this.phase) {
            case 4:
            case 3:
                for (Player p : proxPlayer) {
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                        Vector direction = p.getLocation().toVector().subtract(this.getEntity().getLocation().toVector()).clone().normalize().multiply(2);
                        WitherSkull skull = (WitherSkull)this.entity.getWorld().spawnEntity(this.getEntity().getLocation(), EntityType.WITHER_SKULL);
                        skull.setCharged(true);
                        skull.setDirection(direction); 
                        },60);
                }


            case 2:
                ArrayList<Integer> tasks = new ArrayList<>();
                for (int i = 0;i<8;i++) {
                    Vex vex = (Vex)this.entity.getWorld().spawnEntity(this.entity.getLocation(),EntityType.VEX);

                    vex.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,10000000,0,false,false));
                    vex.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(6);
                    vex.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30);
                    vex.setHealth(30);
                    vex.setSilent(true);

                    vex.addScoreboardTag("Eclipse");

                    final int j = i;
                    tasks.add(
                            Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
                                if (vex.isDead()) Bukkit.getScheduler().cancelTask(tasks.get(j));
                                vex.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,vex.getLocation(),
                                        10, 0.1, 0.1,0.1,0);
                            },0,1).getTaskId()
                    );

                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> vex.setHealth(0),200);
                }

            case 1:
                int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()-> {
                    double yaw = 2*Math.random()*Math.PI;
                    double pitch = -Math.random()*Math.PI/2;
                    double coscos = Math.cos(pitch) * Math.cos(yaw);
                    double cossin = -Math.cos(pitch) * Math.sin(yaw);
                    Location loc = this.entity.getLocation().add(new Vector(cossin, Math.sin(pitch), coscos).multiply(0.2));
                    loc.setYaw((float)yaw);
                    loc.setPitch((float)pitch);
                    WitherSkull skull = (WitherSkull)this.entity.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
                    skull.setDirection(new Vector(cossin, Math.sin(pitch), coscos).multiply(0.2));
                },0,2).getTaskId();
                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),()->{
                    Bukkit.getScheduler().cancelTask(task);
                },50);
        }
    }

    private void attackLaser(List<Player> proxPlayer) {
        for (int i = 0; i < this.phase ; i++) {
            Player target = proxPlayer.get((int)Math.random()*proxPlayer.size());
            target.playSound(this.getEntity().getLocation(),Sound.ENTITY_GUARDIAN_ATTACK,SoundCategory.HOSTILE,4,1);
            Laser laser = null;
            try {
                laser = new Laser.GuardianLaser(this.getEntity().getLocation(),target.getLocation().add(0,-0.5,0),60,64);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            laser.start(EventManager.getPlugin());
            Laser finalLaser = laser;
            int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
                finalLaser.moveEnd(target.getLocation().add(0,-0.5,0),1,null);
                finalLaser.moveStart(this.getEntity().getLocation(),1,null);
            },0,1).getTaskId();

            Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                finalLaser.stop();
                Bukkit.getScheduler().cancelTask(task);
                Vector direction = this.getEntity().getLocation().toVector().subtract(target.getLocation().toVector()).normalize().multiply(-1);
                this.getEntity().getWorld().spawnArrow(this.getEntity().getLocation().add(direction),direction,11,0);
            },60);


        }
    }

    private void attackFireball() {
        switch (this.phase) {
            case 1:
                for (double a = 0; a < 2*Math.PI;a = a + Math.PI/8) {
                    SmallFireball fireball = (SmallFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+Math.random())*2,0,Math.sin(a+Math.random())*2), EntityType.SMALL_FIREBALL);
                    fireball.setVelocity(new Vector(0,-1,0));
                    fireball.setDirection(new Vector(0,-1,0));
                    fireball = (SmallFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+Math.random())*5,0,Math.sin(a+Math.random())*5), EntityType.SMALL_FIREBALL);
                    fireball.setVelocity(new Vector(0,-0.5,0));
                    fireball.setDirection(new Vector(0,-0.5,0));
                }
                break;
            case 2:
                for (double a = 0; a < 2*Math.PI;a = a + Math.PI/10) {
                    LargeFireball fireball = (LargeFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+Math.random())*3,0,Math.sin(a+Math.random())*3), EntityType.FIREBALL);
                    fireball.setVelocity(new Vector(0,-1,0));
                    fireball.setDirection(new Vector(0,-1,0));
                    SmallFireball smallFireball = (SmallFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+Math.random())*5,0,Math.sin(a+Math.random())*5), EntityType.SMALL_FIREBALL);
                    smallFireball.setVelocity(new Vector(0,-1,0));
                    smallFireball.setDirection(new Vector(0,-1,0));
                }
                break;
            case 3:
                for (double a = 0; a < 2*Math.PI;a = a + Math.PI/13) {
                    LargeFireball fireball = (LargeFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+Math.random())*3,0,Math.sin(a+Math.random())*3), EntityType.FIREBALL);
                    fireball.setVelocity(new Vector(0,-1.5,0));
                    fireball.setDirection(new Vector(0,-1,0));
                    fireball = (LargeFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+Math.random())*6,0,Math.sin(a+Math.random())*6), EntityType.FIREBALL);
                    fireball.setVelocity(new Vector(0,-1.5,0));
                    fireball.setDirection(new Vector(0,-1,0));
                }
                break;
            case 4:
                for (double a = 0; a < 2*Math.PI;a = a + Math.PI/4) {
                    DragonFireball fireball = (DragonFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+Math.random())*3,0,Math.sin(a+Math.random())*3), EntityType.DRAGON_FIREBALL);
                    fireball.setVelocity(new Vector(0,-2,0));
                    fireball.setDirection(new Vector(0,-1,0));
                }
                break;
        }

    }

    private void attackVortex(List<Player> proxPlayer) {
        switch (this.phase) {
            case 1:
                for (Player p : proxPlayer) {
                    Vector toVortex = p.getLocation().toVector().subtract(this.getEntity().getLocation().toVector()).normalize();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,60,1,true,true));
                    p.setVelocity(toVortex.multiply(-0.3));
                }
                break;
            case 2:
                for (Player p : proxPlayer) {
                    Vector toVortex = p.getLocation().toVector().subtract(this.getEntity().getLocation().toVector()).normalize();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,80,1,true,true));
                    p.setVelocity(toVortex.multiply(-0.7));
                }
                break;
            case 3:
                for (Player p : proxPlayer) {
                    Vector toVortex = p.getLocation().toVector().subtract(this.getEntity().getLocation().toVector()).normalize();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,80,2,true,true));
                    p.setVelocity(toVortex.multiply(-1));
                }
                break;
            case 4:
                for (Player p : proxPlayer) {
                    Vector toVortex = p.getLocation().toVector().subtract(this.getEntity().getLocation().toVector()).normalize();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,100,2,true,true));
                    p.setVelocity(toVortex.multiply(-1.3));
                }
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
