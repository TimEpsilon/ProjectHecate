package com.github.tim91690.eclipse.item;

import com.github.tim91690.eclipse.item.enchants.EnchantRegister;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomItems {

    // Définition mcoin
    public static ItemStack mcoin(int n) {
        ItemStack mcoin = new ItemStack(Material.EMERALD,n);
        ItemMeta Mmeta = mcoin.getItemMeta();
        Mmeta.displayName(Component.text(ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin"));
        Mmeta.setCustomModelData(42);
        mcoin.setItemMeta(Mmeta);
        return mcoin;
    }

    public static ItemStack getMobSoul() {
        ItemStack soul = new ItemStack(Material.GUNPOWDER);
        ItemMeta meta = soul.getItemMeta();
        meta.setCustomModelData(1);
        meta.displayName(Component.text(ChatColor.AQUA+"Moon Soul"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.LIGHT_PURPLE+ "Âme d'une créature de la nuit"));
        lore.add(Component.text(ChatColor.LIGHT_PURPLE+ "L'éclipse leur permet de prendre forme"));
        lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC+"Infusez une épée en netherite avec 8 âmes"));
        lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC+"pour obtenir un métal tranchant"));
        meta.lore(lore);
        soul.setItemMeta(meta);
        return soul;
    }

    public static ItemStack getMoonBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();
        meta.displayName(Component.text(ChatColor.AQUA + "Moon Blade"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.LIGHT_PURPLE +"Une épée forgée à partir de roches lunaires"));
        lore.add(Component.text(ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit"));
        lore.add(Component.text(ChatColor.GREEN+""+ ChatColor.ITALIC+"Peut être améliorée en"));
        lore.add(Component.text(ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant"));
        meta.lore(lore);
        meta.setCustomModelData(1);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        blade.setItemMeta(meta);
        return blade;
    }

    public static ItemStack getUltimateMoonBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = blade.getItemMeta();
        meta.displayName(Component.text(ChatColor.AQUA+"True Moon Blade"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.LIGHT_PURPLE +"La forme finale de la lame la Lune"));
        lore.add(Component.text(ChatColor.LIGHT_PURPLE +"Infusée par les 7 péchés capitaux"));
        lore.add(Component.text(ChatColor.GREEN+""+ChatColor.ITALIC+"Clic droit pour invoquer un péché"));
        lore.add(Component.text(ChatColor.GREEN+""+ChatColor.ITALIC+"Shift + clic droit pour changer de péché"));
        lore.add(Component.text(""));
        lore.add(Component.text(ChatColor.GRAY+"Sins of the Moon I"));
        meta.lore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setCustomModelData(8);
        blade.setItemMeta(meta);
        blade.addUnsafeEnchantment(EnchantRegister.MOON_BLESSING,7);
        return blade;
    }

    public static ItemStack getSlothSoul() {
        return customSoul(Material.SLIME_BALL,ChatColor.YELLOW+"Slime of Sloth","Âme du King Slime");
    }

    public static ItemStack getGreedSoul() {
        return customSoul(Material.PHANTOM_MEMBRANE,ChatColor.DARK_BLUE+"Wings of Greed","Âme du Phantom Overlord");
    }

    public static ItemStack getWrathSoul() {
        return customSoul(Material.RABBIT_FOOT,ChatColor.DARK_RED+"Foot of Wrath","Âme de Scarlet Devil");
    }

    public static ItemStack getPrideSoul() {
        return customSoul(Material.BLACK_DYE,ChatColor.GOLD+"Spirit of Pride","Âme de Shadow");
    }

    public static ItemStack getEnvySoul() {
        return customSoul(Material.COPPER_INGOT,ChatColor.GREEN+"Eye of Envy","Âme du Demiurge");
    }

    public static ItemStack getGluttonySoul() {
        return customSoul(Material.HONEYCOMB,ChatColor.BLUE+"Honey of Gluttony","Âme de Queen Bee");
    }

    public static ItemStack getLustSoul() {
        return customSoul(Material.GLOWSTONE_DUST,ChatColor.DARK_PURPLE+"Dust of Lust","Âme de Night Fairy");
    }

    private static ItemStack customSoul(Material material,String name,String lore1) {
        ItemStack soul = new ItemStack(material);
        ItemMeta meta = soul.getItemMeta();
        meta.setCustomModelData(1);
        meta.displayName(Component.text(name));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.LIGHT_PURPLE+lore1));
        lore.add(Component.text(ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Moon Blade"));
        meta.lore(lore);
        soul.setItemMeta(meta);
        return soul;
    }
}
