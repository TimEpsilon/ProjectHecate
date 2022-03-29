package com.github.tim91690.comet.mobs.boss;

import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class TempBoss {

    private final Vector loc;
    private final int health;
    private final String name;
    private static final HashMap<UUID,TempBoss> tempBossList = new HashMap<>();


    public TempBoss(Vector loc, int health, String name,UUID uuid) {
        this.loc = loc;
        this.health = health;
        this.name = name;

        tempBossList.put(uuid,this);
    }

    public Vector getLocation() {
        return loc;
    }

    public static HashMap<UUID, TempBoss> getTempBossList() {
        return tempBossList;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }
}
