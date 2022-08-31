package com.github.timepsilon.comet.commands;

import com.github.timepsilon.comet.mobs.boss.*;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BossList implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player p) {
            List<Boss> bossList = Boss.getBossList();
            HashMap<UUID, TempBoss> tempList = TempBoss.getTempBossList();
            p.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "Liste Des Boss :");
            p.sendMessage("      ");

            if (bossList.isEmpty() && tempList.isEmpty()) {
                p.sendMessage(ChatColor.YELLOW + "Aucun Boss n'est actuellement vivant");
                return true;
            }
            for (Boss boss : bossList) {
                Location loc = boss.getEntity().getLocation();
                String dist = (p.getWorld().equals(loc.getWorld())) ? ""+(int)loc.distance(p.getLocation()) : "NaN";

                p.sendMessage(ChatColor.YELLOW + "⚔ : " + boss.getEntity().getCustomName()
                        + ChatColor.YELLOW + ", ♥ : " + ChatColor.RED + (int)((LivingEntity)boss.getEntity()).getHealth()
                        + ChatColor.YELLOW + ", ⌚ : "
                        + ChatColor.GREEN + "<"
                        +(int)loc.getX()+" , "
                        +(int)loc.getY()+" , "
                        +(int)loc.getZ()+"> ("
                        +dist
                        +"m)");
                if (!Waypoint.PlayersHaveMaps.contains(p)) continue;
                switch (ChatColor.stripColor(boss.getName())) {
                    case "King Slime" -> p.sendMessage(Component.text("xaero-waypoint:KingSlime:KS:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":2:false:0:Internal-overworld-waypoints"));
                    case "Phantom Overlord" -> p.sendMessage(Component.text("xaero-waypoint:PhantomOverlord:PO:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":1:false:0:Internal-overworld-waypoints"));
                    case "Shadow" -> p.sendMessage(Component.text("xaero-waypoint:Shadow:S:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":0:false:0:Internal-overworld-waypoints"));
                    case "Scarlet Devil" -> p.sendMessage(Component.text("xaero-waypoint:ScarletRabbit:SR:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":4:false:0:Internal-overworld-waypoints"));
                    case "Night Fairy" -> p.sendMessage(Component.text("xaero-waypoint:NightFairy:NF:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":d:false:0:Internal-overworld-waypoints"));
                    case "Queen Bee" -> p.sendMessage(Component.text("xaero-waypoint:QueenBee:QB:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":6:false:0:Internal-overworld-waypoints"));
                }
            }

            for (TempBoss boss : tempList.values()) {
                Vector loc = boss.getLocation();
                String dist = (p.getWorld().getEnvironment().equals(World.Environment.NORMAL)) ? ""+loc.distance(p.getLocation().toVector()) : "NaN";
                p.sendMessage(ChatColor.YELLOW + "⚔ : " + boss.getName()
                        + ChatColor.YELLOW + ", ♥ : " + ChatColor.RED + boss.getHealth()
                        + ChatColor.YELLOW + ", ⌚ : "
                        + ChatColor.GREEN + "<"
                        +(int)loc.getX()+" , "
                        +(int)loc.getY()+" , "
                        +(int)loc.getZ()+"> ("
                        +dist
                        +"m)");

                if (!Waypoint.PlayersHaveMaps.contains(p)) continue;
                switch (ChatColor.stripColor(boss.getName())) {
                    case "King Slime" -> p.sendMessage(Component.text("xaero-waypoint:KingSlime:KS:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":2:false:0:Internal-overworld-waypoints"));
                    case "Phantom Overlord" -> p.sendMessage(Component.text("xaero-waypoint:PhantomOverlord:PO:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":1:false:0:Internal-overworld-waypoints"));
                    case "Scarlet Rabbit" -> p.sendMessage(Component.text("xaero-waypoint:ScarletRabbit:SR:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":4:false:0:Internal-overworld-waypoints"));
                }
            }


            p.sendMessage("      ");
            return true;
        }
        return false;
    }
}
