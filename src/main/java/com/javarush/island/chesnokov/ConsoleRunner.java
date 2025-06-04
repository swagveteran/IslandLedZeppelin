package com.javarush.island.chesnokov;

import com.javarush.island.chesnokov.map.Island;
import com.javarush.island.chesnokov.map.Location;
import com.javarush.island.chesnokov.organizm.animals.herbivores.Rabbit;
import com.javarush.island.chesnokov.organizm.animals.predators.Wolf;

public class ConsoleRunner {
    public static void main(String[] args){
        Island island = new Island(100, 20);

        // Заселяем стартовую точку для теста
        Location start = island.getLocation(0, 0);
        Wolf wolf = new Wolf(50.0, 30, 3, 8.0);
        Rabbit rabbit1 = new Rabbit(2.0, 150, 2, 0.45);
        Rabbit rabbit2 = new Rabbit(2.0, 150, 2, 0.45);

        start.addAnimal(wolf);
        start.addAnimal(rabbit1);
        start.addAnimal(rabbit2);

        wolf.setCurrentLocation(start);
        rabbit1.setCurrentLocation(start);
        rabbit2.setCurrentLocation(start);

        // Запускаем симуляцию
        Simulation simulation = new Simulation(island);
        simulation.start();
    }
}
