package com.javarush.island.chesnokov;

import com.javarush.island.chesnokov.map.Island;
import com.javarush.island.chesnokov.map.Location;
import com.javarush.island.chesnokov.organizm.animals.*;
import com.javarush.island.chesnokov.simulation.Simulation;

public class ConsoleRunner {
    public static void main(String[] args){
        Island island = new Island(100, 20);

        // Заселяем стартовую точку для теста
        Location start = island.getLocation(0, 0);
        Animal wolf = AnimalFactory.create(AnimalType.WOLF);
        Animal rabbit1 = AnimalFactory.create(AnimalType.RABBIT);
        Animal rabbit2 = AnimalFactory.create(AnimalType.RABBIT);
        Animal fox = AnimalFactory.create(AnimalType.FOX);
        Animal sheep = AnimalFactory.create(AnimalType.SHEEP);

        start.addAnimal(wolf);
        start.addAnimal(rabbit1);
        start.addAnimal(rabbit2);
        start.addAnimal(sheep);
        start.addAnimal(fox);

        wolf.setCurrentLocation(start);
        rabbit1.setCurrentLocation(start);
        rabbit2.setCurrentLocation(start);
        sheep.setCurrentLocation(start);
        fox.setCurrentLocation(start);

        // Запускаем симуляцию
        Simulation simulation = new Simulation(island);
        simulation.start();
    }
}
