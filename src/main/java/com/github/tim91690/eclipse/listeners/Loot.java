package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.CustomItems;
import com.github.tim91690.eclipse.mobs.boss.*;
import com.github.tim91690.eclipse.mobs.boss.Boss;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Loot implements Listener {

    private final static float probaDrop = 0.1f;
    private final static float probaSoul = 0.03f;
    private final static int semiBossBonus = 8; //bonus = [bonus/2 , bonus] + n
    private final static int bossBonus = 30;
    private final static int phantomBonus = 10;
    private final static int fairyBonus = 40;
    private final static int scarletBonus = 20;
    private final static int beeBonus = 25;
    private final static int shadowBonus = 35;
    private final static int demiurgeBonus = 100;

    @EventHandler
    public void mobDeath(EntityDeathEvent event) {
        LivingEntity e = event.getEntity();
        if (!e.getScoreboardTags().contains("Eclipse")) return;
        if (Math.random() < probaDrop) return;
        //10% de chance de drop

        // 1 mcoins par mobs de l'éclipse
        int n = 1;

        // [5-9] mcoins par semi boss
        if(e.getScoreboardTags().contains("SemiBoss")) n += new Random().nextInt(semiBossBonus/2) + semiBossBonus/2;

        // [16 - 31] mcoins par boss
        if(e.getScoreboardTags().contains("Boss")) {

            n += new Random().nextInt(bossBonus/2) + bossBonus/2;
            Boss boss = Boss.getBossInList(e);

            List<Player> proxPlayer = boss.getBossbar().getPlayers();

            if(boss instanceof KingSlime) {
                giveItemToPlayers(proxPlayer,CustomItems.SOUL_SLOTH.getItem(),"moonblade_bless1");
            }

            // [26-41] mcoins par phantom
            if(boss instanceof PhantomOverlord) {
                n += phantomBonus;
                giveItemToPlayers(proxPlayer,CustomItems.SOUL_GREED.getItem(),"moonblade_bless2");
            }

            // [56-71] mcoins par fairy
            if(boss instanceof NightFairy) {
                n += fairyBonus;
                giveItemToPlayers(proxPlayer,CustomItems.SOUL_LUST.getItem(),"moonblade_bless3");
            }

            // [36-51] mcoins par demon
            if(boss instanceof ScarletRabbit) {
                n += scarletBonus;
                giveItemToPlayers(proxPlayer,CustomItems.SOUL_WRATH.getItem(),"moonblade_bless4");
            }

            // [41-56] mcoins par bee
            if(boss instanceof QueenBee) {
                n += beeBonus;
                giveItemToPlayers(proxPlayer,CustomItems.SOUL_GLUTTONY.getItem(),"moonblade_bless5");
            }

            // [51-76] mcoins par shadow
            if(boss instanceof Shadows) {
                n += shadowBonus;
                giveItemToPlayers(proxPlayer,CustomItems.SOUL_PRIDE.getItem(),"moonblade_bless6");
            }

            // [116-131] mcoins par shadow
            if(boss instanceof Demiurge) {
                n += demiurgeBonus;
                giveItemToPlayers(proxPlayer,CustomItems.SOUL_ENVY.getItem(),"moonblade_bless7");
            }
        }

        int i = 1;
        ItemStack mcoin = CustomItems.MCOIN.getItem();
        for (int s =1; s <= n; s += i) {
            mcoin.setAmount(i);
            Item item = e.getLocation().getWorld().dropItem(e.getLocation(), mcoin);
            item.setVelocity(Vector.getRandom().multiply(3));
            i = (int)(Math.random()*3+1);
        }


        if (Math.random() < probaSoul) e.getWorld().dropItem(e.getLocation(),CustomItems.SOUL_MOB.getItem());
    }


    private void giveItemToPlayers(List<Player> proxPlayer,ItemStack loot,String enchKey) {
        for (Player p : proxPlayer) {
            HashMap<Integer,ItemStack> remaining =  p.getInventory().addItem(loot);
            for (ItemStack item : remaining.values()) {
                p.getWorld().dropItem(p.getLocation(),item);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1 ,1);
                p.sendMessage(Component.text(CustomItems.PDAText + loot.getItemMeta().displayName() + ChatColor.GOLD + " obtenue !" + ChatColor.GREEN +
                        "Une âme vient d'être capturée! \n" +
                        "Son énergie semble pouvoir être exploitée..."));
            }
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),enchKey))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),enchKey));
        }
    }
}
