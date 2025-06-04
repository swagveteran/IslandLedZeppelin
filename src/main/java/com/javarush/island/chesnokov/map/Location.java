package com.javarush.island.chesnokov.map;

import com.javarush.island.chesnokov.organizm.animals.Animal;

import java.util.*;

public class Location {
    private final int row;
    private final int col;
    private final List<Animal> animals = Collections.synchronizedList(new ArrayList<>());

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
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
                        System.out.println(parent.getClass().getSimpleName() + " размножился в локации [" + row + "," + col + "]");
                    }
                }
            }
        }
    }
}
