package com.javarush.island.chesnokov;

import com.javarush.island.chesnokov.map.Island;
import com.javarush.island.chesnokov.simulation.IslandPopulator;
import com.javarush.island.chesnokov.simulation.Simulation;

public class ConsoleRunner {
    public static void main(String[] args){
        Island island = new Island(100, 20);

        IslandPopulator.populateIsland(island);

        Simulation simulation = new Simulation(island);
        simulation.start();
    }
}
