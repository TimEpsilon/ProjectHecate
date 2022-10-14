package com.github.timepsilon.comet.mobs.boss.demiurge.first_form;


import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.mobs.boss.Boss;
import com.github.timepsilon.comet.mobs.boss.demiurge.final_form.TrueDemiurgeAttack;
import com.github.timepsilon.comet.structure.EnergyPylon;
import com.github.timepsilon.comet.misc.ConfigManager;
import com.github.timepsilon.comet.misc.Laser;
import com.github.timepsilon.comet.misc.TextManager;
import com.github.timepsilon.comet.misc.WeightCollection;
import com.github.timepsilon.comet.mobs.*;
import com.github.timepsilon.comet.mobs.semiboss.DrownedOverlordHorse;
import com.github.timepsilon.comet.mobs.semiboss.IllusionerMage;
import com.github.timepsilon.comet.mobs.semiboss.PhantomFurries;
import com.github.timepsilon.comet.mobs.semiboss.RavagerBeast;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Demiurge extends Boss {

    private final ArmorStand ring1;
    private final ArmorStand ring2;
    private final ArmorStand core;
    private final ArmorStand shell;
    private final ArmorStand wings;
    private int phase;
    private int tick;
    private float speed = 0.1f;
    private boolean isFloating = true;
    private boolean hasSpawnedPylons = false;
    private boolean speedForm = false;
    public int lastLine = 0;

    private static final Location center = ConfigManager.getLoc();
    public static final String NAME = ChatColor.BOLD+""+ChatColor.AQUA+"Demiurge";

    private static final WeightCollection<DemiurgeAttack> ATTACKS = getAttacks();

    private static WeightCollection<DemiurgeAttack> getAttacks() {
        WeightCollection<DemiurgeAttack> rc = new WeightCollection<>();
        Arrays.stream(DemiurgeAttack.values()).forEach(attack -> rc.add(attack.getWeight(),attack));
        return rc;
    }

    private final static Random random = new Random();

    /** 3 phases
     * 1) Le demiurge se téléporte autour de l'arène, lançant divers projectiles et spawnant des mobs + attaques spéciales, -> 100% HP
     * 2) Le demiurge augmente son armure et devient quasi invinsible, il faut alors tuer les boss qui apparaissent et activer les piliers. -> 66% HP
     * 3) Le demiurge se téléporte plus souvent et brusquement, spawn des mini-boss, + d'attaques spéciales -> 33% HP, Le demiurge perd ses anneaux et passe en mode vitesse, les joueurs subissent de l'antigravité et sont éjectés
     */
    public Demiurge(Location loc) {
        super(loc.getWorld().spawnEntity(loc, EntityType.MAGMA_CUBE),2040,NAME, BarColor.GREEN, null,0);

        Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', "&eLe &k&bDemiurge &ea spawn en &a<" + (int) loc.getX() + " , " + (int) loc.getY() + " , " + (int) loc.getZ() + ">")));

        this.entity.setCustomName(this.name);
        this.entity.setAI(true);
        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100000000,0,false,false));
        ((MagmaCube)this.entity).setSize(2);
        this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getMaxHealth());
        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(35);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(10);
        this.entity.setHealth(this.getMaxHealth());

        this.phase=1;

        this.ring1 = modelConstruct(100);
        this.ring2 = modelConstruct(100);
        this.core = modelConstruct(200);
        this.shell = modelConstruct(300);
        this.wings = modelConstruct(400,true);

        TextManager.demiurgeLore(0);

        tick();
    }

    public void setPhase(int phase) {
        this.phase = phase;
        switch (this.phase) {
            case 2:
                if (!this.hasSpawnedPylons) {
                    int i = 0;
                    for (EnergyPylon pylon : EnergyPylon.values()) {
                        pylon.turnOn(i);
                        i+=random.nextInt(3600);
                    }
                    TextManager.sendSamTextToPlayer(ChatColor.GREEN + "La défense du Demiurge grimpe en flèche.");
                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> TextManager.sendSamTextToPlayer(ChatColor.GREEN +
                            "[CONSEIL] : Les pylônes alentours peuvent être chargés en énergie. " +
                            "En activer 4 permettrait d'outrepasser l'armure du Demiurge."), 40);
                    this.hasSpawnedPylons = true;
                }
                break;

            case 3:
                if (!this.speedForm) {
                    this.getEntity().getWorld().spawnParticle(Particle.GLOW_SQUID_INK, this.getEntity().getLocation(), 40, 1, 1, 1, 0, null, true);
                    this.entity.teleport(center.clone().add(0,15,0));
                    this.speed = 0.3f;
                    this.getEntity().getWorld().spawnParticle(Particle.BLOCK_CRACK, this.getEntity().getLocation(), 500, 3, 3, 3, 1, Material.COPPER_BLOCK.createBlockData());
                    this.getEntity().getWorld().playSound(this.getEntity().getLocation(),Sound.BLOCK_ANVIL_PLACE,SoundCategory.HOSTILE,20,0.6f);
                    this.ring1.setMarker(false);
                    this.ring2.setMarker(false);
                    this.ring1.setVelocity(Vector.getRandom().subtract(new Vector(0.5,0,0.5)));
                    this.ring2.setVelocity(Vector.getRandom().subtract(new Vector(0.5,0,0.5)));
                    this.wings.setSmall(true);
                    this.core.setSmall(true);
                    this.shell.setSmall(true);
                    TextManager.sendSamTextToPlayer(ChatColor.GREEN  + "[INFO] Décharge énergétique réussie. La défense du Demiurge redescend à des valeurs normales.");
                    this.speedForm = true;

                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()->{
                        this.ring1.setMarker(true);
                        this.ring2.setMarker(true);
                    },60);
                }
        }
    }

    public int getPhase() {
        return this.phase;
    }

    private void tick() {

        //période d'oscillation/rotation de chaque composante
        float Tcore = 300;
        float Tshell = 700;
        float Tring1 = 900;
        float Tring2 = 1100;
        float Twings = 100;
        this.tick = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),() -> {
            int t = this.entity.getTicksLived();

            if (this.isFloating) {
                Vector move = this.getEntity().getLocation().subtract(center.clone()).toVector().normalize().rotateAroundY(Math.PI/2).multiply(this.speed).setY(0);
                this.getEntity().teleport(this.getEntity().getLocation().add(move));
            }

            //model position
            float yaw = this.getEntity().getLocation().getYaw();
            if (!this.speedForm) {
                this.ring1.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
                this.ring1.setRotation(yaw,0);
                this.ring2.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
                this.ring2.setRotation(yaw,0);

                this.ring1.setHeadPose(new EulerAngle(Math.PI/2,0,t/Tring1*2*Math.PI));
                this.ring2.setHeadPose(new EulerAngle(0,-t/Tring2*2*Math.PI,0));
            }

            this.core.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.core.setRotation(yaw,0);
            this.shell.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.shell.setRotation(yaw,0);
            this.wings.teleport(this.entity.getLocation().clone().add(0,-0.7,0));
            this.wings.setRotation(yaw,0);

            //model rotation
            this.core.setHeadPose(new EulerAngle(0,t/Tcore*2*Math.PI,0));
            this.shell.setHeadPose(new EulerAngle(0,-t/Tshell*2*Math.PI,0));
            double theta = 20*Math.PI/180*Math.cos(t / Twings * 2 * Math.PI);
            this.wings.setRightArmPose(new EulerAngle(theta,0,Math.PI/2));
            this.wings.setLeftArmPose(new EulerAngle(theta,0,-Math.PI/2));

        },0,1).getTaskId();
    }

    @Override
    public void death() {
        super.death();
        this.bossbar.removeAll();
        this.ring1.remove();
        this.ring2.remove();
        this.core.remove();
        this.shell.remove();
        this.wings.remove();
        Bukkit.getScheduler().cancelTask(this.tick);

        this.entity.getWorld().playSound(this.entity.getLocation(),Sound.ENTITY_WITHER_DEATH,SoundCategory.HOSTILE,20,0);
        this.entity.getWorld().spawnParticle(Particle.TOTEM,this.entity.getLocation(),1000,5,5,5,1,null,true);
        this.entity.getWorld().spawnParticle(Particle.GLOW,this.entity.getLocation(),200,0.5,0.5,0.5,0,null,true);

        AtomicInteger i = new AtomicInteger(18);
        int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
            TextManager.demiurgeLore(i.get());
            i.getAndIncrement();
        },0,100).getTaskId();

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> Bukkit.getScheduler().cancelTask(task),600);

        //ProjectHecate.getComet().lastWave();

        //RitualArena.openBarrier();

    }

    private void randomTeleport() {
        if (this.phase == 2) return;
        this.getEntity().getWorld().spawnParticle(Particle.GLOW_SQUID_INK, this.getEntity().getLocation(), 40, 1, 1, 1, 0, null, true);
        this.getEntity().teleport(center.clone().add(random.nextFloat(48) - 24, 14 + random.nextFloat(5), random.nextFloat(48) - 24));
        this.getEntity().getLocation().setYaw(random.nextFloat(360));
        this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 2, 1);
    }

    @Override
    public void attack(List<Player> proxPlayer) {
        if (proxPlayer.isEmpty()) return;
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), ()->{
            if (random.nextInt(2) == 0) randomTeleport();
        },30);
        if (this.phase == 2 && random.nextInt(20)!=0) return;

        switch (ATTACKS.next()) {
            case MOB -> attackMob();
            case LASER -> attackLaser(proxPlayer);
            case WITHERWAVE -> attackWither(proxPlayer);
            case FIREBALL -> attackFireball();
            case VORTEX -> attackVortex(proxPlayer);
            case DASH -> attackDash(proxPlayer);
            case BULLET_HELL -> attackBulletHell(proxPlayer);
            case TELEPORT -> attackTp();
            case METEOR -> attackMeteor(proxPlayer);
        }
    }

    private void attackMob() {
        int soldiers = 0;
        for (Entity e : entity.getNearbyEntities(50,30,50)) {
            if (e.getScoreboardTags().contains("Comet")) soldiers ++;
        }
        //TODO : check if fair
        if (soldiers > Bukkit.getOnlinePlayers().size()*8) {
            attack(proxPlayer);
            return;
        }

        Location loc = this.getEntity().getLocation().add(0,-1,0);
        WeightCollection<String> rc;
        rc = new WeightCollection<String>()
                .add(30, "CreeperBomb")
                .add(5, "EvokerSorcerer")
                .add(35, "SkeletonSniper")
                .add(30, "SpiderCrawler")
                .add(20, "SpiderHerd")
                .add(45, "ZombieTank");
        switch (this.phase) {
            case 1, 2 -> {
                this.getEntity().getWorld().playSound(loc, Sound.BLOCK_LAVA_EXTINGUISH, SoundCategory.HOSTILE, 3, 0);
                this.getEntity().getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc.clone().add(0, -1, 0), 30, 1, 1, 1, 0, null, true);
                for (int i = 0; i < 3; i++) {
                    switch (rc.next()) {
                        case "CreeperBomb" -> new CreeperBomb(loc.clone().add(Vector.getRandom()));
                        case "EvokerSorcerer" -> new EvokerSorcerer(loc.clone().add(Vector.getRandom()));
                        case "SkeletonSniper" -> new SkeletonSniper(loc.clone().add(Vector.getRandom()));
                        case "SpiderCrawler" -> new SpiderCrawler(loc.clone().add(Vector.getRandom()));
                        case "SpiderHerd" -> new SpiderHerd(loc.clone().add(Vector.getRandom()));
                        case "ZombieTank" -> new ZombieTank(loc.clone().add(Vector.getRandom()), 4);
                    }
                }
            }
            case 3 -> {
                WeightCollection<String> rcboss;
                rcboss = new WeightCollection<String>()
                        .add(10, "DrownedOverlord")
                        .add(20, "IllusionerMage")
                        .add(15, "PhantomFurries")
                        .add(20, "RavagerBeast")
                        .add(70,"mobs");
                switch (rcboss.next()) {
                    case "DrownedOverlord" -> new DrownedOverlordHorse(loc.clone().add(Vector.getRandom()));
                    case "IllusionerMage" -> new IllusionerMage(loc.clone().add(Vector.getRandom()));
                    case "PhantomFurries" -> new PhantomFurries(loc.clone().add(Vector.getRandom()));
                    case "RavagerBeast" -> new RavagerBeast(loc.clone().add(Vector.getRandom()));
                    case "mobs" -> {
                        for (int i = 0; i < 5; i++) {
                            switch (rc.next()) {
                                case "CreeperBomb" -> new CreeperBomb(loc.clone().add(Vector.getRandom()));
                                case "EvokerSorcerer" -> new EvokerSorcerer(loc.clone().add(Vector.getRandom()));
                                case "SkeletonSniper" -> new SkeletonSniper(loc.clone().add(Vector.getRandom()));
                                case "SpiderCrawler" -> new SpiderCrawler(loc.clone().add(Vector.getRandom()));
                                case "SpiderHerd" -> new SpiderHerd(loc.clone().add(Vector.getRandom()));
                                case "ZombieTank" -> new ZombieTank(loc.clone().add(Vector.getRandom()), 4);
                            }
                        }
                    }
                }
            }
        }
    }

    private void attackWither(List<Player> proxPlayer) {
        switch (this.phase) {
            case 3:
                for (Player p : proxPlayer) {
                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),() -> {
                        Vector direction = p.getLocation().toVector().subtract(this.getEntity().getLocation().toVector()).clone().normalize().multiply(2);
                        WitherSkull skull = (WitherSkull)this.entity.getWorld().spawnEntity(this.getEntity().getLocation(), EntityType.WITHER_SKULL);
                        skull.setCharged(true);
                        skull.setDirection(direction); 
                        },50);
                }


            case 2:
                ArrayList<Integer> tasks = new ArrayList<>();
                for (int i = 0;i<5;i++) {
                    Vex vex = (Vex)this.entity.getWorld().spawnEntity(this.entity.getLocation(),EntityType.VEX);

                    vex.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,10000000,0,false,false));
                    vex.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(6);
                    vex.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30);
                    vex.setHealth(30);
                    vex.setSilent(true);

                    vex.addScoreboardTag("Comet");

                    final int j = i;
                    tasks.add(
                            Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),() -> {
                                if (vex.isDead()) Bukkit.getScheduler().cancelTask(tasks.get(j));
                                vex.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,vex.getLocation(),
                                        6, 0.1, 0.1,0.1,0);
                            },0,1).getTaskId()
                    );

                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> vex.setHealth(0),200);
                }

            case 1:
                int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()-> {
                    double yaw = random.nextDouble(2*Math.PI);
                    double pitch = -random.nextDouble(Math.PI/2);
                    double coscos = Math.cos(pitch) * Math.cos(yaw);
                    double cossin = -Math.cos(pitch) * Math.sin(yaw);
                    Location loc = entity.getLocation().add(new Vector(cossin, Math.sin(pitch), coscos).multiply(0.2));
                    loc.setYaw((float)yaw);
                    loc.setPitch((float)pitch);
                    WitherSkull skull = (WitherSkull) entity.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
                    skull.setDirection(new Vector(cossin, Math.sin(pitch), coscos).multiply(0.2));
                },0,2).getTaskId();
                Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> Bukkit.getScheduler().cancelTask(task),50);
        }
    }

    private void attackBulletHell(List<Player> proxPlayer) {
        switch (this.phase) {
            case 3:
                List<Integer> meteorTask = new ArrayList<>();
                for (Player p : proxPlayer) {
                    meteorTask.add(Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
                        for (int i = 0; i < 2; i++) {
                            WitherSkull skull = (WitherSkull) p.getWorld().spawnEntity(p.getLocation().add(random.nextFloat(8) - 4, 15, random.nextFloat(8) - 4), EntityType.WITHER_SKULL);
                            p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 1, 1);
                            skull.setVelocity(new Vector(0, -0.5, 0));
                            skull.setDirection(new Vector(0, -0.5, 0));
                            skull.setCharged(true);
                        }
                    }, 0, 20).getTaskId());
                }
                Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> meteorTask.forEach(integer -> Bukkit.getScheduler().cancelTask(integer)), 80);
            case 2:
            case 1:
                List<Integer> bulletTask = new ArrayList<>();
                for (Player p : proxPlayer) {
                    bulletTask.add(Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
                        for (int i = 0; i < 4 * this.phase; i++) {
                            double angle = random.nextDouble(2 * Math.PI);
                            WitherSkull skull = (WitherSkull) p.getWorld().spawnEntity(p.getLocation().add(15 * Math.cos(angle), 0, 15 * Math.sin(angle)), EntityType.WITHER_SKULL);
                            Vector direction = p.getLocation().subtract(skull.getLocation()).toVector().normalize();
                            skull.setVelocity(direction.multiply(0.2));
                            skull.setDirection(direction);
                        }
                    }, 0, 20).getTaskId());
                }
                Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> bulletTask.forEach(integer -> Bukkit.getScheduler().cancelTask(integer)), 80);
        }
    }

    private void attackLaser(List<Player> proxPlayer) {
        for (int i = 0; i < this.phase ; i++) {
            Player target = proxPlayer.get(random.nextInt(proxPlayer.size()));
            target.playSound(this.getEntity().getLocation(),Sound.ENTITY_GUARDIAN_ATTACK,SoundCategory.HOSTILE,4,1);
            Laser laser = null;
            try {
                laser = new Laser.GuardianLaser(this.getEntity().getLocation(),target.getLocation().add(0,-0.5,0),60,64);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            laser.start(ProjectHecate.getPlugin());
            Laser finalLaser = laser;
            int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),() -> {
                finalLaser.moveEnd(target.getLocation().add(0,-0.5,0),4,null);
                finalLaser.moveStart(this.getEntity().getLocation(),1,null);
            },0,4).getTaskId();

            Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> {
                finalLaser.stop();
                Bukkit.getScheduler().cancelTask(task);
                Vector direction = this.getEntity().getLocation().toVector().subtract(finalLaser.getEnd().toVector()).normalize().multiply(-1);
                this.getEntity().getWorld().spawnArrow(this.getEntity().getLocation().add(direction),direction,8,0);
            },60);


        }
    }

    private void attackDash(List<Player> proxPlayer) {
        Player target = proxPlayer.get(random.nextInt(proxPlayer.size()));
        Location start = this.entity.getLocation();
        Location finish = target.getLocation();
        this.isFloating = false;
        target.playSound(this.getEntity().getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 4, 1);
        Vector direction = finish.subtract(start).toVector();
        double length = direction.length();

        this.getEntity().setVelocity(direction.clone().multiply(5/length));

        for (int i = 0; i<length; i+=2) {
            start.getWorld().spawnParticle(Particle.CLOUD,start.clone().add(direction.clone().multiply(i/length)),30,1.5,1.5,1.5,0,null,true);
        }

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),() -> {
            this.isFloating = true;
            for (int i = 0; i<length; i+=2) {
                start.getWorld().createExplosion(start.clone().add(direction.clone().multiply(i/length)),2,false,false,this.entity);
            }
        },30);
    }

    private void attackFireball() {
        switch (this.phase) {
            case 1:
            case 2:
                for (double a = 0; a < 2*Math.PI;a = a + Math.PI/7) {
                    SmallFireball fireball = (SmallFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+random.nextFloat())*2,0,Math.sin(a+random.nextFloat())*2), EntityType.SMALL_FIREBALL);
                    fireball.setVelocity(new Vector(0,-1,0));
                    fireball.setDirection(new Vector(0,-1,0));
                    fireball = (SmallFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+random.nextFloat())*5,0,Math.sin(a+random.nextFloat())*5), EntityType.SMALL_FIREBALL);
                    fireball.setVelocity(new Vector(0,-0.5,0));
                    fireball.setDirection(new Vector(0,-0.5,0));
                }
                break;
            case 3:
                for (double a = 0; a < 2*Math.PI;a = a + Math.PI/10) {
                    LargeFireball fireball = (LargeFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+random.nextFloat())*3,0,Math.sin(a+random.nextFloat())*3), EntityType.FIREBALL);
                    fireball.setVelocity(new Vector(0,-1,0));
                    fireball.setDirection(new Vector(0,-1,0));
                    SmallFireball smallFireball = (SmallFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(a+random.nextFloat())*5,0,Math.sin(a+random.nextFloat())*5), EntityType.SMALL_FIREBALL);
                    smallFireball.setVelocity(new Vector(0,-1,0));
                    smallFireball.setDirection(new Vector(0,-1,0));
                }
                DragonFireball fireball = (DragonFireball) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation().add(Math.cos(random.nextDouble(2*Math.PI))*3,0,Math.sin(random.nextDouble(2*Math.PI))*3), EntityType.DRAGON_FIREBALL);
                fireball.setVelocity(new Vector(0,-2,0));
                fireball.setDirection(new Vector(0,-1,0));
                break;
        }

    }

    private void attackVortex(List<Player> proxPlayer) {
        switch (this.phase) {
            case 1:
                for (Player p : proxPlayer) {
                    Vector toVortex = p.getLocation().toVector().subtract(this.getEntity().getLocation().toVector()).normalize();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,60,1,true,true));
                    p.setVelocity(toVortex.multiply(-0.7));
                }
                break;
            case 2:
                for (Player p : proxPlayer) {
                    Vector toVortex = p.getLocation().toVector().subtract(this.getEntity().getLocation().toVector()).normalize();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,80,2,true,true));
                    p.setVelocity(toVortex.multiply(-1));
                }
                break;
            case 3:
                for (Player p : proxPlayer) {
                    ShulkerBullet bullet = (ShulkerBullet) entity.getWorld().spawnEntity(entity.getLocation().subtract(0,5,0),EntityType.SHULKER_BULLET);
                    bullet.setTarget(p);
                    Vector toVortex = p.getLocation().toVector().subtract(this.getEntity().getLocation().toVector()).normalize();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,100,4,true,true));
                    p.setVelocity(toVortex.multiply(-2));
                }
                break;
        }
    }

    private void attackTp() {
        int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()-> {
            LargeFireball fireball = (LargeFireball) this.getEntity().getWorld().spawnEntity(this.entity.getLocation(),EntityType.FIREBALL);
            fireball.setVelocity(new Vector(0,-1,0));
            fireball.setDirection(new Vector(0,-1,0));
            randomTeleport();
        },0,10).getTaskId();

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> Bukkit.getScheduler().cancelTask(task),10 + 10L *this.phase);
    }

    private void attackMeteor(List<Player> proxPlayer) {
        List<Integer> tasks = new ArrayList<>();
        for (int i = 0; i < phase*2+1; i++) {
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()->{
                double theta = random.nextDouble(2*Math.PI);
                Location loc = proxPlayer.get(random.nextInt(proxPlayer.size())).getLocation();

                LargeFireball fireball = (LargeFireball) entity.getWorld().spawnEntity(loc.clone().add(20*Math.cos(theta),100,20*Math.sin(theta)), EntityType.FIREBALL);
                Vector direction = loc.toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(0.1);
                fireball.setDirection(direction);
                fireball.setVelocity(direction);
                fireball.setVisualFire(true);

                fireball.getWorld().playSound(loc,"minecraft:custom/meteor_fall", SoundCategory.AMBIENT,20,1);


                tasks.add(finalI,Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
                    if(fireball.isDead()) Bukkit.getScheduler().cancelTask(tasks.get(finalI));
                    loc.getWorld().spawnParticle(Particle.GLOW_SQUID_INK,fireball.getLocation(),20,1,2,1,0,null,true);
                },0,1).getTaskId());
            }, 20L *i);
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