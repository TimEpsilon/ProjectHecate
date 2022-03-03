package com.github.tim91690.eclipse.mobs.boss;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.CustomItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class Boss {
    protected static final List<Boss> bossList = new ArrayList<>();
    protected LivingEntity entity;
    protected final BossBar bossbar;
    private final double maxHealth;
    private final ItemStack soul;
    private final int lvl;
    private final int bonus;
    protected final String name;
    private List<Player> proxPlayer;
    private int bossbarTask;

    private final static int bossBonus = 30;

    private static Random random = new Random();

    /** Crée un boss
     *
     * @param e entité du boss
     * @param health max hp
     * @param name le nom en couleur
     * @param barcolor couleur de la barre
     * @param soul la soul a drop à la mort
     * @param lvl niveau de craft de la moonblade
     * @param bonus mcoin à drop en bonus
     */
    public Boss(Entity e, int health,String name,BarColor barcolor,ItemStack soul,int lvl,int bonus) {
        this.bossbar = Bukkit.createBossBar(name, barcolor, BarStyle.SOLID, BarFlag.CREATE_FOG,BarFlag.DARKEN_SKY);
        this.maxHealth = health;
        this.soul = soul;
        this.lvl = lvl;
        this.entity = (LivingEntity) e;
        this.name = name;
        this.bonus = bonus;
        this.proxPlayer = new ArrayList<>();

        //Ne supprime pas l'entité quand trop loin
        this.entity.setPersistent(true);
        this.entity.setRemoveWhenFarAway(false);

        //Tags
        this.entity.addScoreboardTag("Eclipse");
        this.entity.addScoreboardTag("Boss");

        //team
        Team scarlet;
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet") == null) {
            scarlet = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("Scarlet");
            scarlet.color(NamedTextColor.DARK_RED);
        } else {
            scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");
        }
        scarlet.addEntry(this.getEntity().getUniqueId().toString());

        //vie
        this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        this.entity.setHealth(health);

        bossList.add(this);

        this.bossbar.setProgress(this.entity.getHealth() / health);
        CustomBossBar();
    }

    //retire le boss de la liste et supprime sa bossbar + give récompense aux joueurs alentours
    public void death() {
        List<Player> proxPlayer = this.bossbar.getPlayers();
        giveItemToPlayers(proxPlayer);

        int n = this.bonus + random.nextInt(bossBonus/2) + bossBonus/2;
        int i = 1;
        ItemStack mcoin;
        for (int s =1; s <= n; s += i) {
            mcoin = CustomItems.MCOIN.getItem();
            mcoin.setAmount(i);
            Item item = this.entity.getLocation().getWorld().dropItem(this.entity.getLocation().add(Vector.getRandom().subtract(new Vector(0.5,0,0.5)).multiply(3)), mcoin);
            item.setVelocity(Vector.getRandom().subtract(new Vector(0.5,0,0.5)).multiply(0.3));
            i = random.nextInt(3)+1;
        }

        bossList.remove(this);
        this.bossbar.removeAll();
        Bukkit.getScheduler().cancelTask(this.bossbarTask);
    }

    private void giveItemToPlayers(List<Player> proxPlayer) {
        for (Player p : proxPlayer) {
            HashMap<Integer,ItemStack> remaining =  p.getInventory().addItem(this.soul);
            p.sendMessage(Component.text(CustomItems.PDAText + this.soul.getItemMeta().getDisplayName() + ChatColor.GOLD + " obtenue !\n" + ChatColor.GREEN +
                    "Une âme vient d'être capturée! \n" +
                    "Son énergie semble pouvoir être exploitée..."));
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1 ,1);
            for (ItemStack item : remaining.values()) {
                p.getWorld().dropItem(p.getLocation(),item);
            }
            if (!p.hasDiscoveredRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless"+this.lvl))) p.discoverRecipe(new NamespacedKey(EventManager.getPlugin(),"moonblade_bless"+this.lvl));
        }
    }

    private void CustomBossBar() {
        this.bossbarTask = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(), () -> {

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().distance(this.entity.getLocation()) < 40) {
                    //ajoute le joueur si il est aux alentours
                    this.getBossbar().addPlayer(p);
                    this.proxPlayer.add(p);
                }
                //le retire si il n'y est pas
                else this.getBossbar().removePlayer(p);
            }
            if (this.proxPlayer.size() != 0) this.attack(this.proxPlayer);
            this.proxPlayer.clear();
        }, 0, 100).getTaskId();
    }

    public static List<Boss> getBossList() {
        return bossList;
    }

    public Entity getEntity() {
        return entity;
    }

    public BossBar getBossbar() {
        return bossbar;
    }

    public static Boss getBossInList(Entity entity) {
        for (Boss boss : bossList) {
            if (boss.getEntity().equals(entity)) return boss;
        }
        return null;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public abstract void attack(List<Player> proxPlayer);
}
