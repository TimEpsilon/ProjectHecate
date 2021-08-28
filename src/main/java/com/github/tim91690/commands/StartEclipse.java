package com.github.tim91690.commands;

import com.github.tim91690.eclipse.events.Meteor;
import com.github.tim91690.eclipse.mobs.boss.*;
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
                case "meteor":
                    new Meteor(p.getLocation());
                    break;
                case "bee":
                    new QueenBee(p.getLocation());
                    break;
            }

            return true;
        }
        return false;
    }
}
