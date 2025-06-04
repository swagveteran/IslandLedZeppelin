package com.javarush.island.chesnokov.organizm.animals;

import com.javarush.island.chesnokov.map.Island;
import com.javarush.island.chesnokov.map.Location;
import com.javarush.island.chesnokov.organizm.Eatable;
import com.javarush.island.chesnokov.organizm.Movable;
import com.javarush.island.chesnokov.organizm.Reproducible;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal implements Eatable, Movable, Reproducible {
    private final double  weight;
    private final int maxCountOnCell;
    private final int speed;
    private final double foodAmount;
    private Location currentLocation;
    private boolean processedInTick;
    private volatile boolean alive = true;
    private int satiety;
    private static final int MAX_SATIETY = 5;

    protected Animal(double weight, int maxCountOnCell, int speed, double foodAmount) {
        this.weight = weight;
        this.maxCountOnCell = maxCountOnCell;
        this.speed = speed;
        this.foodAmount = foodAmount;
        this.satiety = MAX_SATIETY;
    }

    @Override
    public void eat(Location location) {
    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public int getMaxCountOnCell() {
        return maxCountOnCell;
    }

    public double getWeight() {
        return weight;
    }

    public int getSpeed() {
        return speed;
    }

    public double getFoodAmount() {
        return foodAmount;
    }

    public boolean isProcessedInTick() {
        return processedInTick;
    }

    public void markProcessed() {
        this.processedInTick = true;
    }

    public void resetTick() {
        this.processedInTick = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        this.alive = false;
    }

    public void eatSuccessful() {
        satiety = MAX_SATIETY;
    }

    public void loseSatiety() {
        satiety--;
    }

    public boolean isStarving() {
        return satiety <= 0;
    }

    public void move(Island island) {
        if (!this.isAlive()) return;
        int maxDistance = ThreadLocalRandom.current().nextInt(1, speed + 1); // от 0 до speed
        if (maxDistance == 0) {
            System.out.println(this.getClass().getSimpleName() + " остался на месте");
            return;
        }

        int islandRows = island.getRows();
        int islandCols = island.getCols();
        int currentRow = currentLocation.getRow();
        int currentCol = currentLocation.getCol();

        int dRow, dCol;
        do {
            dRow = ThreadLocalRandom.current().nextInt(-1, 2);
            dCol = ThreadLocalRandom.current().nextInt(-1, 2);
        } while (dRow == 0 && dCol == 0);

        int newRow = Math.max(0, Math.min(islandRows - 1, currentRow + dRow * maxDistance));
        int newCol = Math.max(0, Math.min(islandCols - 1, currentCol + dCol * maxDistance));

        Location newLocation = island.getLocation(newRow, newCol);

        long sameKindCount = newLocation.getAnimals().stream()
                .filter(a -> a.getClass() == this.getClass())
                .count();

        if (sameKindCount >= this.getMaxCountOnCell()) {
            System.out.println(this.getClass().getSimpleName() + " не может переместиться в [" + newRow + "," + newCol + "], клетка переполнена");
            return;
        }

        currentLocation.removeAnimal(this);
        newLocation.addAnimal(this);
        setCurrentLocation(newLocation);

        System.out.println(this.getClass().getSimpleName() + " переместился в [" + newRow + "," + newCol + "]");
    }

    @Override
    public Animal reproduce() {
        try {
            return this.getClass()
                    .getDeclaredConstructor(double.class, int.class, int.class, double.class)
                    .newInstance(getWeight(), getMaxCountOnCell(), getSpeed(), getFoodAmount());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
