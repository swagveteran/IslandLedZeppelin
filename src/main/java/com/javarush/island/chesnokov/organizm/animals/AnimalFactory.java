package com.javarush.island.chesnokov.organizm.animals;

import com.javarush.island.chesnokov.organizm.animals.config.AnimalConfig;
import com.javarush.island.chesnokov.organizm.animals.config.AnimalTypeConfig;
import com.javarush.island.chesnokov.organizm.animals.herbivores.Rabbit;
import com.javarush.island.chesnokov.organizm.animals.herbivores.Sheep;

import com.javarush.island.chesnokov.organizm.animals.predators.Fox;
import com.javarush.island.chesnokov.organizm.animals.predators.Wolf;

public class AnimalFactory {

    public static Animal create(AnimalType type) {
        AnimalConfig config = AnimalTypeConfig.get(type);

        return switch (type) {
            case WOLF   -> new Wolf(config.weight, config.maxPerCell, config.speed, config.foodRequired);
            case FOX    -> new Fox(config.weight, config.maxPerCell, config.speed, config.foodRequired);
            case RABBIT -> new Rabbit(config.weight, config.maxPerCell, config.speed, config.foodRequired);
            case SHEEP  -> new Sheep(config.weight, config.maxPerCell, config.speed, config.foodRequired);
        };
    }
}