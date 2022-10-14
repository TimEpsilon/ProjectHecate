package com.github.timepsilon.comet.mobs.boss.shadows;

public enum ShadowsAttack {

    DESPAIR(50),
    TELEPORT(35),
    WITHER(40),
    SOUL(45),
    NULL(50);

    private final double weight;

    ShadowsAttack(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
