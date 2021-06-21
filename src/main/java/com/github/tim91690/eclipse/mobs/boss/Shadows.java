package com.github.tim91690.eclipse.mobs.boss;

import com.github.tim91690.EventManager;
import com.github.tim91690.misc.WeightCollection;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public class Shadows extends Boss {

    public Shadows(Location loc) {

        super(loc.getWorld().spawnEntity(loc, EntityType.PLAYER),350,ChatColor.translateAlternateColorCodes('&',"&0&lShadow"), BarColor.WHITE);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&eUne &0&lShadow &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getY()+" , "+(int)loc.getZ()+">"));

       /*//killer bunny
        ((Rabbit) this.entity).setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);

        //nom
        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);
        this.entity.setGlowing(true);

        ((Rabbit) this.entity).addPotionEffect(new PotionEffect(PotionEffectType.JUMP,2000000,2));
        ((Rabbit) this.entity).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,2000000,0));

        ((Rabbit) this.entity).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(60);
        ((Rabbit) this.entity).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
        ((Rabbit) this.entity).getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(14);
        ((Rabbit) this.entity).getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(4);*/
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
