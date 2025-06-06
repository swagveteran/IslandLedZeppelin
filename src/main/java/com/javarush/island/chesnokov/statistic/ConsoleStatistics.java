package com.javarush.island.chesnokov.statistic;

import com.javarush.island.chesnokov.map.Island;
import com.javarush.island.chesnokov.organizm.animals.Animal;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsoleStatistics {
    public static void print(Island island, int tickNumber) {
        clearConsole();

        List<Animal> animals = island.getAllAnimals();

        System.out.println("===== СТАТИСТИКА | ТИК #" + tickNumber + " =====");
        System.out.println("Животных на острове: " + animals.size());

        if (animals.isEmpty()) return;

        Map<String, List<Animal>> grouped = animals.stream()
                .collect(Collectors.groupingBy(a -> a.getClass().getSimpleName()));

        for (Map.Entry<String, List<Animal>> entry : grouped.entrySet()) {
            String type = entry.getKey();
            List<Animal> group = entry.getValue();
            int count = group.size();

            double minSatiety = group.stream().mapToDouble(Animal::getSatiety).min().orElse(0);
            double maxSatiety = group.stream().mapToDouble(Animal::getSatiety).max().orElse(0);
            double avgSatiety = group.stream().mapToDouble(Animal::getSatiety).average().orElse(0);

            System.out.printf(
                    "%-12s | %3d шт | насыщение: мин %.2f / макс %.2f / ср %.2f\n",
                    type, count, minSatiety, maxSatiety, avgSatiety
            );
        }
    }

    private static void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println(E);
        }
    }
}
