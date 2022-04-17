package com.github.timepsilon.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Waypoint implements CommandExecutor {

    public final static ArrayList<Player> PlayersHaveMaps = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player p)) return false;
        if (args.length == 0) {
            if (!PlayersHaveMaps.contains(p)) {
                PlayersHaveMaps.add(p);
                p.sendMessage(Component.text(ChatColor.GREEN + "Waypoints Visibles"));
                return true;
            }
            return true;
        }
        if (!(WaypointOnTabComplete.choice.contains(args[0]))) return false;

        if (Boolean.parseBoolean(args[0]) && !PlayersHaveMaps.contains(p)) {
            PlayersHaveMaps.add(p);
            p.sendMessage(Component.text(ChatColor.GREEN + "Waypoints Visibles"));
        }
        else if (!Boolean.parseBoolean(args[0])) {
            PlayersHaveMaps.remove(p);
            p.sendMessage(Component.text(ChatColor.RED + "Waypoints Non Visibles"));
        }
        return true;
    }
}
