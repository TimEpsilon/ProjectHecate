package com.github.timepsilon.comet.item;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.item.enchants.EnchantRegister;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum CustomItems {

    COSMICBLADE(Material.NETHERITE_SWORD,ChatColor.AQUA + "Cosmic Blade",300,
            ChatColor.LIGHT_PURPLE +"Une épée forgée au coeur d'une comète",ChatColor.LIGHT_PURPLE +"Sa lame tranche les créatures de la nuit","",
            ChatColor.GREEN+""+ ChatColor.ITALIC+"Peut être améliorée en",ChatColor.GREEN+""+ ChatColor.ITALIC+"l'infusant des restes d'un ennemi puissant","",
            ChatColor.GRAY + "Enchanté par :","",ChatColor.BLUE + "7 Cosmic Damage"),
    COSMICBLADE_TRUE(Material.NETHERITE_SWORD,ChatColor.AQUA+"True Cosmic Blade",307,EnchantRegister.COSMIC_BLESSING,7,ChatColor.LIGHT_PURPLE +"La forme finale de la lame du cosmos",
            ChatColor.LIGHT_PURPLE +"Infusée par les 7 péchés capitaux",ChatColor.GREEN+""+ChatColor.ITALIC+"Clic droit pour invoquer un péché",ChatColor.GREEN+""+ChatColor.ITALIC+"Shift + clic droit pour changer de péché","",
            ChatColor.GRAY+"Sins Of The Universe I","",ChatColor.BLUE+"31.5 Cosmic Damage"),
    MCOIN(Material.EMERALD,ChatColor.AQUA + "" + ChatColor.BOLD + "M-Coin",42,null,0,true),
    SOUL_MOB(Material.GUNPOWDER,ChatColor.AQUA+"Lost Soul",10,ChatColor.LIGHT_PURPLE+ "Âme d'une créature de la nuit",ChatColor.LIGHT_PURPLE+ "La comète augmente leur force...",
            ChatColor.GRAY +""+ ChatColor.ITALIC+"Infusez une épée en netherite avec 8 âmes"),
    SOUL_SLOTH(Material.SLIME_BALL,ChatColor.YELLOW+"Slime of Sloth",10,ChatColor.LIGHT_PURPLE+"Âme du King Slime",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Cosmic Blade",""
            ,ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "'Ainsi la paresse est mère.", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Elle a un fils, le vol, et une fille, la faim'"),
    SOUL_GREED(Material.PHANTOM_MEMBRANE,ChatColor.DARK_BLUE+"Wings of Greed",10,ChatColor.LIGHT_PURPLE+"Âme du Phantom Overlord",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Cosmic Blade",""
            ,ChatColor.DARK_PURPLE +""+ ChatColor.ITALIC + "'Icare, volant plus haut,", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "la cire réchauffée par le soleil,", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "tomba dans la mer'"),
    SOUL_WRATH(Material.RABBIT_FOOT,ChatColor.DARK_RED+"Foot of Wrath",10,ChatColor.LIGHT_PURPLE+"Âme de Scarlet Devil",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Cosmic Blade",""
            ,ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "'Déesse, chante-nous la colère d’Achille," , ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "qui valut aux Argiens d’innombrables malheurs'"),
    SOUL_PRIDE(Material.BLACK_DYE,ChatColor.GOLD+"Spirit of Pride",10,ChatColor.LIGHT_PURPLE+"Âme de Shadow",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Cosmic Blade",""
            ,ChatColor.DARK_PURPLE +""+ "'La plupart des Hommes ignorent leur ombre.", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Elle est « les gens », auxquels on prête la cruauté,"
            ,ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "la couardise qu'il serait tragique de se reconnaître.",
            ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC +"Elle est tout ce qui déclenche le dégoût'"),
    SOUL_ENVY(Material.COPPER_INGOT,ChatColor.GREEN+"Eye of Envy",10,ChatColor.LIGHT_PURPLE+"Âme du Demiurge",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Cosmic Blade",""
            ,ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "'Du fond de l’abîme Ialdabaôth engendra la matière,", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "il est le Démiurge'"),
    SOUL_GLUTTONY(Material.HONEYCOMB,ChatColor.BLUE+"Honey of Gluttony",10,ChatColor.LIGHT_PURPLE+"Âme de Queen Bee",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Cosmic Blade",""
            ,ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "'Des millions étaient occupés à satisfaire",ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "la vanité et l’ambition d’autres abeilles,",
            ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC +"qui étaient uniquement employées",ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC +"à consumer les travaux des premières'"),
    SOUL_LUST(Material.GLOWSTONE_DUST,ChatColor.DARK_PURPLE+"Dust of Lust",10,ChatColor.LIGHT_PURPLE+"Âme de Night Fairy",ChatColor.GRAY +""+ ChatColor.ITALIC+"Permet de renforcer la Cosmic Blade",""
            ,ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "'Narcisse fut ébloui par sa propre personne et,", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "visage immobile, reste cloué sur place'"),
    POCKET_BANK(Material.IRON_INGOT,ChatColor.BLUE + "" + ChatColor.BOLD + "Pocket Vault", 12,ChatColor.GRAY+"Permet d'accéder à votre compte",ChatColor.GRAY + "" +ChatColor.ITALIC + "Usage unique")
    ;


    public static NamespacedKey CustomItemKey = new NamespacedKey(ProjectHecate.getPlugin(),"CustomItem");
    public final static String PDAText = ChatColor.GREEN + "" + ChatColor.BOLD + "[S.A.M.] ";
    private final ItemStack item;
    private String name;


    CustomItems(Material material, String name, int cmd, Enchantment ench, int lvl, boolean isMcoin, String... lore) {
        this.name = ChatColor.stripColor(name);
        this.item = new ItemStack(material,1);
        ItemMeta meta = this.item.getItemMeta();
        meta.setCustomModelData(cmd);
        meta.displayName(Component.text(name));
        List<Component> itemlore = new ArrayList<>();
        for (String l : lore) {
            itemlore.add(Component.text(l));
        }
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.lore(itemlore);
        if(isMcoin) {
            meta.getPersistentDataContainer().set(new NamespacedKey("stonkexchange","customitem"), PersistentDataType.STRING,this.name);
        } else {
            meta.getPersistentDataContainer().set(new NamespacedKey(ProjectHecate.getPlugin(),"CustomItem"),PersistentDataType.STRING,this.name);
            if (material.equals(Material.NETHERITE_SWORD)) {
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(),"AttackSpeed", 20, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),"AttackDamage", 7, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }
        }
        this.item.setItemMeta(meta);
        if (ench != null) this.item.addUnsafeEnchantment(ench,lvl);
    }

    CustomItems(Material material, String name, int cmd, String... lore) {
        this(material,name,cmd,null,0,false,lore);
    }

    CustomItems(Material material, String name, int cmd, Enchantment ench, int lvl, String... lore) {
        this(material,name,cmd,ench,lvl,false,lore);
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public String getName() { return name;}

    public boolean compareCustomKey(String key) {
        if (!item.getItemMeta().getPersistentDataContainer().has(CustomItemKey,PersistentDataType.STRING)) return false;
        return item.getItemMeta().getPersistentDataContainer().get(CustomItemKey,PersistentDataType.STRING).equals(key);
    }

    public boolean compareCustomKey(ItemStack item) {
        if (item.getItemMeta() == null) return false;
        String key = item.getItemMeta().getPersistentDataContainer().get(CustomItemKey,PersistentDataType.STRING);
        if (!item.getItemMeta().getPersistentDataContainer().has(CustomItemKey,PersistentDataType.STRING)) return false;
        return item.getItemMeta().getPersistentDataContainer().get(CustomItemKey,PersistentDataType.STRING).equals(key);
    }
}
