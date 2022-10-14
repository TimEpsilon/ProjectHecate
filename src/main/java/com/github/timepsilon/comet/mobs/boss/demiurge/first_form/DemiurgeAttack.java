package com.github.timepsilon.comet.mobs.boss.demiurge.first_form;

public enum DemiurgeAttack {

    MOB(50),
    WITHERWAVE(40),
    LASER(35),
    FIREBALL(50),
    VORTEX(30),
    DASH(20),
    METEOR(35),
    BULLET_HELL(30),
    TELEPORT(25),
    NULL(40);

    private double weight;

    DemiurgeAttack(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
