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
                Animal prey = iterator.next();
                if (prey == this || !prey.isAlive()) continue;

                Integer chance = getFoodPreferences().get(prey.getClass());
                if (chance != null && ThreadLocalRandom.current().nextInt(100) < chance) {
                    prey.die();
                    iterator.remove();
                    this.eatSuccessful(); // 🔥 поел — восстанавливаем насыщение
                    System.out.println(this.getClass().getSimpleName() + " съел " + prey.getClass().getSimpleName()
                            + " в локации [" + location.getRow() + "," + location.getCol() + "]");
                    return;
                }
            }
        }

        this.loseSatiety();
    }
}
