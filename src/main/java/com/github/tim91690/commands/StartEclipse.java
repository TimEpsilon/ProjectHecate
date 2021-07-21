package com.github.tim91690.commands;

import com.github.tim91690.eclipse.mobs.boss.KingSlime;
import com.github.tim91690.eclipse.mobs.boss.PhantomOverlord;
import com.github.tim91690.eclipse.mobs.boss.ScarletRabbit;
import com.github.tim91690.eclipse.mobs.boss.Shadows;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.util.Vector;

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
                case "fireball":
                    for (int i = 0;i<20;i++) {
                        Location loc = p.getLocation().add(new Vector(Math.cos(i * 2 * Math.PI / 20), 0d, Math.sin(i * 2 * Math.PI / 20)).multiply(0.2));
                        loc.setYaw(i*360/20-90);
                        loc.setPitch(0);
                        WitherSkull skull = (WitherSkull)p.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
                        skull.setDirection(new Vector(Math.cos(i * 2 * Math.PI / 20), 0d, Math.sin(i * 2 * Math.PI / 20)).multiply(0.1));
                        skull.setCharged(true);
                        skull.setVisualFire(true);
                        skull.setRotation(i/20,i/20);
                    }
            }

            return true;
        }
        return false;
    }
}
