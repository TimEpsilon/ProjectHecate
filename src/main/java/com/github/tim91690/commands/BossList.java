package com.github.tim91690.commands;

import com.github.tim91690.eclipse.mobs.boss.Boss;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class BossList implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            List<Boss> bossList = Boss.getBossList();
            Player p = (Player) sender;
            p.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + "Liste Des Boss :");
            p.sendMessage("      ");

            if (bossList.isEmpty()) {
                p.sendMessage(ChatColor.YELLOW + "Aucun Boss n'est actuellement vivant");
                return false;
            }
            for (Boss boss : bossList) {
                p.sendMessage(ChatColor.YELLOW + "⚔ : " + boss.getEntity().getCustomName()
                        + ChatColor.YELLOW + ", ♥ : " + ChatColor.RED + (int)((LivingEntity)boss.getEntity()).getHealth()
                        + ChatColor.YELLOW + ", ⌚ : "
                        + ChatColor.GREEN + "<"
                        +(int)boss.getEntity().getLocation().getX()+" , "
                        +(int)boss.getEntity().getLocation().getY()+" , "
                        +(int)boss.getEntity().getLocation().getZ()+">");
            }
            p.sendMessage("      ");
        }
        return false;
    }
}
