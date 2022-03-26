package com.github.tim91690.eclipse.item.enchants;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.CustomItems;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CosmicEnchant extends Enchantment implements Listener {
    public CosmicEnchant() {
        super(new NamespacedKey(EventManager.getPlugin(),"cosmic_blessing"));
    }

    private final HashMap<UUID, Boolean> playerList = new HashMap<>();
    private final HashMap<UUID,Integer> sinId = new HashMap<>();
    private final HashMap<UUID, HashMap<Integer, Long>> sinCooldown = new HashMap<>();
    private final static Random random = new Random();

    @EventHandler
    public void BlessingDamage(EntityDamageByEntityEvent e) {
        if (!EventManager.isRunningEvent) return;
        if(!e.getEntity().getScoreboardTags().contains("Eclipse")) return;
        if(e.getEntity().getScoreboardTags().contains("Boss")) return;
        Player p = null;
        if(e.getDamager() instanceof Player) p = (Player)e.getDamager();
        if(e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player) p = (Player)((Projectile)e.getDamager()).getShooter();
        if(p == null) return;

        if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.getByKey(EnchantRegister.COSMIC_BLESSING.getKey()))) {
            int lvl = p.getInventory().getItemInMainHand().getEnchantments().get(Enchantment.getByKey(EnchantRegister.COSMIC_BLESSING.getKey()));
            switch (lvl) {
                case 1 -> e.setDamage(e.getDamage() * 1.5);
                case 2 -> {
                    e.setDamage(e.getDamage() * 2);
                    ((ExperienceOrb) p.getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.EXPERIENCE_ORB)).setExperience(1);
                }
                case 3 -> {
                    e.setDamage(e.getDamage() * 2.5);
                    ((ExperienceOrb) p.getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.EXPERIENCE_ORB)).setExperience(3);
                    if (random.nextInt(50) == 0)
                        p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.MCOIN.getItem());
                }
                case 4 -> {
                    e.setDamage(e.getDamage() * 3);
                    for (int i = 0; i < 2; i++) {
                        ((ExperienceOrb) p.getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.EXPERIENCE_ORB)).setExperience(3);
                        if (random.nextInt(40) == 0)
                            p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.MCOIN.getItem());
                    }
                }
                case 5 -> {
                    e.setDamage(e.getDamage() * 3.5);
                    for (int i = 0; i < 2; i++) {
                        ((ExperienceOrb) p.getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.EXPERIENCE_ORB)).setExperience(random.nextInt(5)+1);
                        if (random.nextInt(30) == 0)
                            p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.MCOIN.getItem());
                    }
                }
                case 6 -> {
                    e.setDamage(e.getDamage() * 4);
                    for (int i = 0; i < 3; i++) {
                        ((ExperienceOrb) p.getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.EXPERIENCE_ORB)).setExperience(random.nextInt(5)+1);
                        if (random.nextInt(30) == 0)
                            p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.MCOIN.getItem());
                    }
                }
                case 7 -> {
                    e.setDamage(e.getDamage() * 4.5);
                    for (int i = 0; i < 4; i++) {
                        ((ExperienceOrb) p.getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.EXPERIENCE_ORB)).setExperience(random.nextInt(7)+1);
                        if (random.nextInt(25) == 0)
                            p.getWorld().dropItem(e.getEntity().getLocation(), CustomItems.MCOIN.getItem());
                    }
                    passiveSin(p, (LivingEntity) e.getEntity());
                }
            }
        }
    }

    @EventHandler
    public void LaserSword(PlayerInteractEvent e) {
        if (!EventManager.isRunningEvent) return;
        if(e.getItem() == null) return;
        if (e.getAction() == Action.LEFT_CLICK_AIR && e.getItem().getEnchantments().containsKey(Enchantment.getByKey(EnchantRegister.COSMIC_BLESSING.getKey()))) {
            Player p = e.getPlayer();

            int lvl = e.getItem().getEnchantments().get(Enchantment.getByKey(EnchantRegister.COSMIC_BLESSING.getKey()));
            if (!playerList.containsKey(p.getUniqueId())) playerList.put(p.getUniqueId(),true);
            if (!playerList.get(p.getUniqueId())) return;

            switch (lvl) {
                case 4 -> cosmicArrow(p);
                case 5 -> cosmicTrident(p);
                case 6 -> {
                    playerList.replace(p.getUniqueId(), false);
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                        playerList.replace(p.getUniqueId(), true);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, SoundCategory.PLAYERS, 1, 2);
                        p.spawnParticle(Particle.REDSTONE, p.getLocation(), 30, 1, 0.5, 1, new Particle.DustOptions(Color.LIME, 1));
                    }, 30);
                    new CosmicLaser(p.getLocation().add(0, 1, 0).add(p.getLocation().getDirection().multiply(2)), Color.LIME, p, 0);
                }
                case 7 -> {
                    if (!p.isSneaking()) return;
                    playerList.replace(p.getUniqueId(), false);

                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                        playerList.replace(p.getUniqueId(), true);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, SoundCategory.PLAYERS, 1, 2);
                        p.spawnParticle(Particle.REDSTONE, p.getLocation(), 30, 1, 0.5, 1, new Particle.DustOptions(Color.LIME, 1));
                    }, 150);

                    new CosmicLaser(p.getTargetBlock(null, 8).getLocation().add(Math.random() * 16 - 8, Math.random() * 5, Math.random() * 16 - 8), Color.YELLOW, p, 20);
                    new CosmicLaser(p.getTargetBlock(null, 8).getLocation().add(Math.random() * 16 - 8, Math.random() * 5, Math.random() * 16 - 8), Color.GREEN, p, 25);
                    new CosmicLaser(p.getTargetBlock(null, 8).getLocation().add(Math.random() * 16 - 8, Math.random() * 5, Math.random() * 16 - 8), Color.TEAL, p, 30);
                    new CosmicLaser(p.getTargetBlock(null, 8).getLocation().add(Math.random() * 16 - 8, Math.random() * 5, Math.random() * 16 - 8), Color.BLUE, p, 35);
                    new CosmicLaser(p.getTargetBlock(null, 8).getLocation().add(Math.random() * 16 - 8, Math.random() * 5, Math.random() * 16 - 8), Color.PURPLE, p, 40);
                    new CosmicLaser(p.getTargetBlock(null, 8).getLocation().add(Math.random() * 16 - 8, Math.random() * 5, Math.random() * 16 - 8), Color.RED, p, 45);
                    new CosmicLaser(p.getTargetBlock(null, 8).getLocation().add(Math.random() * 16 - 8, Math.random() * 5, Math.random() * 16 - 8), Color.ORANGE, p, 50);
                }
            }
        }
    }

    @EventHandler
    public void SinsOfTheCosmos(PlayerInteractEvent e) {
        if (!EventManager.isRunningEvent) return;
        if (e.getItem() == null) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if (!e.getItem().getEnchantments().containsKey(Enchantment.getByKey(EnchantRegister.COSMIC_BLESSING.getKey()))) return;
            if (e.getItem().getEnchantments().get(Enchantment.getByKey(EnchantRegister.COSMIC_BLESSING.getKey())) != 7) return;
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
            case 0 ->
                    //Sloth
                    p.sendActionBar(Component.text(ChatColor.YELLOW + "" + ChatColor.BOLD + "Sloth"));
            case 1 ->
                    //Greed
                    p.sendActionBar(Component.text(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Greed"));
            case 2 ->
                    //Lust
                    p.sendActionBar(Component.text(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Lust"));
            case 3 ->
                    //Wrath
                    p.sendActionBar(Component.text(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Wrath"));
            case 4 ->
                    //Gluttony
                    p.sendActionBar(Component.text(ChatColor.BLUE + "" + ChatColor.BOLD + "Gluttony"));
            case 5 ->
                    //Pride
                    p.sendActionBar(Component.text(ChatColor.GOLD + "" + ChatColor.BOLD + "Pride"));
            case 6 ->
                    //Envy
                    p.sendActionBar(Component.text(ChatColor.GREEN + "" + ChatColor.BOLD + "Envy"));
        }
        sinId.replace(p.getUniqueId(),id);
        p.playSound(p.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,SoundCategory.PLAYERS,1,1);
    }

    private void activateSin(Player p) {
        int id = sinId.get(p.getUniqueId());
        int timer = 0;
        Color sin = Color.WHITE;

        if (sinCooldown.get(p.getUniqueId()).get(id) > System.currentTimeMillis()) {
            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,SoundCategory.PLAYERS,1,1);
            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Cooldown : " + ChatColor.GOLD + (int)((sinCooldown.get(p.getUniqueId()).get(id) - System.currentTimeMillis())/1000) + "s");
            return;
        }
        p.playSound(p.getLocation(),Sound.BLOCK_ENCHANTMENT_TABLE_USE,SoundCategory.PLAYERS,1,1);
        switch (id) {
            case 0 -> {
                //Sloth
                p.getWorld().spawnParticle(Particle.SLIME, p.getLocation(), 300, 8, 8, 8);
                for (Entity e : p.getNearbyEntities(8, 8, 8)) {
                    if (e instanceof LivingEntity && e.getScoreboardTags().contains("Eclipse")) {
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5, false, true));
                        ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2, false, true));
                    }
                }
                timer = 300;
                sin = Color.YELLOW;
            }
            case 1 -> {
                //Greed
                p.getWorld().spawnParticle(Particle.TOTEM, p.getLocation(), 200, 5, 5, 5);
                for (Entity e : p.getNearbyEntities(5, 5, 5)) {
                    if (e instanceof LivingEntity && e.getScoreboardTags().contains("Eclipse")) {
                        ExperienceOrb xp = (ExperienceOrb) e.getWorld().spawnEntity(e.getLocation(), EntityType.EXPERIENCE_ORB);
                        for (int i = 0; i < 5; i++) {
                            xp.setExperience(8);
                            xp.setVelocity(Vector.getRandom());
                        }
                    }
                }
                timer = 1000;
                sin = Color.GREEN;
            }
            case 2 -> {
                //Lust
                p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 50, 1, 1, 1);
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2000, 2));
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100, 2));
                timer = 2200;
                sin = Color.PURPLE;
            }
            case 3 -> {
                //Wrath
                p.getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getLocation(), 150, 1, 1, 1, 0, Material.REDSTONE_BLOCK.createBlockData());
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1));
                timer = 1200;
                sin = Color.RED;
            }
            case 4 -> {
                //Gluttony
                p.getWorld().spawnParticle(Particle.DRAGON_BREATH, p.getLocation(), 250, 3, 3, 3, 0);
                int abslvl = 0;
                for (Entity e : p.getNearbyEntities(4, 4, 4)) {
                    if (!e.getScoreboardTags().contains("Eclipse")) continue;
                    ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 50, 2));
                    abslvl += 1;
                }
                p.setAbsorptionAmount(abslvl);
                timer = 900;
                sin = Color.AQUA;
            }
            case 5 -> {
                //Pride
                p.getWorld().spawnParticle(Particle.SOUL, p.getLocation(), 150, 2, 2, 2, 0);
                p.setVelocity(p.getLocation().getDirection().normalize().multiply(3));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 1));
                timer = 250;
                sin = Color.ORANGE;
            }
            case 6 -> {
                //Envy
                p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, p.getLocation(), 250, 4, 4, 4, 0);
                for (Entity e : p.getNearbyEntities(8, 8, 8)) {
                    if (!e.getScoreboardTags().contains("Eclipse")) continue;
                    e.getWorld().strikeLightning(e.getLocation());
                }
                timer = 800;
                sin = Color.GREEN;
            }
        }
        //Timer
        HashMap<Integer,Long> newSinCooldown = sinCooldown.get(p.getUniqueId());
        newSinCooldown.replace(id,System.currentTimeMillis()+timer*50);
        sinCooldown.replace(p.getUniqueId(), newSinCooldown);

        final Color color = sin;
        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME,SoundCategory.PLAYERS,1,1);
            p.spawnParticle(Particle.REDSTONE,p.getLocation(),50,1,0.5,1,new Particle.DustOptions(color,1));
        },timer);
    }

    private void passiveSin(Player p, LivingEntity e) {
        if (!sinId.containsKey(p.getUniqueId())) sinId.put(p.getUniqueId(),0);
        int id = sinId.get(p.getUniqueId());

        switch (id) {
            case 0:
                //Sloth
                e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,50,0));
                e.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,50,0));
                break;
            case 1:
                //Greed
                if (random.nextInt(4)==0) {
                    LootTable loot = LootTables.SHIPWRECK_TREASURE.getLootTable();
                    LootContext.Builder contextBuilder = new LootContext.Builder(p.getLocation());
                    LootContext context = contextBuilder.build();
                    List<ItemStack> drop = (List<ItemStack>) loot.populateLoot(new Random(), context);
                    drop.get(0).setAmount(1);
                    e.getWorld().dropItem(e.getLocation(), drop.get(0));
                    p.getWorld().spawnParticle(Particle.TOTEM,e.getLocation(),60,1,1.5,1);
                    p.playSound(p.getLocation(),Sound.ITEM_TOTEM_USE,SoundCategory.PLAYERS,1,2);
                }
                break;
            case 2:
                //Lust
                if (random.nextInt(7)==0) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,40,2));
                    p.getWorld().spawnParticle(Particle.HEART,p.getLocation(),60,1,1.5,1);
                    p.playSound(p.getLocation(),Sound.BLOCK_AMETHYST_BLOCK_BREAK,SoundCategory.PLAYERS,1,0.5f);
                }
                break;
            case 3:
                //Wrath
                if (random.nextInt(11)==0) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,40,0));
                    p.getWorld().spawnParticle(Particle.BLOCK_CRACK,p.getLocation(),60,1,1.5,1,Material.REDSTONE_BLOCK.createBlockData());
                    p.playSound(p.getLocation(),Sound.BLOCK_STONE_BREAK,SoundCategory.PLAYERS,1,1);
                }
                break;
            case 4:
                //Gluttony
                if (random.nextInt(6)==0) {
                    e.damage(8);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,1,1));
                    double length = e.getLocation().subtract(p.getLocation()).length();
                    Vector direction = p.getEyeLocation().toVector().subtract(e.getEyeLocation().toVector()).multiply(1d/length);
                    Location loc = e.getLocation().add(0,0.5,0).clone();
                    for (int i=0; i<length;i++) {
                        p.getWorld().spawnParticle(Particle.DRAGON_BREATH,loc,10,0.2,0.2,0.2,0);
                        loc.add(direction);
                    }
                }
                break;
            case 5:
                //Pride
                if (random.nextInt(7)==0) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,60,0));
                    p.getWorld().spawnParticle(Particle.SOUL,p.getLocation(),60,1,1.5,1);
                    p.playSound(p.getLocation(),Sound.ENTITY_WITHER_SHOOT,SoundCategory.PLAYERS,1,1);
                }
                break;

            case 6:
                //Envy
                if (random.nextInt(12)==0) {
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

    private void cosmicArrow(Player p) {
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
            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,SoundCategory.PLAYERS,1,2);
            p.spawnParticle(Particle.REDSTONE,p.getLocation(),30,1,0.5,1,new Particle.DustOptions(Color.LIME,1));
        },50);
    }

    private void cosmicTrident(Player p) {
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
            p.playSound(p.getLocation(),Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,SoundCategory.PLAYERS,1,2);
            p.spawnParticle(Particle.REDSTONE,p.getLocation(),30,1,0.5,1,new Particle.DustOptions(Color.LIME,1));
        },20);
    }

    @Override
    public @NotNull String getName() {
        return "Cosmic Blessing";
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
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
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
    public boolean conflictsWith(@NotNull Enchantment other) {
        return true;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return true;
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text("Cosmic Blessing");
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return EnchantmentRarity.VERY_RARE;
    }

    @Override
    public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return null;
    }

    @Override
    public @NotNull String translationKey() {
        return null;
    }
}
