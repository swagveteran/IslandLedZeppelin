package com.javarush.island.chesnokov.organizm.animals.herbivores;

import com.javarush.island.chesnokov.map.Location;
import com.javarush.island.chesnokov.organizm.animals.Animal;
import com.javarush.island.chesnokov.organizm.plants.Plant;

import java.util.List;

public abstract class Herbivore extends Animal {
    protected Herbivore(double weight, int maxCountOnCell, int speed, double foodAmount) {
        super(weight, maxCountOnCell, speed, foodAmount);
    }

    @Override
    public void eat(Location location) {
        if (!this.isAlive()) return;

        List<Plant> plants = location.getPlants();
        synchronized (plants) {
            if (!plants.isEmpty()) {
                plants.remove(0); // съели растение
                this.eatSuccessful(); // восстановили насыщение
                System.out.println(this.getClass().getSimpleName() + " съел растение в [" +
                        location.getRow() + "," + location.getCol() + "]");
                return;
            }
        }

        // если растения не нашёл
        this.loseSatiety();
    }
}
