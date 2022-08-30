package com.github.timepsilon.comet.events;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.item.CustomItems;
import com.github.timepsilon.comet.misc.ConfigManager;
import com.github.timepsilon.comet.misc.TextManager;
import com.github.timepsilon.comet.mobs.boss.*;
import com.github.timepsilon.comet.structure.RitualArena;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Comet {

    private int phase;
    private final int timeToLvl5 = 108000; //90min = 10 + 4*20
    private final World world;
    private int tickTask;
    private int timeSkipTask;
    private final AtomicInteger timer = new AtomicInteger();
    private static final Random random = new Random();
    private static final float probaMeteor = 0.04f;
    private static final float probaBee = 0.6f;
    private int lastLine = -1;
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
        ProjectHecate.isRunningEvent = true;

        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
        world.setGameRule(GameRule.DO_FIRE_TICK,false);
        world.setGameRule(GameRule.DO_INSOMNIA,true);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE,false);
        world.setGameRule(GameRule.KEEP_INVENTORY,true);
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE,101);
        world.setClearWeatherDuration(180000);
        world.setSpawnLimit(SpawnCategory.MONSTER,55);

        //team
        Team scarlet;
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet") == null) {
            scarlet = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("Scarlet");
            scarlet.color(NamedTextColor.DARK_RED);
            scarlet.setAllowFriendlyFire(false);
        }

        long time = world.getFullTime() % 192000; //Modulo la semaine
        long delay;
        //109500 = ticks avant 19h30 soir de nouvelle lune de la semaine
        //301500 = ticks avant 19h30 soir de nouvelle lune de la semaine suivante
        if (time > 109500) delay = 301500 - time; //jusqu'à semaine suivant
        else delay = 109500 - time; //jusqu'à nouvelle lune de la semaine
        long days = delay/24000; //nombre de jours restant
        long rest = delay%24000; //nombre de ticks dans le jour restant
        world.setFullTime(world.getFullTime()+days*24000);
        int timeTask = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()-> world.setFullTime(world.getFullTime()+rest/100),0,1).getTaskId();
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> Bukkit.getScheduler().cancelTask(timeTask),100);

        RitualArena.spawnRitualArena(false);

        tick();

        TextComponent xaeros = new TextComponent(CustomItems.PDAText+ ChatColor.GREEN + "Cliquez si Xaero's Minimap est installé");
        xaeros.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/showwaypoint true"));
        Bukkit.broadcast(xaeros);

    }

    private void tick() {
        int tick = 100;
        int pass = (int) Math.floor(4500f/ timeToLvl5 *tick);

        //1 minecraft hour = 1000 ticks
        //Starts at 19h30 (13500)
        //adds 4 ticks to the world every 100 ticks for 108 000 ticks
        timeSkipTask = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> world.setFullTime(world.getFullTime()+pass),0,tick).getTaskId();

        tickTask = Bukkit.getScheduler().runTaskTimerAsynchronously(ProjectHecate.getPlugin(),()->{
            boolean hasChanged = changePhase(timer.get());
            //TODO : show boss message spawn to closest players
            playEvent(hasChanged);
            sendText(timer.get());
            timer.addAndGet(tick);

        },0,tick).getTaskId();
    }

    public void lastWave() {
        int timeInTicks = 18000; //15min

        timeSkipTask = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> world.setFullTime(world.getFullTime()+1),0,4).getTaskId();
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),() -> TextManager.sendSamTextToPlayer(ChatColor.GREEN + "15min avant le lever du Soleil. La Comète s'éloigne..."),350);
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),() -> stopEvent(false),timeInTicks);
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

    private void sendText(int timer) {
        int line = (timer*90)/timeToLvl5;
        if (lastLine != line) {
            lastLine = line;
            TextManager.SamExplains(line);
        }
    }

    private void playEvent(boolean spawnBoss) {
        int players = Bukkit.getOnlinePlayers().size();

        Vector eventLoc = new Vector(random.nextGaussian(0, 50), 100, random.nextGaussian(0, 50));
        int n = players/2 + random.nextInt(3) + 1;
        switch (phase) {
            case 1 -> {
                if (spawnBoss) {
                    for (int i = 0; i<n; i++) {
                        Bukkit.getScheduler().runTaskLaterAsynchronously(ProjectHecate.getPlugin(), () -> {
                            Vector loc = getRandomLocation();
                            try {
                                waitUntilLoaded(loc);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', "&eUn &2&lKing Slime &ea spawn en &a<" + (int) loc.getX() + " , " + (int) loc.getZ() + ">")));
                            Boss.sendWaypoint("xaero-waypoint:KingSlime:KS:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":2:false:0:Internal-overworld-waypoints");
                        }, i*100L);
                    }
                }
            }

            case 2 -> {
                if (random.nextFloat() < probaMeteor)
                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> new Meteor((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (spawnBoss) {
                    for (int i = 0; i<n; i++) {
                        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> {
                            Vector loc = getRandomLocation();
                            try {
                                waitUntilLoaded(loc);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&',"&eUn &1&lPhantom Overlord &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getZ()+">")));
                            Boss.sendWaypoint("xaero-waypoint:PhantomOverlord:PO:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":1:false:0:Internal-overworld-waypoints");
                        }, i*110L);
                    }
                }
            }

            case 3 -> {
                if (random.nextFloat() < probaMeteor)
                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> new Meteor((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (random.nextFloat() < probaBee)
                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> new QueenBee((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (spawnBoss) {
                    for (int i = 0; i<n; i++) {
                        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> {
                            Vector loc = getRandomLocation();
                            try {
                                waitUntilLoaded(loc);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&',"&eUn &4&lScarlet Devil &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getZ()+">")));
                            Boss.sendWaypoint("xaero-waypoint:ScarletRabbit:SR:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":4:false:0:Internal-overworld-waypoints");
                        }, i*120L);
                    }
                }
            }

            case 4 -> {
                if (random.nextFloat() < probaMeteor)
                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> new Meteor((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (random.nextFloat() < probaBee)
                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> new QueenBee((Bukkit.getOnlinePlayers().stream().toList().get(random.nextInt(players))).getLocation().add(eventLoc).toHighestLocation()), 0);
                else if (spawnBoss) {
                    for (int i = 0; i<n; i++) {
                        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(), () -> {
                            Vector loc = getRandomLocation();
                            try {
                                waitUntilLoaded(loc);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&',"&eUne &0&lShadow &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getZ()+">")));
                            Boss.sendWaypoint("xaero-waypoint:Shadow:S:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":0:false:0:Internal-overworld-waypoints");
                        }, i*130L);
                    }
                }
            }

            case 5 -> {
                if (spawnBoss) {
                    //TODO : check if all bosses are dead
                    initRitual();
                    Bukkit.getScheduler().cancelTask(timeSkipTask);
                    Bukkit.getScheduler().cancelTask(tickTask);
                    long time = world.getFullTime();
                    long remain = 18000 - (time%24000);
                    Bukkit.getScheduler().runTask(ProjectHecate.getPlugin(),()->world.setFullTime(time + remain));

                }
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
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE,100);
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES,true);
        world.setSpawnLimit(SpawnCategory.MONSTER,-1);

        //team
        Team scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");
        assert scarlet != null;
        scarlet.unregister();

        ProjectHecate.isRunningEvent = false;

        Bukkit.getScheduler().cancelTask(timeSkipTask);

        if (isRestarting) return;

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),() -> TextManager.sendSamTextToPlayer(ChatColor.GREEN + "Le jour se lève... L'énergie de la Comète est à présent négligeable."),0);
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),() -> TextManager.sendSamTextToPlayer(ChatColor.GREEN + "Sans la présence de la Comète, vos artéfacts ont perdu leur pouvoir... Jusqu'à son prochain passage..."),50);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> {
            showStats(DeathCount,"Morts");
            Bukkit.getOnlinePlayers().forEach(Player -> Player.playSound(Player.getLocation(),Sound.ENTITY_ITEM_PICKUP,SoundCategory.PLAYERS,1,0.5f));
        },100);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> {
            showStats(KillCount,"Kills");
            Bukkit.getOnlinePlayers().forEach(Player -> Player.playSound(Player.getLocation(),Sound.ENTITY_ITEM_PICKUP,SoundCategory.PLAYERS,1,0.5f));
        },200);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> {
            showStats(DamageCount,"Damage");
            Bukkit.getOnlinePlayers().forEach(Player -> Player.playSound(Player.getLocation(),Sound.ENTITY_ITEM_PICKUP,SoundCategory.PLAYERS,1,0.5f));
        },300);
    }

    private Vector getRandomLocation() {
        double r = random.nextGaussian(1500,400);
        double theta = random.nextDouble()*2*Math.PI;

        return new Vector(r*Math.cos(theta),100,r*Math.sin(theta));
    }

    private void initRitual() {
        Location loc = ConfigManager.getLoc();
        new CosmicRitual(loc);
    }

    private void waitUntilLoaded(Vector vector) throws ExecutionException, InterruptedException {
        ArrayList<Integer> tasks = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        switch (phase) {
            case 1 -> new TempBoss(vector,250,ChatColor.translateAlternateColorCodes('&',"&2&lKing Slime"),uuid);
            case 2 -> new TempBoss(vector,280,ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Phantom Overlord",uuid);
            case 3 -> new TempBoss(vector,300,ChatColor.translateAlternateColorCodes('&',"&4&lScarlet Devil"),uuid);
            case 4 -> new TempBoss(vector,350,ChatColor.translateAlternateColorCodes('&',"&8&lShadow"),uuid);
        }


//        Chunk chunk = world.getChunkAtAsync((int) vector.getX(), (int) vector.getZ()).join();
        tasks.add(Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
            if (!world.isChunkLoaded((int) vector.getX() >> 4, (int) vector.getZ() >> 4)) return;

            Location loc = new Location(world, vector.getX(), 320, vector.getZ());
            while (loc.getBlock().getType().isAir() && loc.getY() > 0) {
                loc.subtract(0, 5, 0);
            }
            loc.add(0,5,0);

            Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> {
                switch (phase) {
                    case 1 -> new KingSlime(loc.add(0, 10, 0), false);
                    case 2 -> new PhantomOverlord(loc.add(0, 50, 0), false);
                    case 3 -> new ScarletRabbit(loc, false);
                    case 4 -> new Shadows(loc, false);
                }
            },100);
            TempBoss.getTempBossList().remove(uuid);
            Bukkit.getScheduler().cancelTask(tasks.get(0));
        }, 100, 60).getTaskId());
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
