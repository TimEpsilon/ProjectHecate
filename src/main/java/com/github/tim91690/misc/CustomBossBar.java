package com.github.tim91690.misc;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.mobs.boss.Boss;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class CustomBossBar {

    public CustomBossBar() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(EventManager.getPlugin(), () -> {

            //Chaque boss
            for (Boss boss : Boss.getBossList()) {
                //entités dans un cube de 100x100x100 centré sur le boss
                List<Entity> nearby = boss.getEntity().getNearbyEntities(50,50,50);

                for (Entity target : nearby) {
                    if (target instanceof Player) {

                    }
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (nearby.contains(p)) {
                        //ajoute le joueur si il est aux alentours
                        boss.getBossbar().addPlayer(p);
                        boss.attack(p);

                    }
                    //le retire si il n'y est pas
                    else boss.getBossbar().removePlayer(p);
                }
            }

        }, 0, 100);
    }
}
