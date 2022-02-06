package com.github.tim91690.eclipse.item;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.enchants.EnchantRegister;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public enum CustomItems {

    MOONBLADE(Material.NETHERITE_SWORD,1,ChatColor.AQUA + "Moon Blade",300,
            ChatColor.LIGHT_PURPLE +"Une épée forgée à partir de roches lunaires",ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit",
            ChatColor.GREEN+""+ ChatColor.ITALIC+"Peut être améliorée en",ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant"),
    MOONBLADE_LVL1(Material.NETHERITE_SWORD,1,ChatColor.AQUA + "Moon Blade",301,EnchantRegister.MOON_BLESSING,1,
            ChatColor.LIGHT_PURPLE +"Une épée forgée à partir de roches lunaires",ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit",
            ChatColor.GREEN+""+ ChatColor.ITALIC+"Peut être améliorée en",ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant","",ChatColor.GRAY+"Moon's Blessing I"),
    MOONBLADE_LVL2(Material.NETHERITE_SWORD,1,ChatColor.AQUA + "Moon Blade",302,EnchantRegister.MOON_BLESSING,2,
            ChatColor.LIGHT_PURPLE +"Une épée forgée à partir de roches lunaires",ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit",
            ChatColor.GREEN+""+ ChatColor.ITALIC+"Peut être améliorée en",ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant","",ChatColor.GRAY+"Moon's Blessing II"),
    MOONBLADE_LVL3(Material.NETHERITE_SWORD,1,ChatColor.AQUA + "Moon Blade",303,EnchantRegister.MOON_BLESSING,3,
            ChatColor.LIGHT_PURPLE +"Une épée forgée à partir de roches lunaires",ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit",
            ChatColor.GREEN+""+ ChatColor.ITALIC+"Peut être améliorée en",ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant","",ChatColor.GRAY+"Moon's Blessing III"),
    MOONBLADE_LVL4(Material.NETHERITE_SWORD,1,ChatColor.AQUA + "Moon Blade",304,EnchantRegister.MOON_BLESSING,4,
            ChatColor.LIGHT_PURPLE +"Une épée forgée à partir de roches lunaires",ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit",
            ChatColor.GREEN+""+ ChatColor.ITALIC+"Peut être améliorée en",ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant","",ChatColor.GRAY+"Moon's Blessing IV"),
    MOONBLADE_LVL5(Material.NETHERITE_SWORD,1,ChatColor.AQUA + "Moon Blade",305,EnchantRegister.MOON_BLESSING,5,
            ChatColor.LIGHT_PURPLE +"Une épée forgée à partir de roches lunaires",ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit",
            ChatColor.GREEN+""+ ChatColor.ITALIC+"Peut être améliorée en",ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant","",ChatColor.GRAY+"Moon's Blessing V"),
    MOONBLADE_LVL6(Material.NETHERITE_SWORD,1,ChatColor.AQUA + "Moon Blade",306,EnchantRegister.MOON_BLESSING,6,
            ChatColor.LIGHT_PURPLE +"Une épée forgée à partir de roches lunaires",ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit",
            ChatColor.GREEN+""+ ChatColor.ITALIC+"Peut être améliorée en",ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant","",ChatColor.GRAY+"Moon's Blessing VI"),
    MOONBLADE_TRUE(Material.NETHERITE_SWORD,1,ChatColor.AQUA+"True Moon Blade",307,EnchantRegister.MOON_BLESSING,7,ChatColor.LIGHT_PURPLE +"La forme finale de la lame la Lune",
            ChatColor.LIGHT_PURPLE +"Infusée par les 7 péchés capitaux",ChatColor.GREEN+""+ChatColor.ITALIC+"Clic droit pour invoquer un péché",ChatColor.GREEN+""+ChatColor.ITALIC+"Shift + clic droit pour changer de péché"),
    MCOIN(Material.EMERALD,1,ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin",42,null,0,true),
    SOUL_MOB(Material.GUNPOWDER,1,ChatColor.AQUA+"Moon Soul",1,ChatColor.LIGHT_PURPLE+ "Âme d'une créature de la nuit",ChatColor.LIGHT_PURPLE+ "L'éclipse leur permet de prendre forme",
            ChatColor.GRAY +""+ ChatColor.ITALIC+"Infusez une épée en netherite avec 8 âmes",ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant"),
    SOUL_SLOTH(Material.SLIME_BALL,1,ChatColor.YELLOW+"Slime of Sloth",10,ChatColor.LIGHT_PURPLE+"Âme du King Slime",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Moon Blade"),
    SOUL_GREED(Material.PHANTOM_MEMBRANE,1,ChatColor.DARK_BLUE+"Wings of Greed",10,ChatColor.LIGHT_PURPLE+"Âme du Phantom Overlord",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Moon Blade"),
    SOUL_WRATH(Material.RABBIT_FOOT,1,ChatColor.DARK_RED+"Foot of Wrath",10,ChatColor.LIGHT_PURPLE+"Âme de Scarlet Devil",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Moon Blade"),
    SOUL_PRIDE(Material.BLACK_DYE,1,ChatColor.GOLD+"Spirit of Pride",10,ChatColor.LIGHT_PURPLE+"Âme de Shadow",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Moon Blade"),
    SOUL_ENVY(Material.COPPER_INGOT,1,ChatColor.GREEN+"Eye of Envy",10,ChatColor.LIGHT_PURPLE+"Âme du Demiurge",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Moon Blade"),
    SOUL_GLUTTONY(Material.HONEYCOMB,1,ChatColor.BLUE+"Honey of Gluttony",10,ChatColor.LIGHT_PURPLE+"Âme de Queen Bee",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Moon Blade"),
    SOUL_LUST(Material.GLOWSTONE_DUST,1,ChatColor.DARK_PURPLE+"Dust of Lust",10,ChatColor.LIGHT_PURPLE+"Âme de Night Fairy",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Moon Blade");


    public static NamespacedKey CustomItemKey = new NamespacedKey(EventManager.getPlugin(),"CustomItem");
    public final static String PDAText = ChatColor.GREEN + "" + ChatColor.BOLD + "[S.A.M.] ";
    private ItemStack item;
    private String name;


    CustomItems(Material material, int n, String name, int cmd, Enchantment ench, int lvl, boolean isMcoin, String... lore) {
        this.name = ChatColor.stripColor(name);
        this.item = new ItemStack(material,n);
        ItemMeta meta = this.item.getItemMeta();
        meta.setCustomModelData(cmd);
        List<Component> itemlore = new ArrayList<>();
        for (String l : lore) {
            itemlore.add(Component.text(l));
        }
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.lore(itemlore);
        if(isMcoin) {
            meta.getPersistentDataContainer().set(new NamespacedKey("stonksexchange","customitem"), PersistentDataType.STRING,this.name);
        } else {
            meta.getPersistentDataContainer().set(new NamespacedKey(EventManager.getPlugin(),"CustomItem"),PersistentDataType.STRING,this.name);
        }
        this.item.setItemMeta(meta);
        if (ench != null) this.item.addUnsafeEnchantment(ench,lvl);
    }

    CustomItems(Material material, int n, String name, int cmd, String... lore) {
        this(material,n,name,cmd,null,0,false,lore);
    }

    CustomItems(Material material, int n, String name, int cmd, Enchantment ench, int lvl, String... lore) {
        this(material,n,name,cmd,ench,lvl,false,lore);
    }

    public ItemStack getItem() {
        return this.item;
    }
}
