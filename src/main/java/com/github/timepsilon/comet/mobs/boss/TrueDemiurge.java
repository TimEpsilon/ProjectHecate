package com.github.timepsilon.comet.mobs.boss;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.misc.ConfigManager;
import com.github.timepsilon.comet.misc.Interpolation;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class TrueDemiurge extends Boss {

    private int phase = 4;
    private ArmorStand arms;
    private ArmorStand legs;
    private ArmorStand wings;
    private int tick;
    private int idle;

    private static final Location center = ConfigManager.getLoc();
    public static final String NAME = ChatColor.BOLD+""+ChatColor.DARK_AQUA+"DEMIURGE";

    public TrueDemiurge(Location loc) {
        super(loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE),1000,NAME, BarColor.GREEN, null,0);

        Bukkit.broadcast(Component.text(ChatColor.GREEN + "Le " + ChatColor.DARK_AQUA + " DEMIURGE " + ChatColor.GREEN + " est éveillé"));

        entity.setCustomName(name);
        entity.setAI(false);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100000000,0,false,false));
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getMaxHealth());
        entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(35);
        entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(15);
        entity.setHealth(this.getMaxHealth());
        Objects.requireNonNull(entity.getEquipment()).clear();

        arms = modelConstruct(605,604,601);
        legs = modelConstruct(603,602,600);
        wings = modelConstruct(606,606,0);

        tick();
        idle();
    }

    private void tick() {
        tick = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
            arms.teleport(entity);
            legs.teleport(entity);
            wings.teleport(entity);
        },0,1).getTaskId();
    }

    private void idle() {
        AtomicInteger t = new AtomicInteger();
        final float Tarms = 120;
        final float Tlegs = 105;
        final float Twings = 15;
        idle = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
            double angleWing = Math.cos(t.get()/Twings*2*Math.PI)*Math.PI/6 + Math.PI/4;
            wings.setRightArmPose(new EulerAngle(angleWing,0,Math.PI/2));
            wings.setLeftArmPose(new EulerAngle(angleWing,0,-Math.PI/2));

            double angleArms = Math.cos(t.get()/Tarms*2*Math.PI)*Math.PI/10;
            arms.setRightArmPose(new EulerAngle(angleArms,0,0));
            arms.setLeftArmPose(new EulerAngle(-angleArms,0,0));

            double angleLegs = Math.cos(t.get()/Tlegs*2*Math.PI)*Math.PI/18;
            legs.setRightArmPose(new EulerAngle(angleLegs,0,0));
            legs.setLeftArmPose(new EulerAngle(-angleLegs,0,0));

            t.getAndIncrement();
        },0,1).getTaskId();
    }


    @Override
    public void attack(List<Player> proxPlayer) {

    }

    private void animationHold() {
        int time = 30;
        List<EulerAngle> angleHead = Interpolation.angleLerp(arms.getHeadPose(),new EulerAngle(25*Math.PI/180,0,0),time);
        List<EulerAngle> leftAngle = Interpolation.angleLerp(arms.getLeftArmPose(),new EulerAngle(310*Math.PI/180,30*Math.PI/180,0),time);
        List<EulerAngle> rightAngle = Interpolation.angleLerp(arms.getRightLegPose(),new EulerAngle(310*Math.PI/180,30*Math.PI/180,0),time);
        AtomicInteger i = new AtomicInteger();
        int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
            arms.setHeadPose(angleHead.get(i.get()));
            arms.setLeftArmPose(leftAngle.get(i.get()));
            arms.setRightArmPose(rightAngle.get(i.get()));
            i.getAndIncrement();
        },0,1).getTaskId();

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> Bukkit.getScheduler().cancelTask(task),time);
    }

    private ArmorStand modelConstruct(int left, int right, int head) {
        ArmorStand stand = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
        stand.setMarker(true);
        stand.setInvisible(true);
        stand.setArms(true);

        ItemStack modelItem = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = modelItem.getItemMeta();

        meta.setCustomModelData(left);
        modelItem.setItemMeta(meta);
        stand.getEquipment().setItemInOffHand(modelItem);

        meta.setCustomModelData(right);
        modelItem.setItemMeta(meta);
        stand.getEquipment().setItemInMainHand(modelItem);

        if (head == 0) return stand;
        meta.setCustomModelData(head);
        modelItem.setItemMeta(meta);
        stand.getEquipment().setHelmet(modelItem);
        return stand;
    }
}
