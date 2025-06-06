package com.javarush.island.chesnokov.map;

import com.javarush.island.chesnokov.organizm.animals.Animal;
import com.javarush.island.chesnokov.organizm.plants.Plant;

import java.util.*;

public class Location {
    private final int row;
    private final int col;
    private final Island island;
    private final List<Animal> animals = Collections.synchronizedList(new ArrayList<>());
    private final List<Plant> plants = Collections.synchronizedList(new ArrayList<>());

    public Location(int row, int col, Island island) {
        this.row = row;
        this.col = col;
        this.island = island;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Island getIsland() {
        return island;
    }

    public void reproduceAnimals() {
        Map<Class<? extends Animal>, List<Animal>> groupedByType = new HashMap<>();

        synchronized (animals) {
            for (Animal animal : animals) {
                if (!animal.isAlive()) continue; // <-- добавляем фильтр
                groupedByType
                        .computeIfAbsent(animal.getClass(), k -> new ArrayList<>())
                        .add(animal);
            }

            for (Map.Entry<Class<? extends Animal>, List<Animal>> entry : groupedByType.entrySet()) {
                List<Animal> group = entry.getValue();

                if (group.size() >= 2) {
                    Animal parent = group.getFirst();
                    Animal offspring = parent.reproduce();

                    if (offspring != null && group.size() < parent.getMaxCountOnCell()) {
                        animals.add(offspring);
                        offspring.setCurrentLocation(this);
                    }
                }
            }
        }
    }
}
