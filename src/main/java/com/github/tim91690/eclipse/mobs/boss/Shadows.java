package com.github.tim91690.eclipse.mobs.boss;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public class Shadows extends Boss {
    ArmorStand body;
    ArmorStand arms;

    public Shadows(Location loc) {

        super(loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON),350,ChatColor.translateAlternateColorCodes('&',"&0&lShadow"), BarColor.WHITE);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&eUne &0&lShadow &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getY()+" , "+(int)loc.getZ()+">"));

       //shadow appearance
        ItemStack shadow = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta meta = shadow.getItemMeta();
        meta.setCustomModelData(1);
        shadow.setItemMeta(meta);
        ((WitherSkeleton)this.entity).getEquipment().setItemInMainHand(null);
        ((WitherSkeleton)this.entity).getEquipment().setHelmet(shadow);
        ((WitherSkeleton)this.entity).getEquipment().setHelmetDropChance(0f);

        //nom
        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);

        ((WitherSkeleton) this.entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,2000000,0,false,false));
        ((WitherSkeleton) this.entity).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,2000000,0,false,false));

        ((WitherSkeleton) this.entity).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(60);
        ((WitherSkeleton) this.entity).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
        ((WitherSkeleton) this.entity).getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(15);
        ((WitherSkeleton) this.entity).getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(5);

        loc.add(0,0.2,0);

        this.body = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        this.body.setInvisible(true);
        this.body.setMarker(true);
        this.body.setGravity(false);
        this.body.getEquipment().setHelmet(shadow);
        this.body.getEquipment().getHelmet().getItemMeta().setCustomModelData(2);

        this.arms = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        this.arms.setInvisible(true);
        this.arms.setMarker(true);
        this.arms.setArms(true);
        this.body.setGravity(false);
        this.arms.getEquipment().setItemInMainHand(shadow);
        this.arms.getEquipment().getItemInMainHand().getItemMeta().setCustomModelData(3);
        this.arms.getEquipment().setItemInOffHand(shadow);
        this.arms.getEquipment().getItemInOffHand().getItemMeta().setCustomModelData(3);


    }

    public void idle() {
        this.entity.getWorld().spawnParticle(Particle.REDSTONE,this.getEntity().getLocation().getX(), this.getEntity().getLocation().getY()+1, this.getEntity().getLocation().getZ(),
                10, 0.4, 0.5,0.4,0, new Particle.DustOptions(Color.BLACK,1),true);
        this.body.teleport(this.entity);
        this.arms.teleport(this.entity);
        this.body.setRotation(this.entity.getLocation().getYaw(),this.entity.getLocation().getPitch());
        this.arms.setRotation(this.entity.getLocation().getYaw(),this.entity.getLocation().getPitch());
    }

    @Override
    public void death() {
        bossList.remove(this);
        this.bossbar.removeAll();
        this.body.remove();
        this.arms.remove();
    }

    /** 5 attaques différentes
     * possible de les prévoir à l'aide des particules annonçant l'attaque
     * remaster du boss final de la V1, pas de triple wither parce que c'est pas drôle et devoir frapper un lapin est infiniment plus galère
     * @param p
     */
    @Override
    public void attack(Player p) {
//        WeightCollection<String> rc;
//        rc = new WeightCollection<String>().add(11,"shockwave").add(10,"lightning").add(9,"spores").add(8,"souls").add(1,"wither").add(60,"void");
//        String attack = rc.next();
//        switch (attack) {
//            case "shockwave":
//                this.getEntity().getWorld().spawnParticle(Particle.BLOCK_CRACK, this.getEntity().getLocation(), 400, 8, 2, 8, 0, Material.DIRT.createBlockData());
//                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
//                    shockwave(p);
//                },60);
//                break;
//            case "lightning":
//                this.getEntity().getWorld().spawnParticle(Particle.ELECTRIC_SPARK, p.getLocation(), 50, 1, 1, 1, 0);
//                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
//                    lightning(p);
//                },60);
//                break;
//            case "spores":
//                this.getEntity().getWorld().spawnParticle(Particle.WARPED_SPORE, this.getEntity().getLocation(), 500, 2, 2, 2, 0);
//                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
//                    spores(p);
//                },60);
//                break;
//            case "souls":
//                this.getEntity().getWorld().spawnParticle(Particle.SOUL,this.getEntity().getLocation(),200,2,2,2,0);
//                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
//                    souls(p);
//                },60);
//                break;
//            case "wither":
//                this.getEntity().getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,this.getEntity().getLocation(),200,2,2,2,0);
//                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
//                    wither(p);
//                },60);
//                break;
//        }
    }

    /** Propulse le joueur en l'air
     * @param p
     */
    private void shockwave(Player p) {
        if (this.getEntity().getLocation().distanceSquared(p.getLocation()) <= 64) {
            Vector d = new Vector(0, 2, 0);
            p.setVelocity(d);
            this.getEntity().getWorld().spawnParticle(Particle.BLOCK_CRACK, this.getEntity().getLocation(), 300, 8, 2, 8, 0, Material.DIRT.createBlockData());
            this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 2f, 0.8f);
        }
    }

    /** Envoi un éclair sur le joueur
     * @param p
     */
    private void lightning(Player p) {
        if (this.getEntity().getLocation().distanceSquared(p.getLocation()) <= 100) {
            Location loc = p.getLocation();
            loc.add(new Vector(Math.random()*4-2,0,Math.random()*4-2));
            p.getWorld().strikeLightning(loc);
        }
    }

    /** Inflige wither 3 pour 3s
     * @param p
     */
    private void spores(Player p) {
        if (this.getEntity().getLocation().distanceSquared(p.getLocation()) <= 144) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 2));
            this.getEntity().getWorld().spawnParticle(Particle.WARPED_SPORE, this.getEntity().getLocation(), 1500, 5, 5, 5, 10);
            this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE, 3f, 0f);
        }
    }

    /** Invoque 2 wither squelettes par joueurs
     * @param p
     */
    private void souls(Player p) {
        if (this.getEntity().getLocation().distanceSquared(p.getLocation()) <= 144) {
            for (int i = 0; i < 2; i++) {
                WitherSkeleton s = (WitherSkeleton) this.getEntity().getLocation().getWorld().spawnEntity(this.getEntity().getLocation(),EntityType.WITHER_SKELETON);

                s.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(80);
                s.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);

                s.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_AXE));
                s.getEquipment().setItemInMainHandDropChance(0.05f);
                s.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
                s.getEquipment().setChestplateDropChance(0.05f);
                s.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
                s.getEquipment().setLeggingsDropChance(0.05f);

                s.setHealth(80);

                s.addScoreboardTag("Eclipse");

                this.getEntity().getWorld().spawnParticle(Particle.SOUL,this.getEntity().getLocation(),500,10,10,10,0);
                this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ENTITY_WITHER_HURT,3f,0f);
            }
        }
    }

    /** Invoque un wither (semiboss)
     * @param p
     */
    private void wither(Player p) {
        if (this.getEntity().getLocation().distanceSquared(p.getLocation()) <= 225) {
            Wither s = (Wither) this.getEntity().getLocation().getWorld().spawnEntity(this.getEntity().getLocation(),EntityType.WITHER);

            s.setCustomName(ChatColor.DARK_RED + "Wrath Spirit");
            s.setCustomNameVisible(true);

            s.addScoreboardTag("Eclipse");
            s.addScoreboardTag("SemiBoss");

            Team scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");
            scarlet.addEntry(s.getUniqueId().toString());

            this.getEntity().getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,this.getEntity().getLocation(),500,5,5,5,0);
        }
    }

}
