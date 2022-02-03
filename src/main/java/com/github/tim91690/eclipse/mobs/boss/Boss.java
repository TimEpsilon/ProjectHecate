package com.github.tim91690.eclipse.mobs.boss;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class Boss {
    protected static final List<Boss> bossList = new ArrayList<>();
    protected LivingEntity entity;
    protected final BossBar bossbar;
    private final double maxHealth;
    protected String name;

    /**Classe Boss
     * Décrit les mobs boss apparaissant à chaque étape
     * @param e l'entité boss
     * @param health la vie max
     */
    public Boss(Entity e, int health,String name,BarColor barcolor) {
        this.bossbar = Bukkit.createBossBar(name, barcolor, BarStyle.SOLID, BarFlag.CREATE_FOG,BarFlag.DARKEN_SKY);
        this.maxHealth = health;
        this.entity = (LivingEntity) e;
        this.name = name;

        //Ne supprime pas l'entité quand trop loin
        this.entity.setPersistent(true);
        this.entity.setRemoveWhenFarAway(false);

        this.entity.addScoreboardTag("Eclipse");
        this.entity.addScoreboardTag("Boss");

        Team scarlet;
        //team
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet") == null) {
            scarlet = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("Scarlet");
            scarlet.color(NamedTextColor.DARK_RED);
        }
        else scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");
        scarlet.addEntry(this.getEntity().getUniqueId().toString());

        //vie
        this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        this.entity.setHealth(health);

        bossList.add(this);

        this.bossbar.setProgress(this.entity.getHealth() / health);
    }

    //retire le boss de la liste et supprime sa bossbar
    public void death() {
        bossList.remove(this);
        this.bossbar.removeAll();
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
