package com.github.tim91690.eclipse.item.enchants;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.CustomItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class MoonEnchant extends Enchantment implements Listener {
    public MoonEnchant() {
        super(new NamespacedKey(EventManager.getPlugin(),"moon_blessing"));
    }

    private HashMap<UUID, Boolean> playerList = new HashMap<>();
    private HashMap<UUID,Integer> sinId = new HashMap<>();
    private HashMap<UUID, HashMap<Integer, Long>> sinCooldown = new HashMap<>();

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
                    e.setDamage(e.getDamage()*1.5);
                    break;
                case 2:
                    e.setDamage(e.getDamage()*2);
                    ((ExperienceOrb)p.getWorld().spawnEntity(e.getEntity().getLocation(),EntityType.EXPERIENCE_ORB)).setExperience(1);
                    break;
                case 3:
                    e.setDamage(e.getDamage()*2.5);
                    ((ExperienceOrb)p.getWorld().spawnEntity(e.getEntity().getLocation(),EntityType.EXPERIENCE_ORB)).setExperience(2);
                    if ((int)(Math.random()*50)==0) p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.mcoin(1));
                    break;
                case 4:
                    e.setDamage(e.getDamage()*3);
                    for (int i =0; i<2;i++) {
                        ((ExperienceOrb)p.getWorld().spawnEntity(e.getEntity().getLocation(),EntityType.EXPERIENCE_ORB)).setExperience(2);
                        if ((int)(Math.random()*48)==0) p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.mcoin(1));
                    }
                    break;
                case 5:
                    e.setDamage(e.getDamage()*3.5);
                    for (int i =0; i<2;i++) {
                        ((ExperienceOrb)p.getWorld().spawnEntity(e.getEntity().getLocation(),EntityType.EXPERIENCE_ORB)).setExperience((int)(Math.random()*5));
                        if ((int)(Math.random()*44)==0) p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.mcoin(1));
                    }
                    break;
                case 6:
                    e.setDamage(e.getDamage()*4);
                    for (int i =0; i<3;i++) {
                        ((ExperienceOrb)p.getWorld().spawnEntity(e.getEntity().getLocation(),EntityType.EXPERIENCE_ORB)).setExperience((int)(Math.random()*5));
                        if ((int)(Math.random()*40)==0) p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.mcoin(1));
                    }
                    break;
                case 7:
                    e.setDamage(e.getDamage()*4.5);
                    for (int i =0; i<4;i++) {
                        ((ExperienceOrb)p.getWorld().spawnEntity(e.getEntity().getLocation(),EntityType.EXPERIENCE_ORB)).setExperience((int)(Math.random()*6));
                        if ((int)(Math.random()*36)==0) p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.mcoin(1));
                    }
                    passiveSin(p,(LivingEntity)e.getEntity());
                    break;
            }
        }
    }

    @EventHandler
    public void LaserSword(PlayerInteractEvent e) {
        if(e.getItem() == null) return;
        if (e.getAction() == Action.LEFT_CLICK_AIR && e.getItem().getEnchantments().containsKey(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()))) {
            Player p = e.getPlayer();

            int lvl = e.getItem().getEnchantments().get(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()));
            if (!playerList.containsKey(p.getUniqueId())) playerList.put(p.getUniqueId(),true);
            if (!playerList.get(p.getUniqueId())) return;

            switch (lvl) {
                case 4:
                    moonArrow(p);
                    break;
                case 5:
                    moonTrident(p);
                    break;
                case 6:
                    playerList.replace(p.getUniqueId(),false);
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                        playerList.replace(p.getUniqueId(),true);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,SoundCategory.MASTER,1,2);
                        p.spawnParticle(Particle.REDSTONE,p.getLocation(),30,1,0.5,1,new Particle.DustOptions(Color.LIME,1));
                    },30);
                    new MoonLaser(p.getLocation().add(0,1,0).add(p.getLocation().getDirection().multiply(2)),Color.LIME,p,0);
                    break;
                case 7:
                    if (!p.isSneaking()) return;
                    playerList.replace(p.getUniqueId(),false);
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
                        playerList.replace(p.getUniqueId(),true);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,SoundCategory.MASTER,1,2);
                        p.spawnParticle(Particle.REDSTONE,p.getLocation(),30,1,0.5,1,new Particle.DustOptions(Color.LIME,1));
                    },150);
                    new MoonLaser(p.getTargetBlock(null,8).getLocation().add(Math.random()*16-8,Math.random()*5,Math.random()*16-8),Color.YELLOW,p,20);
                    new MoonLaser(p.getTargetBlock(null,8).getLocation().add(Math.random()*16-8,Math.random()*5,Math.random()*16-8),Color.GREEN,p,25);
                    new MoonLaser(p.getTargetBlock(null,8).getLocation().add(Math.random()*16-8,Math.random()*5,Math.random()*16-8),Color.TEAL,p,30);
                    new MoonLaser(p.getTargetBlock(null,8).getLocation().add(Math.random()*16-8,Math.random()*5,Math.random()*16-8),Color.BLUE,p,35);
                    new MoonLaser(p.getTargetBlock(null,8).getLocation().add(Math.random()*16-8,Math.random()*5,Math.random()*16-8),Color.PURPLE,p,40);
                    new MoonLaser(p.getTargetBlock(null,8).getLocation().add(Math.random()*16-8,Math.random()*5,Math.random()*16-8),Color.RED,p,45);
                    new MoonLaser(p.getTargetBlock(null,8).getLocation().add(Math.random()*16-8,Math.random()*5,Math.random()*16-8),Color.ORANGE,p,50);
                    break;
            }
        }
    }

    @EventHandler
    public void SinsOfTheMoon(PlayerInteractEvent e) {
        if (e.getPlayer() == null || e.getItem() == null) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if (!e.getItem().getEnchantments().containsKey(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey()))) return;
            if (e.getItem().getEnchantments().get(Enchantment.getByKey(EnchantRegister.MOON_BLESSING.getKey())) != 7) return;
            if (!sinId.containsKey(p.getUniqueId())) sinId.put(p.getUniqueId(),0);
            if (!sinCooldown.containsKey(p.getUniqueId())) sinCooldown.put(p.getUniqueId(),getDefaultSins());

            if (p.isSneaking()) switchSin(p);
            else activateSin(p);
        }
    }

    private void switchSin(Player p) {
        int id = sinId.get(p.getUniqueId());
        id = (id + 1)%7;
        switch (id) {
            case 0:
                //Sloth
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "" + ChatColor.BOLD + "Sloth"));
                break;
            case 1:
                //Greed
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Greed"));
                break;
            case 2:
                //Lust
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Lust"));
                break;
            case 3:
                //Wrath
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Wrath"));
                break;
            case 4:
                //Gluttony
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.BLUE + "" + ChatColor.BOLD + "Gluttony"));
                break;
            case 5:
                //Pride
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "" + ChatColor.BOLD + "Pride"));
                break;
            case 6:
                //Envy
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "" + ChatColor.BOLD + "Envy"));
                break;
        }
        sinId.replace(p.getUniqueId(),id);
        p.playSound(p.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,SoundCategory.MASTER,1,1);
    }

    private void activateSin(Player p) {
        int id =sinId.get(p.getUniqueId());
        int timer =0;
        Color sin = Color.WHITE;

        if (sinCooldown.get(p.getUniqueId()).get(id) > System.currentTimeMillis()) {
            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,SoundCategory.MASTER,1,1);
            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Cooldown : " + ChatColor.GOLD + (int)((sinCooldown.get(p.getUniqueId()).get(id) - System.currentTimeMillis())/1000) + "s");
            return;
        }
        p.playSound(p.getLocation(),Sound.BLOCK_ENCHANTMENT_TABLE_USE,SoundCategory.MASTER,1,1);
        switch (id) {
            case 0:
                //Sloth
                p.getWorld().spawnParticle(Particle.SLIME,p.getLocation(),300,8,8,8);
                for (Entity e : p.getNearbyEntities(8,8,8)) {
                    if (e instanceof LivingEntity && e.getScoreboardTags().contains("Eclipse")) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5, false, true));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2, false, true));
                    }
                }
                timer = 300;
                sin = Color.YELLOW;
                break;

            case 1:
                //Greed
                p.getWorld().spawnParticle(Particle.TOTEM, p.getLocation(), 200, 5, 5, 5);
                for (Entity e : p.getNearbyEntities(5, 5, 5)) {
                    if (e instanceof LivingEntity && e.getScoreboardTags().contains("Eclipse")) {
                        ExperienceOrb xp = (ExperienceOrb) e.getWorld().spawnEntity(e.getLocation(), EntityType.EXPERIENCE_ORB);
                        for (int i = 0; i<5;i++) {
                            xp.setExperience(4);
                            xp.setVelocity(Vector.getRandom());
                        }
                    }
                }
                timer = 1000;
                sin = Color.GREEN;
                break;

            case 2:
                //Lust
                p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 50, 1, 1, 1);
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,2000,2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,300,1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,100,2));
                timer = 2200;
                sin = Color.PURPLE;
                break;

            case 3:
                //Wrath
                p.getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getLocation(), 150, 1, 1, 1,0,Material.REDSTONE_BLOCK.createBlockData());
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,300,1));
                timer = 1200;
                sin = Color.RED;
                break;

            case 4:
                //Gluttony
                p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getLocation(), 250, 3, 3, 3,0);
                int abslvl =0;
                for (Entity e : p.getNearbyEntities(4,4,4)) {
                    if (!e.getScoreboardTags().contains("Eclipse")) continue;
                    ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.WITHER,50,2));
                    abslvl += 1;
                }
                p.setAbsorptionAmount(abslvl);
                timer = 900;
                sin = Color.AQUA;
                break;

            case 5:
                //Pride
                p.getWorld().spawnParticle(Particle.SOUL, p.getLocation(), 150, 2, 2, 2,0);
                p.setVelocity(p.getLocation().getDirection().normalize().multiply(3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,200,1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,200,1));
                timer = 250;
                sin = Color.ORANGE;
                break;

            case 6:
                //Envy
                p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, p.getLocation(), 250, 4, 4, 4,0);
                for (Entity e : p.getNearbyEntities(8,8,8)) {
                    if (!e.getScoreboardTags().contains("Eclipse")) continue;
                    e.getWorld().strikeLightning(e.getLocation());
                }
                timer = 800;
                sin = Color.GREEN;
                break;
        }
        //Timer
        HashMap<Integer,Long> newSinCooldown = sinCooldown.get(p.getUniqueId());
        newSinCooldown.replace(id,System.currentTimeMillis()+timer*50);
        sinCooldown.replace(p.getUniqueId(), newSinCooldown);

        final Color color = sin;
        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME,SoundCategory.MASTER,1,1);
            p.spawnParticle(Particle.REDSTONE,p.getLocation(),50,1,0.5,1,new Particle.DustOptions(color,1));
        },timer);
    }

    private void passiveSin(Player p, LivingEntity e) {
        int id =sinId.get(p.getUniqueId());

        switch (id) {
            case 0:
                //Sloth
                e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,50,0));
                e.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,50,0));
                break;
            case 1:
                //Greed
                if ((int)(Math.random()*12)==0) {
                    e.getWorld().dropItem(e.getLocation(), CustomItems.mcoin(1));
                    p.getWorld().spawnParticle(Particle.TOTEM,e.getLocation(),60,1,1.5,1);
                    p.playSound(p.getLocation(),Sound.ITEM_TOTEM_USE,SoundCategory.MASTER,1,2);
                }
                break;
            case 2:
                //Lust
                if ((int)(Math.random()*7)==0) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,1,0));
                    p.getWorld().spawnParticle(Particle.HEART,p.getLocation(),60,1,1.5,1);
                    p.playSound(p.getLocation(),Sound.BLOCK_AMETHYST_BLOCK_BREAK,SoundCategory.MASTER,1,0.5f);
                }
                break;
            case 3:
                //Wrath
                if ((int)(Math.random()*12)==0) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,40,0));
                    p.getWorld().spawnParticle(Particle.BLOCK_CRACK,p.getLocation(),60,1,1.5,1,Material.REDSTONE_BLOCK.createBlockData());
                    p.playSound(p.getLocation(),Sound.BLOCK_STONE_BREAK,SoundCategory.MASTER,1,1);
                }
                break;
            case 4:
                //Gluttony
                if ((int)(Math.random()*7)==0) {
                    e.damage(8);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,1,0));
                    double length = e.getLocation().add(p.getLocation()).length();
                    Vector direction = p.getEyeLocation().toVector().add(e.getEyeLocation().toVector().multiply(-1)).multiply(1d/length);
                    Location loc = e.getLocation().add(0,0.5,0).clone();
                    for (int i=0; i<e.getLocation().add(p.getLocation()).length();i++) {
                        p.getWorld().spawnParticle(Particle.DRAGON_BREATH,loc,5,0.2,0.2,0.2,0);
                        loc.add(direction);
                    }
                }
                break;
            case 5:
                //Pride
                if ((int)(Math.random()*7)==0) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,60,0));
                    p.getWorld().spawnParticle(Particle.SOUL,p.getLocation(),60,1,1.5,1);
                    p.playSound(p.getLocation(),Sound.ENTITY_WITHER_SHOOT,SoundCategory.MASTER,1,1);
                }
                break;

            case 6:
                //Envy
                if ((int)(Math.random()*12)==0) {
                    e.getWorld().strikeLightning(e.getLocation());
                    p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,e.getLocation(),60,1,1.5,1);
                }
                break;
        }
    }

    private HashMap<Integer, Long> getDefaultSins() {
        HashMap<Integer, Long> cooldown = new HashMap<>();
        long time = System.currentTimeMillis();
        for (int i =0; i<7;i++) {
            cooldown.put(i,time);
        }
        return cooldown;
    }

    private void moonArrow(Player p) {
        Arrow laser = (Arrow)p.getLocation().getWorld().spawnEntity(p.getLocation().add(0,1,0).add(p.getLocation().getDirection().multiply(2)), EntityType.ARROW);

        laser.setVelocity(p.getLocation().getDirection());
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

    private void moonTrident(Player p) {
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
        return true;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }
}
