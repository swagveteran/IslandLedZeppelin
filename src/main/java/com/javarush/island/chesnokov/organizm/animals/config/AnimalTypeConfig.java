package com.javarush.island.chesnokov.organizm.animals.config;

import com.javarush.island.chesnokov.organizm.animals.AnimalType;

import java.util.EnumMap;
import java.util.Map;

public class AnimalTypeConfig {
    private static final Map<AnimalType, AnimalConfig> CONFIG = new EnumMap<>(AnimalType.class);

    static {
        CONFIG.put(AnimalType.WOLF,   new AnimalConfig(50.0, 30, 3, 8.0));
        CONFIG.put(AnimalType.FOX,    new AnimalConfig(8.0, 30, 2, 2.0));
        CONFIG.put(AnimalType.RABBIT, new AnimalConfig(2.0, 150, 2, 0.45));
        CONFIG.put(AnimalType.SHEEP,  new AnimalConfig(70.0, 140, 3, 15.0));
        // продолжишь дальше: MOUSE, BEAR, etc.
    }

    public static AnimalConfig get(AnimalType type) {
        return CONFIG.get(type);
    }
}
