package com.javarush.island.chesnokov.simulation;

import com.javarush.island.chesnokov.map.Island;
import com.javarush.island.chesnokov.map.Location;
import com.javarush.island.chesnokov.organizm.animals.Animal;
import com.javarush.island.chesnokov.organizm.plants.Plant;
import com.javarush.island.chesnokov.simulation.config.SimulationConfig;
import com.javarush.island.chesnokov.statistic.ConsoleStatistics;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class Simulation {
    private final Island island;
    private int tickCount = 0;

    public Simulation(Island island) {
        this.island = island;
    }

    public void start() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::runTick, 0, 1, TimeUnit.SECONDS);
    }

    private void runTick() {
        tickCount++;

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        growPlants();

        // 🐣 ФАЗА 1: Размножение (по локациям)
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Location location = island.getLocation(row, col);
                executor.submit(location::reproduceAnimals);
            }
        }
        waitForPhase(executor);

        // 🍽 ФАЗА 2: Питание
        List<Animal> animalsToProcess = island.getAllAnimals();
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (Animal animal : animalsToProcess) {
            animal.resetTick();
        }

        submitInBatches(animalsToProcess, executor, this::processEating);
        waitForPhase(executor);

        // 🚶 ФАЗА 3: Перемещение
        animalsToProcess = island.getAllAnimals();
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        submitInBatches(animalsToProcess, executor, this::processMovement);
        waitForPhase(executor);

        cleanupStarvedAnimals();

        ConsoleStatistics.print(island, tickCount);
    }

    private void submitInBatches(List<Animal> animals,
                                 ExecutorService executor, Consumer<Animal> action) {
        for (int i = 0; i < animals.size(); i += 100) {
            int to = Math.min(i + 100, animals.size());
            List<Animal> batch = animals.subList(i, to);

            executor.submit(() -> {
                for (Animal animal : batch) {
                    action.accept(animal);
                }
            });
        }
    }

    private void processEating(Animal animal) {
        synchronized (animal) {
            if (animal.isAlive() && !animal.isProcessedInTick()) {
                animal.markProcessed();
                Location location = animal.getCurrentLocation();
                animal.eat(animal.getCurrentLocation());
            }
        }
    }

    private void processMovement(Animal animal) {
        synchronized (animal) {
            if (animal.isAlive()) {
                animal.move(island);
            }
        }
    }

    private void growPlants() {
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Location location = island.getLocation(row, col);
                List<Plant> plants = location.getPlants();

                synchronized (plants) {
                    int currentCount = plants.size();
                    int spaceLeft = SimulationConfig.MAX_PLANTS_PER_CELL - currentCount;
                    if (spaceLeft <= 0) continue;

                    int toGrow = ThreadLocalRandom.current().nextInt(1, 4); // 1–3 растений
                    toGrow = Math.min(toGrow, spaceLeft); // не превышаем лимит

                    for (int i = 0; i < toGrow; i++) {
                        plants.add(new Plant());
                    }
                }
            }
        }
    }

    private void cleanupStarvedAnimals() {
        for (Animal animal : island.getAllAnimals()) {
            if (animal.isAlive() && animal.isStarving()) {
                animal.die();
            }
        }
    }

    private void waitForPhase(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}