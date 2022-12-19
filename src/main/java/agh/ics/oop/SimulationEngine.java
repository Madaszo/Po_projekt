package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IEngine{
    List<Animal> animals = new ArrayList<>();
    public final IMap map;
    public final MapStats mapStats;
    public SimulationEngine(IMap map,int animalsNumber){

    }

    @Override
    public void run() {
        while(true){
            killAnimals();
            moveAnimals();
            eat();
            procreate();
            grassify();
        }
    }

    @Override
    public void killAnimals() {
        for (Animal animal: animals) {
            if (animal.getEnergy()==0){
                map.removeAnimal(animal);
            }
        }
    }

    @Override
    public void moveAnimals() {

    }

    @Override
    public void grassify() {

    }

    @Override
    public void eat() {

    }

    @Override
    public void procreate() {

        this.map = map;
        this.mapStats = new MapStats((IEngine) this, this.map);
    }
}
