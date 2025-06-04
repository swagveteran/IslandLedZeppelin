package com.javarush.island.chesnokov.organizm.animals.predators;

import com.javarush.island.chesnokov.organizm.animals.Animal;
import com.javarush.island.chesnokov.organizm.animals.herbivores.*;


import java.util.Map;

public class Fox extends Predator {
    private static final Map<Class<? extends Animal>, Integer> FOOD_PREFERENCE = Map.of(
            Rabbit.class, 70,
            Sheep.class, 0
    );

    public Fox(double weight, int maxCountOnCell, int speed, double foodAmount) {
        super(weight, maxCountOnCell, speed, foodAmount);
    }

    @Override
    protected Map<Class<? extends Animal>, Integer> getFoodPreferences() {
        return FOOD_PREFERENCE;
    }
}
