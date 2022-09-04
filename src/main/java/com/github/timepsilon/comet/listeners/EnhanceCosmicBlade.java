package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.item.CustomItems;
import com.github.timepsilon.comet.item.enchants.EnchantRegister;
import com.github.timepsilon.comet.misc.TextManager;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EnhanceCosmicBlade implements Listener {

    private static NamespacedKey hasDiscoveredSoul = new NamespacedKey(ProjectHecate.getPlugin(),"HasDiscoveredSoul");
    private static NamespacedKey appliedSouls = new NamespacedKey(ProjectHecate.getPlugin(),"appliedSouls");
    private final static ArrayList<CustomItems> soulsBoss = new ArrayList<>(List.of(
            CustomItems.SOUL_SLOTH,
            CustomItems.SOUL_GREED,
            CustomItems.SOUL_LUST,
            CustomItems.SOUL_WRATH,
            CustomItems.SOUL_GLUTTONY,
            CustomItems.SOUL_PRIDE
            ));

    @EventHandler
    public void onSoulPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (!(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey, PersistentDataType.STRING))) return;
        if (!(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(CustomItems.SOUL_MOB.getName()))) return;
        if (!p.getPersistentDataContainer().has(hasDiscoveredSoul)) {
            p.getPersistentDataContainer().set(hasDiscoveredSoul,PersistentDataType.INTEGER,1);
            TextManager.sendSamTextToPlayer(p, ChatColor.GREEN + "[INFO] : Vous avez obtenu une âme. Obtenez en 8 ainsi qu'une épée en netherite neuve et faîtes clic droit avec.");
        }
    }

    @EventHandler
    public void onLostSoulUse(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;
        ItemStack item = e.getItem();
        if (item == null) return;
        if (item.getItemMeta() == null) return;
        if (!(item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey,PersistentDataType.STRING))) return;
        if (!(item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(CustomItems.SOUL_MOB.getName()))) return;
        if (item.getAmount() < 8) return;
        Player p = e.getPlayer();
        if (!p.getInventory().contains(Material.NETHERITE_SWORD)) return;

        ItemStack sword = null;
        for (ItemStack i : p.getInventory().getContents()) {
            if (i == null) continue;
            if (i.getType().equals(Material.NETHERITE_SWORD)) {
                if (i.getEnchantments().size() != 0) continue;
                if (i.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey,PersistentDataType.STRING)) return;
                if (((Damageable)i.getItemMeta()).getDamage() != 0) continue;

                sword = i;
            }
        }

        if (sword == null) return;


        p.getInventory().removeItem(sword);
        ItemStack souls = CustomItems.SOUL_MOB.getItem();
        souls.setAmount(8);
        p.getInventory().removeItem(souls);

        p.getInventory().addItem(CustomItems.COSMICBLADE.getItem());
        TextManager.sendSamTextToPlayer(p,ChatColor.GREEN + "[CRAFT] : Cosmic Blade obtenue! Son pouvoir est encore faible. Améliorez-la à l'aide d'âmes de puissants monstres.");
        p.playSound(p.getLocation(),Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS,1,1.2f);
        p.spawnParticle(Particle.ITEM_CRACK,p.getLocation(),60,0.5,1,0.5,0,item);
    }

    @EventHandler
    public void onBossSoulUse(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;
        if (e.getItem() == null) return;
        ItemStack item = e.getItem().clone();

        if (item.getItemMeta() == null) return;
        if (!(item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey,PersistentDataType.STRING))) return;
        String key = item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING);
        if (CustomItems.SOUL_ENVY.compareCustomKey(key)) return;

        if (soulsBoss.stream().noneMatch(CustomItems -> CustomItems.compareCustomKey(key))) return;

        Player p = e.getPlayer();

        ItemStack sword = null;
        ItemStack souls = null;
        for (ItemStack i : p.getInventory().getContents()) {
            if (i == null) continue;
            if (!i.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey,PersistentDataType.STRING)) continue;

            if (CustomItems.SOUL_MOB.compareCustomKey(i.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING))) {
                if (i.getAmount() < 5) continue;
                souls = i.clone();
            }

            if (CustomItems.COSMICBLADE.compareCustomKey(i.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING))) {
                sword = i;
            }
        }

        if (sword == null || souls == null) return;

        ItemMeta meta = sword.getItemMeta();
        List<Component> lore = meta.lore();
        if (lore.contains(item.displayName())) return;

        souls.setAmount(5);
        p.getInventory().removeItem(souls);

        item.setAmount(1);
        p.getInventory().removeItem(item);

        int lvl = sword.getEnchantmentLevel(EnchantRegister.COSMIC_BLESSING);
        lore.add(7,item.displayName());
        lore.set(lore.size()-1,Component.text(ChatColor.BLUE + "+" + Math.round((7 * (lvl+3)/2f)*10f)/10f + " Cosmic Damage"));
        meta.lore(lore);
        meta.setCustomModelData(meta.getCustomModelData()+1);
        sword.setItemMeta(meta);
        sword.removeEnchantment(EnchantRegister.COSMIC_BLESSING);
        sword.addUnsafeEnchantment(EnchantRegister.COSMIC_BLESSING,lvl+1);


        TextManager.sendSamTextToPlayer(p,ChatColor.GREEN + "[CRAFT] : Cosmic Blade améliorée!");
        p.playSound(p.getLocation(),Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS,1,1.2f);
        p.spawnParticle(Particle.ITEM_CRACK,p.getLocation(),120,0.5,1,0.5,0,item);
    }

    @EventHandler
    public void onFinalSoulUse(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;
        if (e.getItem() == null) return;
        ItemStack item = e.getItem().clone();

        if (item.getItemMeta() == null) return;
        if (!(item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey,PersistentDataType.STRING))) return;
        String key = item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING);
        if (!CustomItems.SOUL_ENVY.compareCustomKey(key)) return;

        Player p = e.getPlayer();

        ItemStack sword = null;
        ItemStack souls = null;
        for (ItemStack i : p.getInventory().getContents()) {
            if (i == null) continue;
            if (!i.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey,PersistentDataType.STRING)) continue;

            if (CustomItems.SOUL_MOB.compareCustomKey(i.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING))) {
                if (i.getAmount() < 5) continue;
                souls = i.clone();
            }

            if (CustomItems.COSMICBLADE.compareCustomKey(i.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING))) {
                sword = i;
            }
        }

        if (sword == null || souls == null) return;

        ItemMeta meta = sword.getItemMeta();
        List<Component> lore = meta.lore();
        if (!soulsBoss.stream().allMatch(CustomItems -> {
            assert lore != null;
            return lore.contains(CustomItems.getItem().displayName());
        })) return;

        souls.setAmount(5);
        p.getInventory().removeItem(souls);

        item.setAmount(1);
        p.getInventory().removeItem(item);
        p.getInventory().removeItem(sword);

        p.getInventory().addItem(CustomItems.COSMICBLADE_TRUE.getItem());


        TextManager.sendSamTextToPlayer(p,ChatColor.GREEN + "[CRAFT] : True Cosmic Blade obtenue! Renforcée par les péchés capitaux, utilisez ses pouvoirs dévastateurs pour vaincre les créatures de la Comète.");
        p.playSound(p.getLocation(),Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS,1,1.2f);
        p.spawnParticle(Particle.ITEM_CRACK,p.getLocation(),120,0.5,1,0.5,0,item);
    }
}
