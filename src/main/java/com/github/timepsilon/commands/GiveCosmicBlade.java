package com.github.timepsilon.commands;

import com.github.timepsilon.comet.item.CustomItems;
import org.bukkit.entity.Player;

public class GiveCosmicBlade {

    public static void getItems(Player p) {
        for (CustomItems ci : CustomItems.values()) {
            p.getInventory().addItem(ci.getItem());
        }

    }
}
