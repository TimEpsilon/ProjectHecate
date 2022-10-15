package com.github.timepsilon.comet.mobs.boss.demiurge.final_form;

public enum TrueDemiurgeAttack {

    SWORD_SWING_VERTICAL(1),
    SWORD_SWING_HORIZONTAL(1),
    MAGIC_CIRCLE(1),
    SUMMON(1),
    VORTEX(1),
    GRAB(1),
    BOOMERANG(1),
    DASH(1),
    NULL(1);

    private final double weight;

    TrueDemiurgeAttack(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
