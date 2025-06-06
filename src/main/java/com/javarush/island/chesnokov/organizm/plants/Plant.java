package com.javarush.island.chesnokov.organizm.plants;

public class Plant {
    private boolean alive = true;
    private final double weight = 0.05;

    public double getWeight() {
        return weight;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        this.alive = false;
    }
}

