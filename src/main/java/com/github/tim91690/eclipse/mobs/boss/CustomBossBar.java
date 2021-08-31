package com.github.tim91690.eclipse.mobs.boss;

import com.github.tim91690.EventManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CustomBossBar {
    private static List<Player> proxPlayer;

    public CustomBossBar() {
        proxPlayer = new ArrayList<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(EventManager.getPlugin(), () -> {

            //Chaque boss
            for (Boss boss : Boss.getBossList()) {
                //entités dans un cube de 80x80x80 centré sur le boss
                List<Entity> nearby = boss.getEntity().getNearbyEntities(40,40,40);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (nearby.contains(p)) {
                        //ajoute le joueur si il est aux alentours
                        boss.getBossbar().addPlayer(p);
                        proxPlayer.add(p);
                    }
                    //le retire si il n'y est pas
                    else boss.getBossbar().removePlayer(p);
                }
                if (proxPlayer.size() != 0) boss.attack(proxPlayer);
                proxPlayer.clear();
            }

        }, 0, 100);
    }
}