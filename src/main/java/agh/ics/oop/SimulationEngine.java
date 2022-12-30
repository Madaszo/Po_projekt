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
        }
    }

    @Override
    public void grassify() {
        map.spawnGrass(map.getGrassGain());
    }

    @Override
    public void eat() throws Exception {
        for(Animal animal: animals){
            animal.eat();
        }

    }

    @Override
    public void procreate() {
        ArrayList<Animal> babies = new ArrayList<>();
        for(Animal animal: animals){
            Animal baby = animal.procreate();
            if(baby != null){
                babies.add(baby);
                mapStats.animalBorn(baby);
            }
        }
        animals.addAll(babies);
    }

    @Override
    public void run() {
        while (true){
            animals.sort(Comparator.comparing(Animal::getEnergy));
            Collections.reverse(animals);
            try {
                killAnimals();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            moveAnimals();
            try {
                eat();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            procreate();
            grassify();
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
