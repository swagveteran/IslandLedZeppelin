package com.javarush.island.chesnokov.organizm.plants;

public class Plant {
    private boolean alive = true;

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        this.alive = false;
    }
}

