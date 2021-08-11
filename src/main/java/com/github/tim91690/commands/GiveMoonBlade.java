package com.github.tim91690.commands;

import com.github.tim91690.eclipse.item.moonblade.MoonBlade;
import com.github.tim91690.eclipse.item.MoonSoul;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveMoonBlade implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            MoonBlade blade = new MoonBlade();
            blade.giveItem(p);
            p.getInventory().addItem(MoonSoul.getSoul(64));
            p.sendMessage(p.getPing()+"");
            return true;
        }
        return false;
    }
}
