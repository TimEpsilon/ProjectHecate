package com.github.timepsilon.comet.mobs.boss.phantom;

public enum PhantomOverlordAttack {

    SUMMON(1),
    SUCC(2),
    NULL(5);

    private final double weight;

    PhantomOverlordAttack(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
