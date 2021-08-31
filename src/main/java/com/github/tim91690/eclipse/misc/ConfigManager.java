package com.github.tim91690.eclipse.misc;

import com.github.tim91690.EventManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private static Location loc = null;

    public static void loadResource() {
        EventManager.getPlugin().saveDefaultConfig();
    }


    private static FileConfiguration load() {
        File file = new File(EventManager.getPlugin().getDataFolder(),"config.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    private static int[] getCoord() {
        FileConfiguration fc = load();
        int[] coord = new int[3];

        coord[0] = fc.getInt("RitualLocation.x");
        coord[1] = fc.getInt("RitualLocation.y");
        coord[2] = fc.getInt("RitualLocation.z");
        return coord;
    }

    public static Location getLoc() {
        if (loc == null) {
            int[] coord = getCoord();
            World overworld = null;
            for (World world : Bukkit.getWorlds()) if(world.getEnvironment().compareTo(World.Environment.NORMAL) == 0) overworld = world;
            loc = new Location(overworld,coord[0],coord[1],coord[2]);
            Bukkit.broadcast(Component.text("loc"));
        }
        return loc;
    }
}
