package com.github.tim91690.eclipse.mobs.boss;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Boss {
    protected static final List<Boss> bossList = new ArrayList<>();
    protected Entity entity;
    protected final BossBar bossbar;
    private double maxHealth;
    protected String name;

    /**Classe Boss
     * Décrit les mobs boss apparaissant à chaque étape
     * @param e l'entité boss
     * @param health la vie max
     */
    public Boss(Entity e, int health,String name,BarColor barcolor) {
        this.bossbar = Bukkit.createBossBar(name, barcolor, BarStyle.SOLID, BarFlag.CREATE_FOG,BarFlag.DARKEN_SKY);
        this.maxHealth = health;
        this.entity = e;
        this.name = name;

        //Ne supprime pas l'entité quand trop loin
        this.entity.setPersistent(true);
        ((LivingEntity)this.entity).setRemoveWhenFarAway(false);

        this.entity.addScoreboardTag("Eclipse");
        this.entity.addScoreboardTag("Boss");

        //vie
        ((LivingEntity) this.entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        ((LivingEntity) this.entity).setHealth(health);

        bossList.add(this);

        this.bossbar.setProgress(((LivingEntity) this.entity).getHealth() / health);
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

    public void attack(Player p) {
        //Utiliser un @override dans la sous classe du boss pour définir ses attaques
    }
}
