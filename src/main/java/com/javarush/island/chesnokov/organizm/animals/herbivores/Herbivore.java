package com.javarush.island.chesnokov.organizm.animals.herbivores;

import com.javarush.island.chesnokov.map.Location;
import com.javarush.island.chesnokov.organizm.animals.Animal;
import com.javarush.island.chesnokov.organizm.plants.Plant;

import java.util.List;

public abstract class Herbivore extends Animal {

    private static final double PLANT_WEIGHT = 1.0;

    protected Herbivore(double weight, int maxCountOnCell, int speed, double foodAmount) {
        super(weight, maxCountOnCell, speed, foodAmount);
    }

    @Override
    public void eat(Location location) {
        if (!this.isAlive()) return;

        this.loseSatiety();
        if (this.isFull()) return;

        List<Plant> plants = location.getPlants();
        synchronized (plants) {
            if (!plants.isEmpty()) {
                Plant plant = plants.removeFirst();
                this.increaseSatiety(plant.getWeight());
            }
        }
    }
}