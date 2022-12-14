package agh.ics.oop;

import agh.ics.oop.rules.IRuleGenomeExecution;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Animal implements IMapElement{
    private final IMap map;
    private MapDirection direction;
    private int age;
    private int energy;
    private Vector2d position;
    private IRuleGenomeExecution IRGE;

    // just an example, we haven't discussed in what form we want genomes to be
    public int currentGene = 0;
    int[] genome = {0, 1, 2, 3};
    //

    ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    public Animal(IMap map, Vector2d initialPosition,IRuleGenomeExecution IRGE, int energy){
        this.map = map;
        this.position = initialPosition;
        this.map.place(this);
        this.IRGE = IRGE;
        this.age = 0;
        this.energy = energy;
    }

    public String toString() {
        return direction.toString();
    }
    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public String getPath() {
        return switch (direction){
            case EAST -> "src/main/resources/E.png";
            case NORTHEAST -> "src/main/resources/NE.png";
            case WEST -> "src/main/resources/W.png";
            case NORTHWEST -> "src/main/resources/NW.png";
            case SOUTH -> "src/main/resources/S.png";
            case SOUTHEAST -> "src/main/resources/SE.png";
            case SOUTHWEST -> "src/main/resources/SW.png";
            case NORTH -> "src/main/resources/N.png";
        };
    }

    public MapDirection getDirection(){
        return this.direction;
    }
    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }
    public void move() throws FileNotFoundException {
        int d = genome[currentGene];
        if (d == 0) {
            Vector2d newPosition = this.map.move(this);
            positionChanged(this.position, newPosition);
            this.position = newPosition;
        } else {
            this.direction = this.direction.rotate(d);
            positionChanged(this.position, this.position);
        }
        this.IRGE.nextGene(this);
    }
    public void reverseDirection(){this.direction = this.direction.rotate(4);}
    public String getLabel(){
        return getPosition().toString();
    }
    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for(IPositionChangeObserver observer : observers){
            observer.positionChanged(oldPosition,newPosition);
        }
    }
    void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

	// required by IRuleGenomeExecutioner (unless we change the way it interacts with animals)

}

// needed to compare animals in TreeSet in map;
