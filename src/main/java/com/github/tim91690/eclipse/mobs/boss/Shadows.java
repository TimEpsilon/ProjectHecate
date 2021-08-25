package com.github.tim91690.eclipse.mobs.boss;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.misc.WeightCollection;
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
import java.util.Random;

public class Shadows extends Boss {
    ArmorStand body;
    int position_task;



    public Shadows(Location loc) {

        super(loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON),350,ChatColor.translateAlternateColorCodes('&',"&8&lShadow"), BarColor.WHITE);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&eUne &0&lShadow &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getY()+" , "+(int)loc.getZ()+">"));

       //shadow appearance
        ItemStack shadow = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta meta = shadow.getItemMeta();
        meta.setCustomModelData(1);
        shadow.setItemMeta(meta);
        this.entity.getEquipment().setHelmet(shadow);
        this.entity.getEquipment().setHelmetDropChance(0f);
        meta.setCustomModelData(3);
        shadow.setItemMeta(meta);
        this.entity.getEquipment().setItemInMainHand(shadow);
        this.entity.getEquipment().setItemInMainHandDropChance(0f);
        this.entity.getEquipment().setItemInOffHand(shadow);
        this.entity.getEquipment().setItemInOffHandDropChance(0f);


        //nom
        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,2000000,0,false,false));
        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,2000000,0,false,false));

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(60);
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(15);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(5);

        loc.add(0,0.2,0);

        this.body = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        this.body.setInvisible(true);
        this.body.setMarker(true);
        this.body.setGravity(false);
        this.body.getEquipment().setHelmet(shadow);
        meta.setCustomModelData(2);
        shadow.setItemMeta(meta);
        this.body.getEquipment().setHelmet(shadow);
        this.body.setArms(true);
        this.body.setRightArmPose(new EulerAngle(0,0,135*Math.PI/180));
        this.body.setLeftArmPose(new EulerAngle(0,0,225*Math.PI/180));

        idle();
    }

    /** Idle animation de la shadow
     * Répète chaque tick
     */
    public void idle() {
        this.position_task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(), () -> {
            this.entity.getWorld().spawnParticle(Particle.REDSTONE,this.getEntity().getLocation(),
                    30, 0.4, 0.5,0.4,0, new Particle.DustOptions(Color.BLACK,1),true);
            this.body.teleport(this.entity.getLocation().add(0,0.2,0));
        },0,1).getTaskId();
    }

    /** Override de la method de base afin d'également supprimer les armorstands du model
     * Annule l'idle animation
     */
    @Override
    public void death() {
        bossList.remove(this);
        this.bossbar.removeAll();
        this.body.remove();
        Bukkit.getScheduler().cancelTask(this.position_task);
    }

    /** 5 attaques différentes
     * possible de les prévoir à l'aide des particules annonçant l'attaque
     */
    @Override
    public void attack(List<Player> proxPlayer) {
        WeightCollection<String> rc;
        rc = new WeightCollection<String>()
                .add(50,"despair")
                .add(40,"teleport")
                .add(35,"wither")
                .add(50,"void")
                .add(45,"soul");
        String attack = rc.next();
        switch (attack) {
            case "despair":
                attackAnimation();
                if (((LivingEntity)this.getEntity()).getHealth() <= this.getMaxHealth()/2) anguish(proxPlayer);
                else despair(proxPlayer);
                break;
            case "teleport":
                attackAnimation();
                if (((LivingEntity)this.getEntity()).getHealth() <= this.getMaxHealth()/2) shadowClone();
                else shadowTeleport();
                break;
            case "wither":
                attackAnimation();
                if (((LivingEntity)this.getEntity()).getHealth() <= this.getMaxHealth()/2) strongWitherWave(proxPlayer);
                else witherWave(proxPlayer);
                break;
            case "soul":
                attackAnimation();
                if (((LivingEntity)this.getEntity()).getHealth() <= this.getMaxHealth()/2) tormentedSoulSummon(proxPlayer);
                else soulSummon(proxPlayer);
                break;
        }
    }

    /** Animation d'attaque : lève les bras en l'air
     */
    private void attackAnimation() {
        ItemStack arm = this.entity.getEquipment().getItemInMainHand();
        this.entity.getEquipment().setItemInMainHand(null);
        this.entity.getEquipment().setItemInOffHand(null);
        this.body.getEquipment().setItemInMainHand(arm);
        this.body.getEquipment().setItemInOffHand(arm);

        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
            this.entity.getEquipment().setItemInMainHand(arm);
            this.entity.getEquipment().setItemInOffHand(arm);
            this.body.getEquipment().setItemInMainHand(null);
            this.body.getEquipment().setItemInOffHand(null);
        },80);
    }

    /** Draine l'énergie mentale du joueur
     */
    private void despair(List<Player> proxPlayer) {
        for (Player p : proxPlayer) {
            if (this.getEntity().getLocation().distance(p.getLocation()) <= 12) {
                this.getEntity().getWorld().spawnParticle(Particle.SPELL_MOB, this.getEntity().getLocation(), 1000, 5, 5, 5, 0);
                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                    //Effect
                    this.getEntity().getWorld().spawnParticle(Particle.SPELL_MOB, p.getLocation(), 400, 0.5, 1, 0.5, 0);
                    p.playSound(this.getEntity().getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 1f, 0.7f);

                    //Potion
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100,0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,2));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,100,0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,100,0));

                    //Random Message
                    WeightCollection<String> rc = (new WeightCollection<String>().add(1,"Tu es à bout de forces...")
                            .add(1,"Abandonnes...")
                            .add(1,"Sacrifies toi au vide...")
                            .add(1,"Jettes ton épée...")
                            .add(1,"La lune demande un sacrifice...")
                            .add(1,"Retires ton armure..."));

                    //Title
                    p.sendTitle(ChatColor.DARK_GRAY+rc.next(),"",5,80,0);

                    //Fake message
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                        p.sendMessage("<"+((Player)Bukkit.getOnlinePlayers().toArray()[(new Random()).nextInt(Bukkit.getOnlinePlayers().size())]).getName()+"> " + rc.next());
                    },60);
                },40);
            }
        }
    }

    /** Version phase 2 de despair
     */
    private void anguish(List<Player> proxPlayer) {
        for (Player p : proxPlayer) {
            if (this.getEntity().getLocation().distance(p.getLocation()) <= 15) {
                //Effect
                this.getEntity().getWorld().spawnParticle(Particle.SPELL_MOB, p.getLocation(), 500, 0.5, 1, 0.5, 0);
                p.playSound(this.getEntity().getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 1f, 0.7f);

                //Potion
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100,0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,100,1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,100,0));

                //Title
                p.sendTitle(ChatColor.DARK_RED+""+ChatColor.MAGIC+"Feed the void","",5,80,0);
                p.sendMessage(ChatColor.ITALIC+""+ChatColor.GRAY+"Vous ressentez une soudaine pulsion de jeter quelque chose...");

                //Fake message
                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                    p.sendMessage("<"+p.getName()+"> Je n'ai pas besoin de ça...");
                    p.dropItem(false);
                },60);
            }
        }
    }

    /** Téléportation
     */
    private void shadowTeleport() {
        //Rend invulnérable le temps de la téléportation
        ((LivingEntity)this.entity).setAI(false);
        ((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,40,100));

        //3 localisations random + localisation actuelle
        List<Location> randomLoc = new ArrayList<>();
        for (int i=0;i<3;i++) {
            Location loc = this.entity.getLocation().add(Math.random()*20-10,Math.random()*6-3,Math.random()*20-10);
            if (loc.getBlock().isEmpty() && loc.add(0,1,0).getBlock().isEmpty()) randomLoc.add(loc);
            else {
                //Evite d'avoir une localisation dans le sol
                for (double y = loc.getY();y<256;y++) {
                    loc.setY(y);
                    if (loc.getBlock().isEmpty() && loc.add(0,1,0).getBlock().isEmpty()) {
                        randomLoc.add(loc);
                        break;
                    }
                }
            }
        }
        randomLoc.add(this.entity.getLocation());
        this.getEntity().getWorld().playSound(this.entity.getLocation(),Sound.ENTITY_ZOMBIE_VILLAGER_CURE,1,0.5f);

        //particules à chaque localisation
        int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
            for (Location loc : randomLoc) {
                this.getEntity().getWorld().spawnParticle(Particle.SPELL_MOB,loc,100,0.2,1,0.2,0);
            }
        },0,1).getTaskId();

        //Téléportation après 2s
        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
            this.getEntity().teleport(randomLoc.get(1));
            this.body.teleport(this.entity.getLocation().add(0,0.2,0));
            this.getEntity().getWorld().playSound(randomLoc.get(1),Sound.ENTITY_ENDERMAN_TELEPORT,1,2);
            ((LivingEntity)this.getEntity()).setAI(true);
            Bukkit.getScheduler().cancelTask(task);
        },40);


    }

    /** Téléportation et clones
     */
    private void shadowClone() {
        //Invulnérable le temps du clonage
        ((LivingEntity)this.entity).setAI(false);
        ((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,40,100));

        //4 Localisations random + localisation actuelle
        List<Location> randomLoc = new ArrayList<>();
        for (int i=0;i<4;i++) {
            Location loc = this.entity.getLocation().add(Math.random()*24-12,Math.random()*6-3,Math.random()*24-12);
            if (loc.getBlock().isEmpty() && loc.add(0,1,0).getBlock().isEmpty()) randomLoc.add(loc);
            else {
                for (double y = loc.getY();y<256;y++) {
                    loc.setY(y);
                    if (loc.getBlock().isEmpty() && loc.add(0,1,0).getBlock().isEmpty()) {
                        randomLoc.add(loc);
                        break;
                    }
                }
            }
        }
        randomLoc.add(this.entity.getLocation());
        this.getEntity().getWorld().playSound(this.entity.getLocation(),Sound.ENTITY_ZOMBIE_VILLAGER_CURE,1,0.3f);

        int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
            for (Location loc : randomLoc) {
                this.getEntity().getWorld().spawnParticle(Particle.SPELL_MOB,loc,100,0.2,1,0.2,0);
                this.getEntity().getWorld().spawnParticle(Particle.SOUL,loc,5,0.8,1,0.8,0.1);
            }
        },0,1).getTaskId();

        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
            this.getEntity().teleport(randomLoc.get(0));
            this.body.teleport(this.entity.getLocation().add(0,0.2,0));
            this.getEntity().getWorld().playSound(randomLoc.get(1),Sound.ENTITY_ENDERMAN_TELEPORT,1,2);
            ((LivingEntity)this.getEntity()).setAI(true);
            Bukkit.getScheduler().cancelTask(task);

            ArrayList<Integer> tasks = new ArrayList<Integer>();
            for (int i = 1;i<5;i++) {
                WitherSkeleton clone = (WitherSkeleton) randomLoc.get(i).getWorld().spawnEntity(randomLoc.get(i),EntityType.WITHER_SKELETON);

                clone.getEquipment().setHelmet(((LivingEntity) this.entity).getEquipment().getHelmet());
                clone.getEquipment().setHelmetDropChance(0);
                clone.getEquipment().setItemInMainHand(null);


                clone.addScoreboardTag("Eclipse");
                clone.addScoreboardTag("SemiBoss");

                clone.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50);
                clone.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
                clone.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);

                clone.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,2000000,0,false,false));

                clone.setHealth(50);

                final int j = i;
                tasks.add(
                        Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
                    if (clone.isDead()) Bukkit.getScheduler().cancelTask(tasks.get(j-1));
                    clone.getWorld().spawnParticle(Particle.REDSTONE,clone.getLocation().add(0,0.8,0),
                            30, 0.2, 0.5,0.2,0, new Particle.DustOptions(Color.BLACK,1),true);
                },0,1).getTaskId()
                );


            }
        },40);
    }

    /** Invoque une witherwave de façon circulaire
     */
    private void witherWave(List<Player> proxPlayer) {
        Boolean player = false;
        for (Player p : proxPlayer) {
            if (this.getEntity().getLocation().distance(p.getLocation()) <= 10) player = true;
        }
        if (player) {
            this.entity.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,this.entity.getLocation(),100,1,0,1);
            Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                for (int i = 0;i<24;i++) {
                    Location loc = this.entity.getLocation().add(new Vector(Math.cos(i * 2 * Math.PI / 20), 0d, Math.sin(i * 2 * Math.PI / 20)).multiply(0.2));
                    loc.setYaw(i*360/20-90);
                    loc.setPitch(0);
                    WitherSkull skull = (WitherSkull)this.entity.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
                    skull.setDirection(new Vector(Math.cos(i*2*Math.PI/23),0d,Math.sin(i*2*Math.PI/23)).multiply(0.1));
                }
            },40);
        }
    }

    /** Invoque 3 witherwaves de façon circulaire
     */
    private void strongWitherWave(List<Player> proxPlayer) {
        Boolean player = false;
        for (Player p : proxPlayer) {
            if (this.getEntity().getLocation().distance(p.getLocation()) <= 15) player = true;
        }
        if (player) {
            this.entity.getWorld().spawnParticle(Particle.SOUL,this.entity.getLocation(),200,2,0,2);

            Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                for (int i = 0;i<24;i++) {
                    Location loc = this.entity.getLocation().add(new Vector(Math.cos(i * 2 * Math.PI / 20), 0d, Math.sin(i * 2 * Math.PI / 20)).multiply(0.2));
                    loc.setYaw(i*360/20-90);
                    loc.setPitch(0);
                    WitherSkull skull = (WitherSkull)this.entity.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
                    skull.setDirection(new Vector(Math.cos(i*2*Math.PI/23),0d,Math.sin(i*2*Math.PI/23)).multiply(0.2));
                    skull.setRotation(i/23,i/23);
                }
            },40);

            Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                for (int i = 0;i<26;i++) {
                    Location loc = this.entity.getLocation().add(new Vector(Math.cos(i * 2 * Math.PI / 20), 0d, Math.sin(i * 2 * Math.PI / 20)).multiply(0.2));
                    loc.setYaw(i*360/20-90);
                    loc.setPitch(0);
                    WitherSkull skull = (WitherSkull)this.entity.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
                    skull.setDirection(new Vector(Math.cos(i * 2 * Math.PI / 26), 0d, Math.sin(i * 2 * Math.PI / 26)));
                    skull.setRotation(i/26,i/26);
                }
            },60);

            Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                for (int i = 0;i<20;i++) {
                    Location loc = this.entity.getLocation().add(new Vector(Math.cos(i * 2 * Math.PI / 20), 0d, Math.sin(i * 2 * Math.PI / 20)).multiply(0.2));
                    loc.setYaw(i*360/20-90);
                    loc.setPitch(0);
                    WitherSkull skull = (WitherSkull)this.entity.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
                    skull.setDirection(new Vector(Math.cos(i * 2 * Math.PI / 20), 0d, Math.sin(i * 2 * Math.PI / 20)).multiply(0.1));
                    skull.setCharged(true);
                }
            },90);
        }
    }

    /** Invoque des âmes
     */
    private void soulSummon(List<Player> proxPlayer) {
        Boolean prox = false;
        for (Player p : proxPlayer) {
            if (this.entity.getLocation().distance(p.getLocation()) <= 15) {
                prox = true;
                break;
            }
        }

        if(prox) {
            this.entity.getWorld().spawnParticle(Particle.VILLAGER_ANGRY,this.entity.getLocation(),200,3,3,3);
            Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                ArrayList<Integer> tasks = new ArrayList<>();
                for (int i = 0;i<4;i++) {
                    Vex vex = (Vex)this.entity.getWorld().spawnEntity(this.entity.getLocation(),EntityType.VEX);

                    vex.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,10000000,0,false,false));
                    vex.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(14);
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

                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                        vex.setHealth(0);
                    },200);
                }
            },60);
        }
    }

    /** Invoque des âmes explosives
     *
     */
    private void tormentedSoulSummon(List<Player> proxPlayer) {
        Boolean prox = false;
        for (Player p : proxPlayer) {
            if (this.entity.getLocation().distance(p.getLocation()) <= 20) {
                prox = true;
                break;
            }
        }

        if(prox) {
            this.entity.getWorld().spawnParticle(Particle.VILLAGER_ANGRY,this.entity.getLocation(),200,3,3,3);
            this.entity.getWorld().spawnParticle(Particle.SOUL,this.entity.getLocation(),50,3,3,3);
            Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                ArrayList<Integer> tasks = new ArrayList<>();
                for (int i = 0;i<5;i++) {
                    Vex vex = (Vex)this.entity.getWorld().spawnEntity(this.entity.getLocation(),EntityType.VEX);

                    vex.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,10000000,0,false,false));
                    vex.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(14);
                    vex.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
                    vex.setHealth(40);
                    vex.setSilent(true);

                    vex.addScoreboardTag("Eclipse");

                    final int j = i;
                    tasks.add(
                            Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
                                if (vex.isDead()) Bukkit.getScheduler().cancelTask(tasks.get(j));
                                vex.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,vex.getLocation(),
                                        10, 0.1, 0.1,0.1,0);
                                vex.getWorld().spawnParticle(Particle.SMOKE_NORMAL,vex.getLocation(),
                                        5, 0.1, 0.1,0.1,0);
                            },0,1).getTaskId()
                    );
                    vex.getWorld().playSound(vex.getLocation(),Sound.ENTITY_TNT_PRIMED,SoundCategory.HOSTILE,1f,0.8f);
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                        vex.getWorld().createExplosion(vex.getLocation(),3,false,false);
                        vex.setHealth(0);
                    },140);
                }
            },60);
        }
    }
}
