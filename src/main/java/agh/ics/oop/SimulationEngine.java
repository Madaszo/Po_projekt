package agh.ics.oop;

import javafx.application.Platform;

import java.util.*;
import java.util.concurrent.Semaphore;

public class SimulationEngine implements IEngine, Runnable{
    public List<Animal> animals = new ArrayList<>();
    public final IMap map;
    public final MapStats mapStats;
    EngineObserver observer;
    public SimulationEngine(IMap map,EngineObserver observer){
        this.map = map;
        this.observer = observer;
        this.mapStats = new MapStats(this, this.map);
        for(Map.Entry<Vector2d,ArrayList<Animal>> entry: this.map.getAnimals().entrySet()){
            ArrayList<Animal> mAnimals = entry.getValue();
            if(!mAnimals.isEmpty()) {
                for (Animal animal : mAnimals) {
                    animals.add(animal);
                    mapStats.animalBorn(animal);
                    mapStats.deltaSumEnergy(animal.getEnergy());
                }
            }
        }
    }
    @Override
    public void runs(int i) throws Exception {
        for(int j = 0; j < i; j++){
            animals.sort(new AnimalComparator());
            killAnimals();
            moveAnimals();
            //System.out.println(animals);
            //System.out.println(j);
            eat();
            procreate();
            grassify();
//            System.out.println(map);
            mapStats.addSimulationTime();
            observer.updateScene(this);
        }
    }

    @Override
    public void killAnimals() throws Exception {
        ArrayList<Animal> dead = new ArrayList<>();
        for (Animal animal: animals) {
            if (animal.getEnergy()==0){
                dead.add(animal);
            }
        }
        for (Animal animal: dead){
            mapStats.animalAboutToDie(animal);
            map.remove(animal);
            animals.remove(animal);
        }
    }

    @Override
    public void moveAnimals() {
        for(Animal animal: animals){
            animal.move();
            mapStats.deltaSumEnergy(-1);
        }
    }

    @Override
    public void grassify() {
        map.spawnGrass(map.getGrassGain());
    }

    @Override
    public void eat() throws Exception {
        for(Animal animal: animals){
            if (animal.eat()) {
                mapStats.deltaSumEnergy(map.getEnergyGain());
            }
        }

    }

    @Override
    public void procreate() {
        ArrayList<Animal> babies = new ArrayList<>();
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++){
                ArrayList<Animal> animals = map.animalsAt(new Vector2d(i, j));
                if (animals != null && animals.size() > 1) {
                    animals.sort(new AnimalComparator());
                    Animal baby = animals.get(0).procreate();
                    if (baby != null) {
                        babies.add(baby);
                        mapStats.animalBorn(baby);
                    }
                }
            }
        }
        animals.addAll(babies);
    }

    @Override
    public void run() {
        while (true){
            try {
                killAnimals();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            moveAnimals();
            animals.sort(new AnimalComparator());
            try {
                eat();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            animals.sort(new AnimalComparator());
            procreate();
            grassify();
            mapStats.addSimulationTime();
            observer.updateScene(this);
            try {
                waitForRunLater();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();

    }
}
