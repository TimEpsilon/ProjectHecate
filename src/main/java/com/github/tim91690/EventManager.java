package com.github.tim91690;

import com.github.tim91690.commands.BossList;
import com.github.tim91690.commands.GiveMoonBlade;
import com.github.tim91690.commands.StartEclipse;
import com.github.tim91690.eclipse.item.CustomCraft;
import com.github.tim91690.eclipse.item.moonblade.enchants.EnchantRegister;
import com.github.tim91690.eclipse.listeners.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    public static EventManager plugin;

    @Override
    public void onEnable() {
        registerCommands();
        ListenerManager.registerEvents(this);
        new CustomCraft();
        EnchantRegister.register();

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
        getCommand("moonblade").setExecutor(new GiveMoonBlade());
    }

    public static EventManager getPlugin() {
        return plugin;
    }
}
