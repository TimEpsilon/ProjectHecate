package com.github.timepsilon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CometOnTabComplete implements TabCompleter {

    public static List<String> dynamicTab(List<String> list, String arg) {
        List<String> finalList = new ArrayList<>(list);
        for (String s : list) {
            if (!s.toLowerCase().startsWith(arg.toLowerCase())) {
                finalList.remove(s);
            }
        }
        Collections.sort(finalList);
        return finalList;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return dynamicTab(StartComet.argumentList,args[0]);
        if (args.length == 2 && args[0].equals("summon")) return dynamicTab(StartComet.summonList,args[1]);
        if (args.length == 2 && args[0].equals("debug")) return dynamicTab(StartComet.debugList,args[1]);
        return null;
    }
}
