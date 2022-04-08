package com.github.timepsilon.comet.events;

import com.github.timepsilon.EventManager;
import com.github.timepsilon.comet.misc.ConfigManager;
import com.github.timepsilon.comet.misc.Laser;
import com.github.timepsilon.comet.misc.TextManager;
import com.github.timepsilon.comet.mobs.boss.*;
import com.github.timepsilon.comet.structure.RitualArena;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public enum EnergyPylon {

    NORTH("KingSlime",0,-45),
    WEST("ScarletRabbit",-46,0),
    SOUTH("PhantomOverlord",0,45),
    EAST("Shadows",46,0);

    private final Location pylonLoc;
    private int progress = 0;
    private final String bossType;
    private int tick;
    private boolean hasActivated = false;
    private Laser laser;

    private final static int maxProgress = 100;
    private final Location center = ConfigManager.getLoc();

    EnergyPylon(String bossType,int x_rel,int z_rel) {
        this.pylonLoc = center.clone().add(x_rel,0,z_rel);
        this.bossType = bossType;
    }

    public void turnOn(int delay) {
        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), (@NotNull Runnable) this::turnOn,delay);
    }

    public void turnOn() {
        Vector rel = this.pylonLoc.toVector().subtract(center.toVector());
        switch (this.bossType) {
            case "KingSlime" -> new KingSlime(this.pylonLoc.clone().subtract(rel.getX() / 4f, -10, rel.getZ() / 4f));
            case "PhantomOverlord" -> new PhantomOverlord(this.pylonLoc.clone().subtract(rel.getX() / 4f, -10, rel.getZ() / 4f));
            case "ScarletRabbit" -> new ScarletRabbit(this.pylonLoc.clone().subtract(rel.getX() / 4f, -10, rel.getZ() / 4f));
            case "Shadows" -> new Shadows(this.pylonLoc.clone().subtract(rel.getX() / 4f, -10, rel.getZ() / 4f));
        }
        RitualArena.spawnPylon(this.pylonLoc,false);

        this.tick = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()-> tick(this.progress > maxProgress),0,10).getTaskId();
    }

    public void turnOff() {
        Bukkit.getScheduler().cancelTask(this.tick);
        this.laser.stop();
    }

    private void tick(boolean isActive) {
        if (isActive) {
            if (!hasActivated) {
                RitualArena.spawnPylon(this.pylonLoc,true);
                hasActivated = true;
                this.pylonLoc.getWorld().spawnParticle(Particle.TOTEM,this.pylonLoc,500,3,3,3,1,null,true);
                this.pylonLoc.getWorld().playSound(this.pylonLoc, Sound.BLOCK_BEACON_ACTIVATE,3,2);
                RespawnAnchor anchor = (RespawnAnchor) center.clone().add(0,1,0).getBlock().getBlockData();
                anchor.setCharges(anchor.getCharges()+1);
                center.clone().add(0,1,0).getBlock().setBlockData(anchor);
                this.laser = null;
                try {
                    this.laser = new Laser.GuardianLaser(this.pylonLoc.clone().add(0.5,5,0.5),center.clone().add(0.5,1,0.5),-1,64);
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
                laser.start(EventManager.getPlugin());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    TextManager.sendSamTextToPlayer(p,ChatColor.GREEN + "[INFO] Le pylône " + this + " est à présent actif.");
                }
            }
        } else {
            this.pylonLoc.getWorld().spawnParticle(Particle.GLOW,this.pylonLoc,100,2,2,2,0,null,true);
            this.pylonLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,this.pylonLoc.clone().add(1.75,2.5,0.5),100,0.2,0.2,0.2,0,null,true);
            this.pylonLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,this.pylonLoc.clone().add(-0.75,2.5,0.5),100,0.2,0.2,0.2,0,null,true);
            this.pylonLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,this.pylonLoc.clone().add(0.5,2.5,1.75),100,0.2,0.2,0.2,0,null,true);
            this.pylonLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,this.pylonLoc.clone().add(0.5,2.5,-0.75),100,0.2,0.2,0.2,0,null,true);

            StringBuilder progressText = new StringBuilder();

            for (int i = 0; i < maxProgress; i+=0.05*maxProgress) {
                if (i<progress) progressText.append("■");
                else progressText.append("-");
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().distance(this.pylonLoc) > 6) continue;
                p.sendActionBar(Component.text(ChatColor.YELLOW + "" + ChatColor.BOLD + "[ " + progressText + " ]"));
            }
        }
    }

    public void addProgress(int n) {
        this.progress += n;
    }

    public boolean getHasActivated() {
        return this.hasActivated;
    }

    public static EnergyPylon getClosestPylon(Location loc) {
        EnergyPylon closest = null;
        double distance = 1000;
        for (EnergyPylon pylon : EnergyPylon.values()) {
            if (distance > pylon.pylonLoc.distance(loc)) {
                closest = pylon;
                distance = pylon.pylonLoc.distance(loc);
            }
        }
        return closest;
    }
}
