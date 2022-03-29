package com.github.tim91690.commands;

import com.github.tim91690.EventManager;
import com.github.tim91690.comet.events.EnergyPylon;
import com.github.tim91690.comet.events.Meteor;
import com.github.tim91690.comet.events.CosmicRitual;
import com.github.tim91690.comet.misc.ConfigManager;
import com.github.tim91690.comet.misc.MagicCircle;
import com.github.tim91690.comet.mobs.boss.*;
import com.github.tim91690.comet.structure.RitualArena;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartEclipse implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            switch (args[0]) {
                case "slime" -> new KingSlime(p.getLocation());
                case "phantom" -> new PhantomOverlord(p.getLocation());
                case "rabbit" -> new ScarletRabbit(p.getLocation());
                case "shadow" -> new Shadows(p.getLocation());
                case "meteor" -> new Meteor(p.getLocation());
                case "bee" -> new QueenBee(p.getLocation());
                case "ritual" -> RitualArena.spawnRitualArena(false);
                case "ritual_future" -> RitualArena.spawnRitualArena(true);
                case "distance" -> Bukkit.broadcast(Component.text(ConfigManager.getLoc().distance(p.getLocation())));
                case "fire" -> new CosmicRitual(ConfigManager.getLoc());
                case "barrier" -> RitualArena.openBarrier();
                case "magic" -> MagicCircle.inCircle(ConfigManager.getLoc().add(0.5, 1, 0.5), 2);
                case "demiurge" -> new Demiurge(p.getLocation());
                case "pylon" -> {
                    EnergyPylon.WEST.turnOn();
                    EnergyPylon.EAST.turnOn();
                    EnergyPylon.NORTH.turnOn();
                    EnergyPylon.SOUTH.turnOn();
                }
                case "time" -> Bukkit.broadcastMessage(EventManager.getComet().getTime() +" ticks, " + EventManager.getComet().getTime()/20/60 + "min " + EventManager.getComet().getPhase() + " phase");
                case "timeset" -> EventManager.getComet().setTime(Integer.parseInt(args[1]));
                case "start" -> {
                    if (EventManager.isRunningEvent) return false;
                    EventManager.getComet().startEvent();
                }
                case "stop" -> EventManager.getComet().stopEvent(false);
            }
            return true;
        }
        return false;
    }
}
