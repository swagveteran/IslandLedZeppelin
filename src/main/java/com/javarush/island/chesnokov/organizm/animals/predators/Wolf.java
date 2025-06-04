package com.javarush.island.chesnokov.organizm.animals.predators;

import com.javarush.island.chesnokov.organizm.animals.Animal;
import com.javarush.island.chesnokov.organizm.animals.herbivores.Rabbit;

import java.util.List;
import java.util.Map;

public class Wolf extends Predator{
    private static final Map<Class<? extends Animal>, Integer> FOOD_PREFERENCE = Map.of(
            Rabbit.class, 60
    );

    public Wolf(double weight, int maxCountOnCell, int speed, double foodAmount) {
        super(weight, maxCountOnCell, speed, foodAmount);
    }

    @Override
    protected Map<Class<? extends Animal>, Integer> getFoodPreferences() {
        return FOOD_PREFERENCE;
    }
}
