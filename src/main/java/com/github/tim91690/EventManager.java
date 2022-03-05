package com.github.tim91690;

import com.github.tim91690.commands.BossList;
import com.github.tim91690.commands.GiveCosmicBlade;
import com.github.tim91690.commands.StartEclipse;
import com.github.tim91690.eclipse.item.CustomCraft;
import com.github.tim91690.eclipse.item.enchants.EnchantRegister;
import com.github.tim91690.eclipse.listeners.ListenerManager;
import com.github.tim91690.eclipse.misc.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    public static EventManager plugin;

    @Override
    public void onEnable() {
        registerCommands();
        EnchantRegister.register();
        ListenerManager.registerEvents(this);
        new CustomCraft();
        ConfigManager.loadResource();

    }

    @Override
    public void onDisable() {
        EnchantRegister.unregister();
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    private void registerCommands() {
        getCommand("eclipse").setExecutor(new StartEclipse());
        getCommand("bosslocation").setExecutor(new BossList());
        getCommand("moonblade").setExecutor(new GiveCosmicBlade());
    }

    public static EventManager getPlugin() {
        return plugin;
    }
}
