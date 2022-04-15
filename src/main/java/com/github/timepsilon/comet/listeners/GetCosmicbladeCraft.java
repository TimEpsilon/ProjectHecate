package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.EventManager;
import com.github.timepsilon.comet.item.CustomItems;
import com.github.timepsilon.comet.misc.TextManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class GetCosmicbladeCraft implements Listener {
    @EventHandler
    public void onJoin(EntityPickupItemEvent e) {
        if (!EventManager.isRunningEvent) return;
        if (!(e.getEntity() instanceof Player p)) return;
        if (!(e.getItem().getItemStack().isSimilar(CustomItems.SOUL_MOB.getItem()))) return;
        if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade"))) {
            p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"cosmicblade"));
            TextManager.sendSamTextToPlayer(p,"[INFO] : Vous avez obtenu une âme. Obtenez en 8 ainsi qu'une épée en netherite et réalisez le craft suivant :");
            p.sendMessage("┌───┬───┬───┐");
            p.sendMessage("| * | * | * |");
            p.sendMessage("├───┼───┼───┤");
            p.sendMessage("| * | ⚔ | * |");
            p.sendMessage("├───┼───┼───┤");
            p.sendMessage("| * | * | * |");
            p.sendMessage("└───┴───┴───┘");
        }
    }
}
