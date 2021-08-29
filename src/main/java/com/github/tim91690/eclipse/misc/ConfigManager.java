package com.github.tim91690.eclipse.misc;

import com.github.tim91690.EventManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    public static void loadResource() {
        EventManager.getPlugin().saveDefaultConfig();
    }


    private static FileConfiguration load() {
        File file = new File(EventManager.getPlugin().getDataFolder(),"config.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static int[] getCoord() {
        FileConfiguration fc = load();
        int[] coord = new int[3];

        coord[0] = fc.getInt("RitualLocation.x");
        coord[1] = fc.getInt("RitualLocation.y");
        coord[2] = fc.getInt("RitualLocation.z");
        return coord;
    }
}
