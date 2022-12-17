package agh.ics.oop;

import com.sun.source.tree.WhileLoopTree;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IEngine{
    List<Animal> animals = new ArrayList<>();
    public IMap map;
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

    }
}
