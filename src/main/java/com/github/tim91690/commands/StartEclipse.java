package com.github.tim91690.commands;

import com.github.tim91690.eclipse.mobs.boss.PhantomOverlord;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartEclipse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Location loc = p.getLocation();
            loc.setY(150);
            new PhantomOverlord(loc);

            return true;
        }
        return false;
    }
}
