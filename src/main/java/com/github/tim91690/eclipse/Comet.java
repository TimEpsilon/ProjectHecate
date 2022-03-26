package com.github.tim91690.eclipse;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.events.Meteor;
import com.github.tim91690.eclipse.mobs.boss.*;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Comet {

    private int phase;
    private final int timeToLvl5 = 108000; //90min
    private final World world;
    private int tickTask;
    private int timeSkipTask;
    private final AtomicInteger timer = new AtomicInteger();
    private static final Random random = new Random();
    private static final float probaBoss = 0.002f;
    private static final float probaMeteor = 0.001f;
    private static final float probaBee = 0.003f;

    public Comet() {
        this.phase = 0;
        this.world =  getMainWorld();
    }

    private World getMainWorld() {
        World world = null;
        for (World w : Bukkit.getWorlds()) if(w.getEnvironment().compareTo(World.Environment.NORMAL) == 0) world = w;
        assert world != null;
        return world;
    }

    public void startEvent() {
        EventManager.isRunningEvent = true;

        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
        world.setGameRule(GameRule.DO_FIRE_TICK,false);
        world.setGameRule(GameRule.DO_INSOMNIA,true);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE,false);
        world.setGameRule(GameRule.KEEP_INVENTORY,true);
        world.setGameRule(GameRule.MOB_GRIEFING,false);
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE,101);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES,false);

        long time = world.getFullTime() % 192000; //Modulo la semaine
        long delay;
        if (time > 109000) delay = 301000 - time; //jusqu'à semaine suivant
        else delay = 109000 - time; //jusqu'à nouvelle lune de la semaine
        long days = delay/13000; //nombre de jours restant
        long rest = delay%13000; //nombre de ticks dans le jour restant
        world.setFullTime(world.getFullTime()+days*13000);
        int timeTask = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()->{
            world.setFullTime(world.getFullTime()+rest/100);
        },0,1).getTaskId();
        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),()-> Bukkit.getScheduler().cancelTask(timeTask),101);

        tick();
    }

    private void tick() {
        int tick = 100;
        int pass = Math.round(5000f/ timeToLvl5 *tick);

        timeSkipTask = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(), () -> world.setFullTime(world.getFullTime()+pass),0,tick).getTaskId();

        tickTask = Bukkit.getScheduler().runTaskTimerAsynchronously(EventManager.getPlugin(),()->{
            changePhase(timer.get());
            playEvent();

            timer.addAndGet(tick);
        },0,tick).getTaskId();
    }

    private void changePhase(int timer) {
        if (timer <= 12000) {
            phase = 0;
        } else if (timer >= timeToLvl5) {
            phase = 5;
        } else {
            phase = ((timer-12000)*4)/(timeToLvl5 -12000)+1;
        }
    }

    private void playEvent() {
        int players = Bukkit.getOnlinePlayers().size();

        Vector bossLoc = new Vector(random.nextGaussian(0, 800), 100, random.nextGaussian(0, 800));
        Vector eventLoc = new Vector(random.nextGaussian(0, 50), 100, random.nextGaussian(0, 50));

        switch (phase) {
            case 1 -> {
                if (random.nextFloat() < probaBoss)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new KingSlime((new Location(world,bossLoc.getX(),100,bossLoc.getZ())).toHighestLocation()), 0);
            }

            case 2 -> {
                if (random.nextFloat() < probaMeteor)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new Meteor((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (random.nextFloat() < probaBoss) Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new PhantomOverlord((new Location(world,bossLoc.getX(),100,bossLoc.getZ())).toHighestLocation()), 0);
            }

            case 3 -> {
                if (random.nextFloat() < probaMeteor)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new Meteor((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (random.nextFloat() < probaBee)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new QueenBee((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (random.nextFloat() < probaBoss)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new ScarletRabbit((new Location(world,bossLoc.getX(),100,bossLoc.getZ())).toHighestLocation()), 0);
            }

            case 4 -> {
                if (random.nextFloat() < probaMeteor)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new Meteor((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (random.nextFloat() < probaBee)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new QueenBee((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (random.nextFloat() < probaBoss)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new Shadows((new Location(world,bossLoc.getX(),100,bossLoc.getZ())).toHighestLocation()), 0);
            }
        }
    }

    public void stopEvent() {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,true);
        world.setGameRule(GameRule.DO_FIRE_TICK,true);
        world.setGameRule(GameRule.DO_INSOMNIA,true);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE,true);
        world.setGameRule(GameRule.KEEP_INVENTORY,false);
        world.setGameRule(GameRule.MOB_GRIEFING,true);
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE,100);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES,true);

        Bukkit.getScheduler().cancelTask(tickTask);
        Bukkit.getScheduler().cancelTask(timeSkipTask);
    }

    public int getPhase() {
        return phase;
    }

    public int getTime() {
        return timer.get();
    }

    public void setTime(int time) {
        timer.set(time);
    }

}
