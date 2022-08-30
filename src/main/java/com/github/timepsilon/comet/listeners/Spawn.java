package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.misc.WeightCollection;
import com.github.timepsilon.comet.mobs.*;

import com.github.timepsilon.comet.mobs.boss.KingSlime;
import com.github.timepsilon.comet.mobs.boss.PhantomOverlord;
import com.github.timepsilon.comet.mobs.boss.ScarletRabbit;
import com.github.timepsilon.comet.mobs.boss.Shadows;
import com.github.timepsilon.comet.mobs.semiboss.DrownedOverlordHorse;
import com.github.timepsilon.comet.mobs.semiboss.IllusionerMage;
import com.github.timepsilon.comet.mobs.semiboss.PhantomFurries;
import com.github.timepsilon.comet.mobs.semiboss.RavagerBeast;
import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Random;

public class Spawn implements Listener {

    private final float probaSemiBoss = 0.05f;
    private static final Random random = new Random();

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (!ProjectHecate.isRunningEvent) return;
        if (!(event.getEntity() instanceof Monster) || (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL) && (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.COMMAND)))) return;
        if (event.getLocation().getY() < 60) event.setCancelled(true);

        int lvl = ProjectHecate.getComet().getPhase();

        if (random.nextFloat()<probaSemiBoss) {
            event.setCancelled(true);
            spawnSemiBoss(event.getLocation(),lvl);
            return;
        }

        switch (event.getEntity().getType().toString()) {
            case ("ZOMBIE"), ("DROWNED"), ("HUSK") -> //Zombie Tank remplace les zombies, drowned et husk
                    new ZombieTank(event.getEntity(), lvl);
            case ("CREEPER") -> //Creeper Bomb remplace creeper
                    new CreeperBomb(event.getEntity());
            case ("SKELETON") -> //Skeleton Sniper remplace skeletons
                    new SkeletonSniper(event.getEntity());
            case ("SPIDER") ->
                    //Spider Crawler remplace spider
                    new SpiderCrawler(event.getEntity());
            case ("WITCH") ->
                    new WickedWitch(event.getEntity());
            case ("ENDERMAN") ->
                    new ParanoidEnderman(event.getEntity());
        }
    }

    /**Semi boss à chaque étapes (1% -> 5%) :
     1) -> Phantom Furries (100%)
     2) -> Ravager Beast (70%), Phantom Furries (30%)
     3) -> Illusioner Mage (70%), Ravager Beast (25%), Phantom Furries (5%)
     4) -> Drowned Overlord (70%), Illusioner Mage (20%), Ravager Beast (7%), Phantom Furries (3%)
     **/
    private void spawnSemiBoss(Location loc,int lvl) {
        WeightCollection<String> rc = new WeightCollection<>();
        switch (lvl) {
            case 1 -> rc.add(100, "PhantomFurries");
            case 2 -> rc.add(70, "RavagerBeast").add(30, "PhantomFurries");
            case 3 -> rc.add(70, "IllusionerMage").add(25, "RavagerBeast").add(5, "PhantomFurries");
            case 4,5 -> rc.add(70, "DrownedOverlord").add(20, "IllusionerMage").add(7, "RavagerBeast").add(3, "PhantomFurries");
            default -> rc.add(1,"");
        }

        switch (rc.next()) {
            case "PhantomFurries" -> new PhantomFurries(loc);
            case "RavagerBeast" -> new RavagerBeast(loc);
            case "IllusionerMage" -> new IllusionerMage(loc);
            case "DrownedOverlord" -> new DrownedOverlordHorse(loc);
        }
    }

    private void spawnBoss(Location loc, int lvl) {
        WeightCollection<String> rc = new WeightCollection<>();
        switch (lvl) {
            case 1 -> rc.add(80, "KingSlime");
            case 2 -> rc.add(80, "PhantomOverlord").add(20, "KingSlime");
            case 3 -> rc.add(80, "ScarletRabbit").add(17, "PhantomOverlord").add(3, "KingSlime");
            case 4,5 -> rc.add(80, "Shadows").add(15, "ScarletRabbit").add(4, "PhantomOverlord").add(1, "KingSlime");
            default -> rc.add(1,"");
        }

        switch (rc.next()) {
            case "KingSlime" -> new KingSlime(loc);
            case "PhantomOverlord" -> new PhantomOverlord(loc);
            case "ScarletRabbit" -> new ScarletRabbit(loc);
            case "Shadows" -> new Shadows(loc);
        }
    }
}
