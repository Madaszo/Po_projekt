package agh.ics.oop;

import java.util.*;

public class SimulationEngine implements IEngine{
    List<Animal> animals = new ArrayList<>();
    public final IMap map;
    public final MapStats mapStats;
    public SimulationEngine(IMap map){
        System.out.println(map.getAnimals());
        this.map = map;

        this.mapStats = new MapStats(this, this.map);
        for(Map.Entry<Vector2d,ArrayList<Animal>> entry: this.map.getAnimals().entrySet()){
            ArrayList<Animal> mAnimals = entry.getValue();
            if(!mAnimals.isEmpty()) {
                for (Animal animal : mAnimals) {
                    System.out.println(animal);
                    animals.add(animal);
                }
            }
        }
    }

    @Override
    public void run(int i) throws Exception {
        for(int j = 0; j < i; j++){
            animals.sort(Comparator.comparing(Animal::getEnergy));
            Collections.reverse(animals);
            killAnimals();
            moveAnimals();
            System.out.println(animals);
            System.out.println(j);
            eat();
            procreate();
            grassify();
            System.out.println(map);
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
            System.out.println(animal.getEnergy());
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
}
