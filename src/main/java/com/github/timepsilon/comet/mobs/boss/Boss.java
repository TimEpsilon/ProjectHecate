package com.github.timepsilon.comet.mobs.boss;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.commands.Waypoint;
import com.github.timepsilon.comet.item.CustomItems;
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

import java.util.*;

public abstract class Boss {
    protected static final List<Boss> bossList = new ArrayList<>();
    protected LivingEntity entity;
    protected final BossBar bossbar;
    private final double maxHealth;
    private final ItemStack soul;
    private final int lvl;
    private final int bonus;
    protected final String name;
    public List<Player> proxPlayer;
    private int bossbarTask;
    private int attackTask;

    private final static int bossBonus = 20;

    private static final Random random = new Random();


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
        bossbar = Bukkit.createBossBar(name, barcolor, BarStyle.SOLID, BarFlag.CREATE_FOG,BarFlag.DARKEN_SKY);
        maxHealth = health;
        this.soul = soul;
        this.lvl = lvl;
        entity = (LivingEntity) e;
        this.name = name;
        this.bonus = bonus;
        proxPlayer = new ArrayList<>();

        //Ne supprime pas l'entité quand trop loin
        this.entity.setPersistent(true);
        this.entity.setRemoveWhenFarAway(false);

        //Tags
        this.entity.addScoreboardTag("Comet");
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
        AttackLoop();
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
            Item item = entity.getWorld().dropItem(entity.getLocation().add(Vector.getRandom().subtract(new Vector(0.5,0,0.5)).multiply(6)), mcoin);
            item.setVelocity(Vector.getRandom().subtract(new Vector(0.5,0,0.5)).multiply(0.3));
            i = random.nextInt(3)+1;
        }

        bossList.remove(this);
        bossbar.removeAll();
        this.proxPlayer.clear();
        Bukkit.getScheduler().cancelTask(bossbarTask);
        Bukkit.getScheduler().cancelTask(attackTask);
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
            if (!p.hasDiscoveredRecipe(new NamespacedKey(ProjectHecate.getPlugin(),"cosmicblade_bless"+this.lvl))) p.discoverRecipe(new NamespacedKey(ProjectHecate.getPlugin(),"cosmicblade_bless"+this.lvl));
        }
    }

    private void CustomBossBar() {
        this.bossbarTask = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {

            List<Player> prox = getProxPlayer(80);
            bossbar.removeAll();
            prox.forEach(bossbar::addPlayer);

        }, 0, 40).getTaskId();
    }

    private void AttackLoop() {
        attackTask = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
            proxPlayer.clear();
            proxPlayer = getProxPlayer(60);
            if (!proxPlayer.isEmpty()) {
                attack(proxPlayer);
            }
        },0,110).getTaskId();
    }

    public static void sendWaypoint(String s) {
        Waypoint.PlayersHaveMaps.forEach(Player -> Player.sendMessage(s));
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

    public static Boss getBossInList(UUID uuid) {
        for (Boss boss : bossList) {
            if (boss.getEntity().getUniqueId().equals(uuid)) return boss;
        }
        return null;
    }

    public List<Player> getProxPlayer(int dist) {
        List<Player> prox = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getWorld().equals(entity.getWorld())) continue;
            if (p.getLocation().distance(this.entity.getLocation()) < dist) prox.add(p);
        }
        return prox;
    }

    public String getName() {
        return name;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    protected boolean isSuffocating(Location loc,int size) {
        for (int x = -size; x <= size; x++) {
            for (int y = -size; y <= size; y++) {
                for (int z = -size; z <= size; z++) {
                    if (loc.clone().add(new Vector(x,y,z)).getBlock().isSolid()) return true;
                }
            }
        }
        return false;
    }

    public void resync() {
        if (entity.isValid()) return;
        for (Entity e : entity.getNearbyEntities(30,30,30)) {
            if (e.getUniqueId().equals(entity.getUniqueId())) {
                entity = (LivingEntity) e;
                return;
            }
        }
    }

    public abstract void attack(List<Player> proxPlayer);
}
