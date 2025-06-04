package com.javarush.island.chesnokov.map;

import com.javarush.island.chesnokov.organizm.animals.Animal;

import java.util.ArrayList;
import java.util.List;

public class Island {
    private final Location[][] locations;

    public Island(int rows, int cols) {
        locations = new Location[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                locations[row][col] = new Location(row, col);
            }
        }
    }

    public Location getLocation(int row, int col) {
        return locations[row][col];
    }

    public int getRows() {
        return locations.length;
    }

    public int getCols() {
        return locations[0].length;
    }

    public List<Animal> getAllAnimals() {
        List<Animal> all = new ArrayList<>();
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                Location location = getLocation(row, col);
                synchronized (location.getAnimals()) {
                    for (Animal animal : location.getAnimals()) {
                        if (animal.isAlive()) {
                            all.add(animal);
                        }
                    }
                }
            }
        }
        return all;
    }

    public List<Location> getNeighborLocations(Location center) {
        int row = center.getRow();
        int col = center.getCol();
        List<Location> neighbors = new ArrayList<>();

        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                if (dRow == 0 && dCol == 0) continue;

                int newRow = row + dRow;
                int newCol = col + dCol;

                if (newRow >= 0 && newRow < getRows() && newCol >= 0 && newCol < getCols()) {
                    neighbors.add(getLocation(newRow, newCol));
                }
            }
        }

        return neighbors;
    }
}
