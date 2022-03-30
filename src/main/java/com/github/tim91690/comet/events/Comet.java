package com.github.tim91690.comet.events;

import com.github.tim91690.EventManager;
import com.github.tim91690.comet.item.CustomItems;
import com.github.tim91690.comet.misc.ConfigManager;
import com.github.tim91690.comet.mobs.boss.*;
import com.github.tim91690.comet.structure.RitualArena;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Comet {

    private int phase;
    private final int timeToLvl5 = 108000; //90min
    private final World world;
    private int tickTask;
    private int timeSkipTask;
    private final AtomicInteger timer = new AtomicInteger();
    private static final Random random = new Random();
    private static final float probaMeteor = 0.01f;
    private static final float probaBee = 0.03f;
    public final Map<UUID,Double> DeathCount = new HashMap<>();
    public final Map<UUID,Double> KillCount = new HashMap<>();
    public final Map<UUID,Double> DamageCount = new HashMap<>();

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

        RitualArena.spawnRitualArena(false);

        tick();

        TextComponent xaeros = new TextComponent(CustomItems.PDAText+ ChatColor.GREEN + "Cliquez si Xaero's Minimap est installé");
        xaeros.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/showwaypoint true"));
        Bukkit.broadcast(xaeros);

    }

    private void tick() {
        int tick = 100;
        int pass = Math.round(5000f/ timeToLvl5 *tick);

        timeSkipTask = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(), () -> world.setFullTime(world.getFullTime()+pass),0,tick).getTaskId();

        tickTask = Bukkit.getScheduler().runTaskTimerAsynchronously(EventManager.getPlugin(),()->{
            boolean hasChanged = changePhase(timer.get());
            playEvent(hasChanged);

            timer.addAndGet(tick);
        },0,tick).getTaskId();
    }

    private boolean changePhase(int timer) {
        boolean hasChanged;
        if (timer <= 12000) {
            hasChanged = phase != 0;
            phase = 0;
            return hasChanged;
        } else if (timer >= timeToLvl5) {
            hasChanged = phase != 5;
            phase = 5;
            return hasChanged;
        } else {
            hasChanged = phase != ((timer-12000)*4)/(timeToLvl5 -12000)+1;
            phase = ((timer-12000)*4)/(timeToLvl5 -12000)+1;
            return hasChanged;
        }
    }

    private void playEvent(boolean spawnBoss) {
        int players = Bukkit.getOnlinePlayers().size();

        Vector eventLoc = new Vector(random.nextGaussian(0, 50), 100, random.nextGaussian(0, 50));

        switch (phase) {
            case 1 -> {
                if (spawnBoss) {
                    int n = players/4 + random.nextInt(3) + 1;
                    for (int i = 0; i<n; i++) {
                        Bukkit.getScheduler().runTaskLaterAsynchronously(EventManager.getPlugin(), () -> {
                            Vector loc = getRandomLocation();
                            waitUntilLoaded(loc);
                            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', "&eUn &2&lKing Slime &ea spawn en &a<" + (int) loc.getX() + " , " + (int) loc.getZ() + ">")));
                            Boss.sendWaypoint("xaero-waypoint:KingSlime:KS:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":2:false:0:Internal-overworld-waypoints");
                        }, i*100L);
                    }
                }
            }

            case 2 -> {
                if (random.nextFloat() < probaMeteor)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new Meteor((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (spawnBoss) {
                    int n = players/4 + random.nextInt(3) + 1;
                    for (int i = 0; i<n; i++) {
                        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                            Vector loc = getRandomLocation();
                            waitUntilLoaded(loc);
                            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&',"&eUn &1&lPhantom Overlord &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getZ()+">")));
                            Boss.sendWaypoint("xaero-waypoint:PhantomOverlord:PO:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":1:false:0:Internal-overworld-waypoints");
                        }, i*110L);
                    }
                }
            }

            case 3 -> {
                if (random.nextFloat() < probaMeteor)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new Meteor((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (random.nextFloat() < probaBee)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new QueenBee((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (spawnBoss) {
                    int n = players/4 + random.nextInt(3) + 1;
                    for (int i = 0; i<n; i++) {
                        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                            Vector loc = getRandomLocation();
                            waitUntilLoaded(loc);
                            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&',"&eUn &4&lScarlet Devil &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getZ()+">")));
                            Boss.sendWaypoint("xaero-waypoint:ScarletRabbit:SR:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":4:false:0:Internal-overworld-waypoints");
                        }, i*120L);
                    }
                }
            }

            case 4 -> {
                if (random.nextFloat() < probaMeteor)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new Meteor((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (random.nextFloat() < probaBee)
                    Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> new QueenBee((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (spawnBoss) {
                    int n = players/4 + random.nextInt(3) + 1;
                    for (int i = 0; i<n; i++) {
                        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(), () -> {
                            Vector loc = getRandomLocation();
                            waitUntilLoaded(loc);
                            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&',"&eUne &0&lShadow &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getZ()+">")));
                            Boss.sendWaypoint("xaero-waypoint:Shadow:S:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":0:false:0:Internal-overworld-waypoints");
                        }, i*130L);
                    }
                }
            }

            case 5 -> {
                if (spawnBoss) initRitual();
            }
        }
    }

    public void stopEvent(boolean isRestarting) {
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

        EventManager.isRunningEvent = false;

        if (isRestarting) return;

        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),()-> {
            showStats(DeathCount,"Morts");
            Bukkit.getOnlinePlayers().forEach(Player -> Player.playSound(Player.getLocation(),Sound.ENTITY_ITEM_PICKUP,SoundCategory.PLAYERS,1,0.5f));
        },100);

        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),()-> {
            showStats(KillCount,"Kills");
            Bukkit.getOnlinePlayers().forEach(Player -> Player.playSound(Player.getLocation(),Sound.ENTITY_ITEM_PICKUP,SoundCategory.PLAYERS,1,0.5f));
        },200);

        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),()-> {
            showStats(DamageCount,"Damage");
            Bukkit.getOnlinePlayers().forEach(Player -> Player.playSound(Player.getLocation(),Sound.ENTITY_ITEM_PICKUP,SoundCategory.PLAYERS,1,0.5f));
        },300);
    }

    private Vector getRandomLocation() {
        double r = random.nextGaussian(1200,400);
        double theta = random.nextDouble()*2*Math.PI;

        return new Vector(r*Math.cos(theta),100,r*Math.sin(theta));
    }

    private void initRitual() {
        Location loc = ConfigManager.getLoc();

        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),()->{
            new CosmicRitual(loc);
        },6000);
    }

    private void waitUntilLoaded(Vector vector) {
        ArrayList<Integer> tasks = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        switch (phase) {
            case 1 -> new TempBoss(vector,250,ChatColor.translateAlternateColorCodes('&',"&2&lKing Slime"),uuid);
            case 2 -> new TempBoss(vector,280,ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Phantom Overlord",uuid);
            case 3 -> new TempBoss(vector,300,ChatColor.translateAlternateColorCodes('&',"&4&lScarlet Devil"),uuid);
            case 4 -> new TempBoss(vector,350,ChatColor.translateAlternateColorCodes('&',"&8&lShadow"),uuid);
        }

        tasks.add(Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()->{
            for (Player p : Bukkit.getOnlinePlayers()) {
                Vector pvec = p.getLocation().toVector();
                pvec.setY(vector.getY());
                if (pvec.distance(vector) > 160) continue;
                Location loc = new Location(world,vector.getX(),320,vector.getZ());
                while (loc.getBlock().getType().isAir() && loc.getY()>0) {
                    loc.subtract(0,5,0);
                }
                switch (phase) {
                    case 1 -> new KingSlime(loc.add(0,10,0),false);
                    case 2 -> new PhantomOverlord(loc.add(0,50,0),false);
                    case 3 -> new ScarletRabbit(loc,false);
                    case 4 -> new Shadows(loc,false);
                }
                TempBoss.getTempBossList().remove(uuid);
                Bukkit.getScheduler().cancelTask(tasks.get(0));
                break;
            }
        },0,60).getTaskId());
    }

    private void showStats(Map<UUID,Double> StatCount,String StatName) {
        Map<UUID, Double> list = StatCount.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue,(oldValue, newValue) -> oldValue, LinkedHashMap::new));
        char[] nums = new char[]{'❶', '❷', '❸', '❹', '❺' };

        Bukkit.getOnlinePlayers().forEach(Player -> {
            double amount = (list.containsKey(Player.getUniqueId())) ? Math.round(list.get(Player.getUniqueId())*1000d)/1000d : 0.0;
            Player.sendMessage(ChatColor.GOLD + "Votre Nombre de "+ StatName +" : " + amount + "\n");
        });

        Bukkit.broadcast(Component.text(ChatColor.GOLD + "---------- "+ StatName +" ----------"));
        for (int i = 0; i < Math.min(5,list.size()); i++) {
            UUID uuid = (UUID) list.keySet().toArray()[list.size() - 1 - i];
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            Bukkit.broadcast(Component.text(ChatColor.YELLOW + " " + nums[i] + " " + name + " : " + Math.round(StatCount.get(uuid)*1000f)/1000f));
        }
        Bukkit.broadcast(Component.text(ChatColor.GOLD + "---------------------------" + "\n"));
        StatCount.clear();
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
