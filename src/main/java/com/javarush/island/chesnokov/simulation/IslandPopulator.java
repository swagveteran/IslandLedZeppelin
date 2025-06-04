package com.javarush.island.chesnokov.simulation;

import com.javarush.island.chesnokov.map.Island;
import com.javarush.island.chesnokov.map.Location;
import com.javarush.island.chesnokov.organizm.animals.Animal;
import com.javarush.island.chesnokov.organizm.animals.AnimalFactory;
import com.javarush.island.chesnokov.organizm.animals.AnimalType;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class IslandPopulator {
    private static final Map<AnimalType, Integer> INITIAL_COUNTS = Map.of(
            AnimalType.RABBIT, 30,
            AnimalType.WOLF, 5,
            AnimalType.FOX, 5,
            AnimalType.SHEEP, 50
    );

    public static void populateIsland(Island island) {
        for (Map.Entry<AnimalType, Integer> entry : INITIAL_COUNTS.entrySet()) {
            AnimalType type = entry.getKey();
            int count = entry.getValue();

            for (int i = 0; i < count; i++) {
                int row = ThreadLocalRandom.current().nextInt(island.getRows());
                int col = ThreadLocalRandom.current().nextInt(island.getCols());

                Location location = island.getLocation(row, col);
                Animal animal = AnimalFactory.create(type);
                location.addAnimal(animal);
                animal.setCurrentLocation(location);
            }
        }
    }
}
