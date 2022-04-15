package com.github.timepsilon.commands;

import com.github.timepsilon.EventManager;
import com.github.timepsilon.comet.events.EnergyPylon;
import com.github.timepsilon.comet.events.Meteor;
import com.github.timepsilon.comet.events.CosmicRitual;
import com.github.timepsilon.comet.misc.ConfigManager;
import com.github.timepsilon.comet.misc.MagicCircle;
import com.github.timepsilon.comet.mobs.boss.*;
import com.github.timepsilon.comet.structure.RitualArena;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StartComet implements CommandExecutor {

    public final static ArrayList<String> argumentList = new ArrayList<>(List.of(
            "start",
            "stop",
            "summon",
            "debug",
            "info"));
    public final static ArrayList<String> summonList = new ArrayList<>(List.of(
            "KingSlime",
            "PhantomOverlord",
            "ScarletRabbit",
            "Shadows",
            "QueenBee",
            "Demiurge"));
    public final static ArrayList<String> debugList = new ArrayList<>(List.of(
            "meteor",
            "arena_start",
            "arena_end",
            "ritual",
            "magic_circle",
            "start_pylon",
            "set_timer",
            "add_timer",
            "open_barrier",
            "items"));

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player p && sender.isOp()) {
            if (args.length == 0) {
                p.sendMessage(Component.text(ChatColor.RED + "Argument Invalide"));
                return false;
            }
            if (!argumentList.contains(args[0])) {
                p.sendMessage(Component.text(ChatColor.RED + "Argument Invalide"));
                return false;
            }


            switch (args[0]) {
                case "start" -> {
                    if (EventManager.isRunningEvent) {
                        p.sendMessage(Component.text(ChatColor.RED + "Event déjà en cours"));
                        return false;
                    }
                    EventManager.getComet().startEvent();
                    p.sendMessage(Component.text(ChatColor.GREEN + "Début de l'event"));
                    return true;
                }

                case "stop" -> {
                    if (!EventManager.isRunningEvent) {
                        p.sendMessage(Component.text(ChatColor.RED + "Aucun event en cours"));
                        return false;
                    }
                    EventManager.getComet().stopEvent(false);
                    return true;
                }

                case "info" -> {
                    if (!EventManager.isRunningEvent) {
                        p.sendMessage(Component.text(ChatColor.RED + "Aucun event en cours"));
                        return false;
                    }
                    Location loc = ConfigManager.getLoc();
                    p.sendMessage(Component.text(ChatColor.YELLOW + "Ticks since Start : " + EventManager.getComet().getTime()));
                    p.sendMessage(Component.text(ChatColor.YELLOW + "Time since Start : " + EventManager.getComet().getTime()/20/60 + "min"));
                    p.sendMessage(Component.text(ChatColor.YELLOW + "Current Phase : " + EventManager.getComet().getPhase()));
                    p.sendMessage(Component.text(ChatColor.YELLOW + "Ritual Location : <" + loc.getX() + "," + loc.getY() + "," + loc.getZ() +">"));
                    return true;
                }

                case "summon" -> {

                    switch (args[1]) {
                        case "KingSlime" -> new KingSlime(p.getLocation());
                        case "PhantomOverlord" -> new PhantomOverlord(p.getLocation());
                        case "ScarletRabbit" -> new ScarletRabbit(p.getLocation());
                        case "Shadows" -> new Shadows(p.getLocation());
                        case "QueenBee" -> new QueenBee(p.getLocation());
                        case "Demiurge" -> new Demiurge(p.getLocation());
                        default -> p.sendMessage(Component.text(ChatColor.RED + "Argument Invalide"));
                    }

                    p.sendMessage(Component.text(ChatColor.GREEN + args[1] + " spawné"));
                }

                case "debug" -> {

                    switch (args[1]) {

                        case "meteor" -> new Meteor(p.getLocation());
                        case "arena_start" -> RitualArena.spawnRitualArena(false);
                        case "arena_end" -> RitualArena.spawnRitualArena(true);
                        case "ritual" -> new CosmicRitual(ConfigManager.getLoc());
                        case "magic_circle" -> MagicCircle.inCircle(ConfigManager.getLoc().add(0.5, 1, 0.5), 2);
                        case "start_pylon" -> {
                            EnergyPylon.WEST.turnOn();
                            EnergyPylon.EAST.turnOn();
                            EnergyPylon.NORTH.turnOn();
                            EnergyPylon.SOUTH.turnOn();
                        }
                        case "set_timer" -> {
                            int time = (args.length < 3) ? 0 : Integer.parseInt(args[2]);
                            EventManager.getComet().setTime(time);
                        }
                        case "add_timer" -> {
                            int time = (args.length < 3) ? 0 : Integer.parseInt(args[2]);
                            EventManager.getComet().setTime(EventManager.getComet().getTime() + time);
                        }
                        case "open_barrier" -> RitualArena.openBarrier();
                        case "items" -> GiveCosmicBlade.getItems(p);
                        default -> p.sendMessage(Component.text(ChatColor.RED + "Argument Invalide"));
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
