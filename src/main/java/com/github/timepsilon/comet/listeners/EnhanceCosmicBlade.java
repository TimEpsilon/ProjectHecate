package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.item.CustomItems;
import com.github.timepsilon.comet.misc.TextManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;

public class EnhanceCosmicBlade implements Listener {

    private static NamespacedKey hasDiscoveredSoul = new NamespacedKey(ProjectHecate.getPlugin(),"HasDiscoveredSoul");

    @EventHandler
    public void onJoin(EntityPickupItemEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        if (!(e.getEntity() instanceof Player p)) return;
        if (!(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey, PersistentDataType.STRING))) return;
        if (!(e.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(CustomItems.SOUL_MOB.getName()))) return;
        if (!p.getPersistentDataContainer().has(hasDiscoveredSoul)) {
            p.getPersistentDataContainer().set(hasDiscoveredSoul,PersistentDataType.INTEGER,1);
            TextManager.sendSamTextToPlayer(p, ChatColor.GREEN + "[INFO] : Vous avez obtenu une âme. Obtenez en 8 ainsi qu'une épée en netherite neuve et faîtes clic droit avec les âmes.");
        }
    }

    @EventHandler
    public void onLostSoulUse(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;
        ItemStack item = e.getItem();
        if (item == null) return;
        if (!(item.getItemMeta().getPersistentDataContainer().has(CustomItems.CustomItemKey,PersistentDataType.STRING))) return;
        if (!(item.getItemMeta().getPersistentDataContainer().get(CustomItems.CustomItemKey,PersistentDataType.STRING).contains(CustomItems.SOUL_MOB.getName()))) return;
        if (item.getAmount() < 8) return;
        Player p = e.getPlayer();
        if (!p.getInventory().contains(Material.NETHERITE_SWORD)) return;

        ItemStack sword = null;
        for (ItemStack i : p.getInventory().getContents()) {
            if (i == null) continue;
            if (!i.getType().equals(Material.NETHERITE_SWORD)) continue;
            if (i.getEnchantments().size() != 0) continue;
            if (((Damageable)i.getItemMeta()).getDamage() != Material.NETHERITE_SWORD.getMaxDurability()) continue;

            sword = i;
            break;
        }

        if (sword == null) return;


        p.getInventory().remove(sword);
        ItemStack souls = CustomItems.SOUL_MOB.getItem();
        //TODO : resync if old item
        souls.setAmount(8);
        p.getInventory().remove(souls);

        p.getInventory().addItem(CustomItems.COSMICBLADE.getItem());
        TextManager.sendSamTextToPlayer(p,ChatColor.GREEN + "[CRAFT] : Cosmic Blade obtenue! Son pouvoir est encore faible. Améliorez-la à l'aide d'âmes de puissants monstres.");
    }

    //TODO : onBossSoulUse
}
