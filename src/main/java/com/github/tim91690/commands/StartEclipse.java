package com.github.tim91690.commands;

import com.github.tim91690.eclipse.mobs.Zombie;
import net.minecraft.server.level.WorldServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;

public class StartEclipse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Zombie test = new Zombie(p.getLocation());
            WorldServer world = ((CraftWorld) p.getWorld()).getHandle();
            world.addEntity(test);
            return true;
        }
        return false;
    }
}
