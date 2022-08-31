package com.github.timepsilon.comet.mobs;

import com.github.timepsilon.ProjectHecate;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scoreboard.Team;

public abstract class CometMobs {

    protected LivingEntity entity;
    private int despawn;

    public CometMobs(LivingEntity e, int health) {
        this.entity = e;
        this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        this.entity.setHealth(health);

        this.entity.addScoreboardTag("Comet");
        Team scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");

        scarlet.addEntry(this.entity.getUniqueId().toString());

        removeIfDespawn();
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public void removeIfDespawn() {
        despawn = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
            if (!entity.isValid()) {
                Team scarlet = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Scarlet");
                if (scarlet != null) {
                    if (scarlet.getEntries().contains(entity.getUniqueId().toString())) {
                        scarlet.removeEntry(entity.getUniqueId().toString());
                        Bukkit.getScheduler().cancelTask(despawn);
                    }
                }
            }
        },1200,1200).getTaskId();
    }
}
