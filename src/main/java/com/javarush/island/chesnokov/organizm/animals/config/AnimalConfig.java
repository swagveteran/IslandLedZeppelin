package com.javarush.island.chesnokov.organizm.animals.config;

public class AnimalConfig {
    public final double weight;
    public final int maxPerCell;
    public final int speed;
    public final double foodRequired;

    public AnimalConfig(double weight, int maxPerCell, int speed, double foodRequired) {
        this.weight = weight;
        this.maxPerCell = maxPerCell;
        this.speed = speed;
        this.foodRequired = foodRequired;
    }
}
