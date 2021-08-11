package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.moonblade.enchants.EnchantRegister;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;


public class LaserSword implements Listener {
    private HashMap<UUID, Boolean> playerList = new HashMap<>();

    @EventHandler
    public void LaserSword(PlayerInteractEvent e) {
        if(e.getItem() == null) return;
        if (e.getAction() == Action.LEFT_CLICK_AIR && e.getItem().getType() == Material.NETHERITE_SWORD) {
            Player p = e.getPlayer();

             p.sendMessage(""+p.getEquipment().getItemInMainHand().getEnchantments().containsKey(EnchantRegister.MOON_BLESSING));

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
}
