package com.github.tim91690.eclipse.item.moonblade;

import com.github.tim91690.eclipse.item.moonblade.enchants.EnchantRegister;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MoonBlade {

    ItemStack blade;

    public MoonBlade() {
        this.blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = this.blade.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Moon Blade");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.LIGHT_PURPLE +"Une épée forgée à partir de roches lunaires");
        lore.add(ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit");
        lore.add(ChatColor.GRAY+""+ ChatColor.ITALIC+"Peut être améliorée en");
        lore.add(ChatColor.GRAY+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant");
        meta.setLore(lore);
        meta.setCustomModelData(1);
        meta.setUnbreakable(true);
        this.blade.setItemMeta(meta);
        this.blade.addUnsafeEnchantment(EnchantRegister.MOON_BLESSING,1);
    }

    public void giveItem(Player p) {
        p.getInventory().addItem(this.blade);
    }

    public ItemStack getBlade() {
        return this.blade;
    }
}
