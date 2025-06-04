package com.javarush.island.chesnokov.organizm.animals.herbivores;

import com.javarush.island.chesnokov.organizm.animals.Animal;

public abstract class Herbivore extends Animal {
    protected Herbivore(double weight, int maxCountOnCell, int speed, double foodAmount) {
        super(weight, maxCountOnCell, speed, foodAmount);
    }
}
