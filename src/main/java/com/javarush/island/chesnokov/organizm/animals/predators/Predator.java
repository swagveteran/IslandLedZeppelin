package com.javarush.island.chesnokov.organizm.animals.predators;

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

        List<Animal> animalsInCell = location.getAnimals();
        synchronized (animalsInCell) {
            Iterator<Animal> iterator = animalsInCell.iterator();
            while (iterator.hasNext()) {
                Animal potentialPrey = iterator.next();

                if (potentialPrey == this || !potentialPrey.isAlive()) continue;

                Integer chance = getFoodPreferences().get(potentialPrey.getClass());
                if (chance != null && ThreadLocalRandom.current().nextInt(100) < chance) {
                    System.out.println(this.getClass().getSimpleName() + " съел " + potentialPrey.getClass().getSimpleName()
                            + " в локации [" + location.getRow() + "," + location.getCol() + "]");
                    potentialPrey.die();
                    iterator.remove();
                    break;
                }
            }
        }
    }
}
