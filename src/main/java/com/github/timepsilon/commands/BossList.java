package com.github.timepsilon.commands;

import com.github.timepsilon.comet.mobs.boss.Boss;
import com.github.timepsilon.comet.mobs.boss.NightFairy;
import com.github.timepsilon.comet.mobs.boss.QueenBee;
import com.github.timepsilon.comet.mobs.boss.TempBoss;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
                return false;
            }
            for (Boss boss : bossList) {
                if (boss instanceof NightFairy ||boss instanceof QueenBee) continue;
                p.sendMessage(ChatColor.YELLOW + "⚔ : " + boss.getEntity().getCustomName()
                        + ChatColor.YELLOW + ", ♥ : " + ChatColor.RED + (int)((LivingEntity)boss.getEntity()).getHealth()
                        + ChatColor.YELLOW + ", ⌚ : "
                        + ChatColor.GREEN + "<"
                        +(int)boss.getEntity().getLocation().getX()+" , "
                        +(int)boss.getEntity().getLocation().getY()+" , "
                        +(int)boss.getEntity().getLocation().getZ()+"> ("
                        +(int)boss.getEntity().getLocation().distance(p.getLocation())
                        +"m)");
            }

            for (TempBoss boss : tempList.values()) {
                p.sendMessage(ChatColor.YELLOW + "⚔ : " + boss.getName()
                        + ChatColor.YELLOW + ", ♥ : " + ChatColor.RED + boss.getHealth()
                        + ChatColor.YELLOW + ", ⌚ : "
                        + ChatColor.GREEN + "<"
                        +(int)boss.getLocation().getX()+" , "
                        +(int)boss.getLocation().getY()+" , "
                        +(int)boss.getLocation().getZ()+"> ("
                        +(int)boss.getLocation().distance(p.getLocation().toVector())
                        +"m)");
            }


            p.sendMessage("      ");
        }
        return false;
    }
}
