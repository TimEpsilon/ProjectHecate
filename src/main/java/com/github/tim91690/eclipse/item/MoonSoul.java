package com.github.tim91690.eclipse.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MoonSoul {

    public static ItemStack getSoul(int n) {
        ItemStack soul = new ItemStack(Material.GUNPOWDER,n);
        ItemMeta meta = soul.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName(ChatColor.AQUA+"Moon Soul");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.LIGHT_PURPLE+ "Âme d'une créature de la nuit");
        lore.add(ChatColor.LIGHT_PURPLE+ "L'éclipse leur permet de prendre forme");
        lore.add(ChatColor.GRAY +""+ ChatColor.ITALIC+"Infusez une épée en netherite avec 8 âmes");
        lore.add(ChatColor.GRAY +""+ ChatColor.ITALIC+"pour obtenir un métal tranchant");
        meta.setLore(lore);
        soul.setItemMeta(meta);
        return soul;
    }
}
