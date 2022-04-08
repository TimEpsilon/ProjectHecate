package com.github.timepsilon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CometOnTabComplete implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return StartComet.argumentList;
        if (args.length == 2 && args[0].equals("summon")) return StartComet.summonList;
        if (args.length == 2 && args[0].equals("debug")) return StartComet.debugList;
        return null;
    }
}
