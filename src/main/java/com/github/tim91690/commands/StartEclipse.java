package com.github.tim91690.commands;

import com.github.tim91690.eclipse.mobs.boss.KingSlime;
import com.github.tim91690.eclipse.mobs.boss.PhantomOverlord;
import com.github.tim91690.eclipse.mobs.boss.ScarletRabbit;
import com.github.tim91690.eclipse.mobs.boss.Shadows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartEclipse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
            }

            return true;
        }
        return false;
    }
}
