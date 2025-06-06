package com.javarush.island.chesnokov.organizm.animals.predators;

import com.javarush.island.chesnokov.map.Island;
import com.javarush.island.chesnokov.map.Location;
import com.javarush.island.chesnokov.organizm.animals.Animal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Predator extends Animal {
    protected abstract Map<Class<? extends Animal>, Integer> getFoodPreferences();

    protected Predator(double weight, int maxCountOnCell, int speed, double foodAmount) {
        super(weight, maxCountOnCell, speed, foodAmount);
    }

    @Override
    public void eat(Location location) {
        if (!this.isAlive()) return;

        this.loseSatiety();
        if (this.isFull()) return;

        List<Animal> animalsInCell = location.getAnimals();
        synchronized (animalsInCell) {
            for (Iterator<Animal> it = animalsInCell.iterator(); it.hasNext();) {
                Animal prey = it.next();
                if (prey == this || !prey.isAlive()) continue;

                Integer chance = getFoodPreferences().get(prey.getClass());
                if (chance != null && ThreadLocalRandom.current().nextInt(100) < chance) {
                    prey.die();
                    it.remove();
                    this.increaseSatiety(prey.getWeight());
                    return;
                }
            }
        }
    }

    @Override
    public void move(Island island) {
        if (!this.isAlive()) return;

        Location current = this.getCurrentLocation();
        List<Location> neighbors = current.getIsland().getNeighborLocations(current);

        Location target = neighbors.stream()
                .filter(loc -> loc.getAnimals().stream().anyMatch(this::canEat))
                .findFirst()
                .orElseGet(() -> getRandomNeighbor(neighbors, current));

        if (target != current) {
            current.removeAnimal(this);
            target.addAnimal(this);
            this.setCurrentLocation(target);
        }
    }

    private boolean canEat(Animal other) {
        if (other == this || !other.isAlive()) return false;
        Integer chance = getFoodPreferences().get(other.getClass());
        return chance != null && chance > 0;
    }

    private Location getRandomNeighbor(List<Location> neighbors, Location fallback) {
        return neighbors.isEmpty()
                ? fallback
                : neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
    }

}
