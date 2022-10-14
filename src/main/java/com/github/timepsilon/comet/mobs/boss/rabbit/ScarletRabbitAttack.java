package com.github.timepsilon.comet.mobs.boss.rabbit;

public enum ScarletRabbitAttack {

    SHOCKWAVE(12),
    LIGHTNING(11),
    SPORES(10),
    SOULS(5),
    WITHER(1),
    NULL(60);

    private final double weight;

    ScarletRabbitAttack(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
