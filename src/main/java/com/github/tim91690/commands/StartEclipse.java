package com.github.tim91690.commands;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.events.Meteor;
import com.github.tim91690.eclipse.events.MoonRitual;
import com.github.tim91690.eclipse.misc.ConfigManager;
import com.github.tim91690.eclipse.misc.MagicCircle;
import com.github.tim91690.eclipse.mobs.ZombieTank;
import com.github.tim91690.eclipse.mobs.boss.*;
import com.github.tim91690.eclipse.structure.RitualArena;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class StartEclipse implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            switch (args[0]) {
                case "slime":
                    new KingSlime(p.getLocation());
                    break;
                case "phantom":
                    new PhantomOverlord(p.getLocation());
                    break;
                case "rabbit":
                    new ScarletRabbit(p.getLocation());
                    break;
                case "shadow":
                    new Shadows(p.getLocation());
                    break;
                case "meteor":
                    new Meteor(p.getLocation());
                    break;
                case "bee":
                    new QueenBee(p.getLocation());
                    break;
                case "ritual":
                    RitualArena.spawnRitualArena(false);
                    break;
                case "ritual_future":
                    RitualArena.spawnRitualArena(true);
                    break;
                case "distance":
                    Bukkit.broadcast(Component.text(ConfigManager.getLoc().distance(p.getLocation())));
                    break;
                case "fire":
                    new MoonRitual(ConfigManager.getLoc());
                    break;
                case "magic":
                    MagicCircle.inCircle(ConfigManager.getLoc().add(0.5,1,0.5),2);
                    break;
                case "demiurge":
                    new Demiurge(p.getLocation());
                    break;
                case "wither":
                    Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()-> {
                        double yaw = 2*Math.random()*Math.PI;
                        double pitch = -Math.random()*Math.PI/2;
                        double coscos = Math.cos(pitch) * Math.cos(yaw);
                        double cossin = -Math.cos(pitch) * Math.sin(yaw);
                        Location loc = p.getLocation().add(new Vector(cossin, Math.sin(pitch), coscos).multiply(0.2));
                        loc.setYaw((float)yaw);
                        loc.setPitch((float)pitch);
                        WitherSkull skull = (WitherSkull)p.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
                        skull.setDirection(new Vector(cossin, Math.sin(pitch), coscos).multiply(0.2));
                    },0,2);
                    break;
            }

            return true;
        }
        return false;
    }
}
