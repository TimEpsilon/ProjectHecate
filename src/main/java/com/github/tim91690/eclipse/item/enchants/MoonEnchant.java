package com.github.tim91690.eclipse.item.enchants;

import com.github.tim91690.EventManager;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class MoonEnchant extends Enchantment implements Listener {
    public MoonEnchant() {
        super(new NamespacedKey(EventManager.getPlugin(),"moon_blessing"));
    }

    private HashMap<UUID, Boolean> playerList = new HashMap<>();

    @EventHandler
    public void BlessingDamage(EntityDamageByEntityEvent e) {
        if(!e.getEntity().getScoreboardTags().contains("Eclipse")) return;
        Player p = null;
        if(e.getDamager() instanceof Player) p = (Player)e.getDamager();
        if(e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player) p = (Player)((Projectile)e.getDamager()).getShooter();
        if(p == null) return;

        if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()))) {
            int lvl = p.getInventory().getItemInMainHand().getEnchantments().get(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()));
            switch (lvl) {
                case 1:
                    e.setDamage(e.getDamage()*1.6);
                    break;
                case 2:
                    e.setDamage(e.getDamage()*2.2);
                    break;
                case 3:
                    e.setDamage(e.getDamage()*2.8);
                    break;
                case 4:
                    e.setDamage(e.getDamage()*3.4);
                    break;
                case 5:
                    e.setDamage(e.getDamage()*4);
                    break;
                case 6:
                    e.setDamage(e.getDamage()*4.6);
                    break;
                case 7:
                    e.setDamage(e.getDamage()*5.2);
                    break;
            }
        }
    }

    @EventHandler
    public void LaserSword(PlayerInteractEvent e) {
        if(e.getItem() == null) return;
        if (e.getAction() == Action.LEFT_CLICK_AIR && e.getItem().getType() == Material.NETHERITE_SWORD && e.getItem().getEnchantments().containsKey(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()))) {
            Player p = e.getPlayer();

            int lvl = e.getItem().getEnchantments().get(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()));


            if(lvl<4) return;
            else {
                if (!playerList.containsKey(p.getUniqueId())) playerList.put(p.getUniqueId(),true);
                if (!playerList.get(p.getUniqueId())) return;
                if(lvl>4) {
                    Trident laser = (Trident)p.getLocation().getWorld().spawnEntity(p.getLocation().add(0,1,0).add(p.getLocation().getDirection().multiply(2)), EntityType.TRIDENT);

                    laser.setVelocity(p.getLocation().getDirection().multiply(2));
                    laser.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
                    laser.setShooter(p);

                    int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()->{
                        if (laser.isOnGround() || laser.getVelocity().length() < 1) {
                            laser.setTicksLived(1190);
                            laser.remove();
                        } else {
                            laser.setVelocity(laser.getVelocity().add(new Vector(0,0.05,0)));
                            laser.getWorld().spawnParticle(Particle.REDSTONE,laser.getLocation(),30,0.1,0.1,0.1,0,new Particle.DustOptions(Color.LIME,1),true);
                        }
                    },0,1).getTaskId();

                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                        Bukkit.getScheduler().cancelTask(task);
                        laser.setTicksLived(1190);
                        laser.remove();
                    },100);

                    playerList.replace(p.getUniqueId(),false);
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                        playerList.replace(p.getUniqueId(),true);
                        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,SoundCategory.MASTER,1,2);
                        p.spawnParticle(Particle.REDSTONE,p.getLocation(),30,1,0.5,1,new Particle.DustOptions(Color.LIME,1));
                    },20);
                } else {
                    Arrow laser = (Arrow)p.getLocation().getWorld().spawnEntity(p.getLocation().add(0,1,0).add(p.getLocation().getDirection().multiply(2)), EntityType.ARROW);

                    laser.setVelocity(p.getLocation().getDirection().multiply(1));
                    laser.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
                    laser.setShooter(p);

                    int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()->{
                        if (laser.isOnGround() || laser.getVelocity().length() < 0.1 || laser.isDead() ) {
                            laser.setTicksLived(1190);
                            laser.remove();
                        } else {
                            laser.setVelocity(laser.getVelocity().add(new Vector(0,0.05,0)));
                            laser.getWorld().spawnParticle(Particle.REDSTONE,laser.getLocation(),30,0.1,0.1,0.1,0,new Particle.DustOptions(Color.LIME,1),true);
                        }
                    },0,1).getTaskId();

                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                        Bukkit.getScheduler().cancelTask(task);
                        laser.setTicksLived(1190);
                        laser.remove();
                    },80);

                    playerList.replace(p.getUniqueId(),false);
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                        playerList.replace(p.getUniqueId(),true);
                        p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,SoundCategory.MASTER,1,2);
                        p.spawnParticle(Particle.REDSTONE,p.getLocation(),30,1,0.5,1,new Particle.DustOptions(Color.LIME,1));
                    },50);
                }

            }
        }
    }

    @Override
    public String getName() {
        return "Moon's Blessing";
    }

    @Override
    public int getMaxLevel() {
        return 7;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }
}
