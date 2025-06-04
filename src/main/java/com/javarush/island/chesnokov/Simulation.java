package com.javarush.island.chesnokov;

import com.javarush.island.chesnokov.map.Island;
import com.javarush.island.chesnokov.map.Location;
import com.javarush.island.chesnokov.organizm.animals.Animal;

import java.util.List;
import java.util.concurrent.*;

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
        System.out.println("\n===== ТИК #" + tickCount + " =====");

        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

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
            animal.resetTick(); // сброс флага для контроля
            executor.submit(() -> processEating(animal));
        }
        waitForPhase(executor);

        // 🚶 ФАЗА 3: Перемещение
        animalsToProcess = island.getAllAnimals();
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (Animal animal : animalsToProcess) {
            executor.submit(() -> processMovement(animal));
        }
        waitForPhase(executor);
    }

    private void processEating(Animal animal) {
        synchronized (animal) {
            if (animal.isAlive() && !animal.isProcessedInTick()) {
                animal.markProcessed();
                Location location = animal.getCurrentLocation();
                animal.eat(location.getAnimals());
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