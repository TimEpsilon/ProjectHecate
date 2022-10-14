package com.github.timepsilon.comet.mobs.boss.demiurge.final_form;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.misc.ConfigManager;
import com.github.timepsilon.comet.misc.Interpolation;
import com.github.timepsilon.comet.misc.WeightCollection;
import com.github.timepsilon.comet.mobs.boss.Boss;
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
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class TrueDemiurge extends Boss {

    private int phase = 4;
    private final ArmorStand arms;
    private final ArmorStand legs;
    private final ArmorStand wings;
    private int taskTick;
    private int taskArmRight;
    private int taskArmLeft;
    private int taskLegRight;
    private int taskLegLeft;
    private int taskHead;
    private int taskWings;

    private static final Location center = ConfigManager.getLoc();
    public static final String NAME = ChatColor.BOLD+""+ChatColor.DARK_AQUA+"DEMIURGE";
    private static final double degToRad = Math.PI/180;

    private static final WeightCollection<TrueDemiurgeAttack> ATTACKS = getAttacks();

    private static WeightCollection<TrueDemiurgeAttack> getAttacks() {
        WeightCollection<TrueDemiurgeAttack> rc = new WeightCollection<>();
        Arrays.stream(TrueDemiurgeAttack.values()).forEach(attack -> rc.add(attack.getWeight(),attack));
        return rc;
    }

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
        entity.setSilent(true);
        ((Zombie)entity).setShouldBurnInDay(false);
        ((Zombie)entity).setAdult();
        Objects.requireNonNull(entity.getEquipment()).clear();

        arms = modelConstruct(605,604,601);
        legs = modelConstruct(603,602,600);
        wings = modelConstruct(606,606,0);


        attackTime = 500;
        tick();
        idle();
    }

    private void tick() {
        taskTick = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
            arms.teleport(entity);
            legs.teleport(entity);
            wings.teleport(entity);
        },0,1).getTaskId();
    }

    private void idle() {
        Bukkit.broadcastMessage("IDLE");
        final float Tarms = 120;
        final float Tlegs = 105;
        final float Twings = 15;
        final float Thead = 150;

        int lived = entity.getTicksLived();

        if (!Bukkit.getScheduler().isCurrentlyRunning(taskArmRight)) {
            taskArmRight = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
                int t = entity.getTicksLived() - lived;
                double angleArms = Math.cos(t/Tarms*2*Math.PI)*Math.PI/10;
                arms.setRightArmPose(new EulerAngle(angleArms,0,0));
            },0,1).getTaskId();
        }

        if (!Bukkit.getScheduler().isCurrentlyRunning(taskArmLeft)) {
            taskArmLeft = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()-> {
                int t = entity.getTicksLived() - lived;
                double angleArms = Math.cos(t / Tarms * 2 * Math.PI) * Math.PI / 10;
                arms.setLeftArmPose(new EulerAngle(-angleArms, 0, 0));
            },0,1).getTaskId();
        }

        if (!Bukkit.getScheduler().isCurrentlyRunning(taskLegRight)) {
            taskLegRight = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()-> {
                int t = entity.getTicksLived() - lived;
                double angleLegs = Math.cos(t/Tlegs*2*Math.PI)*Math.PI/18;
                legs.setRightArmPose(new EulerAngle(angleLegs,0,0));
            },0,1).getTaskId();
        }

        if (!Bukkit.getScheduler().isCurrentlyRunning(taskLegRight)) {
            taskLegRight = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()-> {
                int t = entity.getTicksLived() - lived;
                double angleLegs = Math.cos(t/Tlegs*2*Math.PI)*Math.PI/18;
                legs.setLeftArmPose(new EulerAngle(-angleLegs,0,0));
            },0,1).getTaskId();
        }

        if (!Bukkit.getScheduler().isCurrentlyRunning(taskHead)) {
            taskHead = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()-> {
                int t = entity.getTicksLived() - lived;
                double angleHead = Math.cos(t/Thead*2*Math.PI)*8*degToRad;
                arms.setHeadPose(new EulerAngle(angleHead,0,0));
            },0,1).getTaskId();
        }


        if (!Bukkit.getScheduler().isCurrentlyRunning(taskWings)) {
            taskWings = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), ()->{
                int t = entity.getTicksLived() - lived;
                double angleWing = Math.cos(t/Twings*2*Math.PI)*Math.PI/6 + Math.PI/4;
                wings.setRightArmPose(new EulerAngle(angleWing,0,Math.PI/2));
                wings.setLeftArmPose(new EulerAngle(angleWing,0,-Math.PI/2));
            },0,1).getTaskId();
        }
    }


    @Override
    public void attack(List<Player> proxPlayer) {
        if (proxPlayer.isEmpty()) return;
        TrueDemiurgeAttack attack = ATTACKS.next();
        Bukkit.broadcastMessage(attack.toString());
        switch (attack) {
            case SWORD_SWING_VERTICAL -> animationHold();
            case SWORD_SWING_HORIZONTAL -> animationSwing();
            case BOOMERANG -> animationThrowLeft();
            case DASH -> animationThrowRight();
            case GRAB -> animationGrab();
            case MAGIC_CIRCLE -> animationMagic();
        }

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> returnToNeutral(10),70);
    }

    private void playAnimation(EulerAngle start, EulerAngle end, int time, TrueDemiurgeParts part) {
        List<EulerAngle> angles = Interpolation.angleLerp(start,end,time);
        AtomicInteger i = new AtomicInteger();
        switch (part) {
            case ARM_LEFT -> {
                if (Bukkit.getScheduler().isCurrentlyRunning(taskArmLeft)) Bukkit.getScheduler().cancelTask(taskArmLeft);
                int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
                    if (i.get()>=time) return;
                    arms.setLeftArmPose(angles.get(i.get()));
                    i.getAndIncrement();
                }, 0, 1).getTaskId();
                Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> Bukkit.getScheduler().cancelTask(task), time);
            }

            case ARM_RIGHT -> {
                if (Bukkit.getScheduler().isCurrentlyRunning(taskArmRight)) Bukkit.getScheduler().cancelTask(taskArmRight);
                int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
                    if (i.get()>=time) return;
                    arms.setRightArmPose(angles.get(i.get()));
                    i.getAndIncrement();
                }, 0, 1).getTaskId();
                Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> Bukkit.getScheduler().cancelTask(task), time);
            }

            case HEAD -> {
                if (Bukkit.getScheduler().isCurrentlyRunning(taskHead)) Bukkit.getScheduler().cancelTask(taskHead);
                int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
                    if (i.get()>=time) return;
                    arms.setHeadPose(angles.get(i.get()));
                    i.getAndIncrement();
                }, 0, 1).getTaskId();
                Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> Bukkit.getScheduler().cancelTask(task), time);
            }

            case LEG_LEFT -> {
                if (Bukkit.getScheduler().isCurrentlyRunning(taskLegLeft)) Bukkit.getScheduler().cancelTask(taskLegLeft);
                int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
                    if (i.get()>=time) return;
                    legs.setLeftArmPose(angles.get(i.get()));
                    i.getAndIncrement();
                }, 0, 1).getTaskId();
                Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> Bukkit.getScheduler().cancelTask(task), time);
            }

            case LEG_RIGHT -> {
                if (Bukkit.getScheduler().isCurrentlyRunning(taskLegRight)) Bukkit.getScheduler().cancelTask(taskLegRight);
                int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
                    if (i.get()>=time) return;
                    legs.setRightArmPose(angles.get(i.get()));
                    i.getAndIncrement();
                }, 0, 1).getTaskId();
                Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> Bukkit.getScheduler().cancelTask(task), time-1);
            }
        }
    }

    private void returnToNeutral(int time) {
        for (TrueDemiurgeParts part : TrueDemiurgeParts.values()) {
            switch (part) {
                case HEAD -> playAnimation(arms.getHeadPose(),new EulerAngle(0,0,0),time,part);
                case ARM_RIGHT -> playAnimation(arms.getRightArmPose(),new EulerAngle(0,0,0),time,part);
                case ARM_LEFT -> playAnimation(arms.getLeftArmPose(),new EulerAngle(0,0,0),time,part);
                case LEG_LEFT -> playAnimation(legs.getLeftArmPose(),new EulerAngle(0,0,0),time,part);
                case LEG_RIGHT -> playAnimation(legs.getRightArmPose(),new EulerAngle(0,0,0),time,part);
            }
        }
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),this::idle,time);
    }

    private void animationHold() {
        int time = 15;

        playAnimation(arms.getHeadPose(),new EulerAngle(25*degToRad,0,0),time, TrueDemiurgeParts.HEAD);
        playAnimation(arms.getLeftArmPose(),new EulerAngle(-50*degToRad,30*degToRad,0),time, TrueDemiurgeParts.ARM_LEFT);
        playAnimation(arms.getRightLegPose(),new EulerAngle(-50*degToRad,-32*degToRad,0),time, TrueDemiurgeParts.ARM_RIGHT);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> returnToNeutral(10),70);
    }

    private void animationGrab() {
        int time = 18;

        playAnimation(arms.getHeadPose(),new EulerAngle(10*degToRad,0,0),time, TrueDemiurgeParts.HEAD);
        playAnimation(arms.getLeftArmPose(),new EulerAngle(12*degToRad,24*degToRad,-2*degToRad),time, TrueDemiurgeParts.ARM_LEFT);
        playAnimation(arms.getRightLegPose(),new EulerAngle(-81*degToRad,-10*degToRad,20*degToRad),time, TrueDemiurgeParts.ARM_RIGHT);
        playAnimation(legs.getLeftArmPose(),new EulerAngle(40*degToRad,10*degToRad,0),time, TrueDemiurgeParts.LEG_LEFT);
        playAnimation(legs.getRightLegPose(),new EulerAngle(42*degToRad,-10*degToRad,0),time, TrueDemiurgeParts.LEG_RIGHT);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> returnToNeutral(10),70);
    }

    private void animationSwing() {
        int time = 30;

        playAnimation(arms.getRightArmPose(),new EulerAngle(165*degToRad,0,-20*degToRad),10,TrueDemiurgeParts.ARM_RIGHT);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> {
            playAnimation(new EulerAngle(165*degToRad,0,-20*degToRad),new EulerAngle(333*degToRad,0,-22*degToRad),time, TrueDemiurgeParts.ARM_RIGHT);
        }, 11);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> returnToNeutral(10),70);
    }

    private void animationThrowLeft() {
        int time = 20;

        playAnimation(arms.getLeftArmPose(),new EulerAngle(56*degToRad,0,66*degToRad),10,TrueDemiurgeParts.ARM_LEFT);
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()->
            playAnimation(new EulerAngle(56*degToRad,0,66*degToRad),new EulerAngle(-120*degToRad,0,42*degToRad),time, TrueDemiurgeParts.ARM_LEFT)
        , 11);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> returnToNeutral(10),70);
    }

    private void animationThrowRight() {
        int time = 20;

        playAnimation(arms.getRightArmPose(),new EulerAngle(44*degToRad,0,-58*degToRad),10,TrueDemiurgeParts.ARM_RIGHT);
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()->
                playAnimation(new EulerAngle(44*degToRad,0,-58*degToRad),new EulerAngle(279*degToRad,0,-62*degToRad),time, TrueDemiurgeParts.ARM_RIGHT)
                , 11);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> returnToNeutral(10),70);
    }

    private void animationMagic() {
        int time = 50;

        playAnimation(arms.getHeadPose(),new EulerAngle(-30*degToRad,0,0),time, TrueDemiurgeParts.HEAD);
        playAnimation(arms.getLeftArmPose(),new EulerAngle(-160*degToRad,0,30*degToRad),time, TrueDemiurgeParts.ARM_LEFT);
        playAnimation(arms.getRightLegPose(),new EulerAngle(-161*degToRad,0,-31*degToRad),time, TrueDemiurgeParts.ARM_RIGHT);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> returnToNeutral(10),70);
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
