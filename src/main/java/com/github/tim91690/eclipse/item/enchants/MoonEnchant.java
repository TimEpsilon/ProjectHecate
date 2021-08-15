package com.github.tim91690.eclipse.item.enchants;

import com.github.tim91690.EventManager;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
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
        if(!e.getEntity().getScoreboardTags().contains("Eclipse") || !(e.getDamager() instanceof Player)) return;
        if (((Player)e.getDamager()).getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()))) {
            int lvl = ((Player)e.getDamager()).getInventory().getItemInMainHand().getEnchantments().get(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()));
            switch (lvl) {
                case 1:
                    e.setDamage(e.getDamage()*1.6);
            }
        }
    }

    @EventHandler
    public void LaserSword(PlayerInteractEvent e) {
        if(e.getItem() == null) return;
        if (e.getAction() == Action.LEFT_CLICK_AIR && e.getItem().getType() == Material.NETHERITE_SWORD && e.getItem().getEnchantments().containsKey(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()))) {
            Player p = e.getPlayer();
            int lvl = e.getItem().getEnchantments().get(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()));

            if (!playerList.containsKey(p.getUniqueId())) playerList.put(p.getUniqueId(),true);

            if (playerList.get(p.getUniqueId())) {
                Trident trident = (Trident)p.getLocation().getWorld().spawnEntity(p.getEyeLocation().add(p.getLocation().getDirection().multiply(2)), EntityType.TRIDENT);
                trident.setVelocity(p.getLocation().getDirection().multiply(2));
                trident.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);

                int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()->{
                    if (trident.isOnGround() || trident.getVelocity().length() < 1) {
                        trident.setTicksLived(1190);
                        trident.remove();
                    } else {
                        trident.setVelocity(trident.getVelocity().add(new Vector(0,0.05,0)));
                        Vector offset = trident.getVelocity().normalize().multiply(0.7);
                        trident.getWorld().spawnParticle(Particle.REDSTONE,trident.getLocation(),30,offset.getX(),offset.getY(),offset.getZ(),0,new Particle.DustOptions(Color.LIME,1),true);
                    }
                },0,1).getTaskId();

                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                    Bukkit.getScheduler().cancelTask(task);
                    trident.setTicksLived(1190);
                    trident.remove();
                },100);

                playerList.replace(p.getUniqueId(),false);
                Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                    playerList.replace(p.getUniqueId(),true);
                },20);

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
