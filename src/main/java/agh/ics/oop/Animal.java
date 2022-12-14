package agh.ics.oop;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;

public class Animal implements IMapElement{
    private final IMap map;
    private int direction = 0;
    private Vector2d position;

    // just an example, we haven't discussed in what form we want genomes to be
    int currentGene = 0;
    int[] genome = {0, 1, 2, 3};
    //

    ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    public Animal(IMap map, Vector2d initialPosition){
        this.map = map;
        this.position = initialPosition;
        this.map.place(this);
    }
    public Animal(IMap map){
        this(map,new Vector2d(2,2));
    }

    public String toString() {
        switch (this.direction){
            case 0 -> {
                return "^";
            }
            case 4 -> {
                return "v";
            }
            case 6 -> {
                return "<";
            }
            case 2 -> {
                return ">";
            }
            default -> throw new IllegalStateException("Unexpected value: " + this.direction);
        }

    }
    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public String getPath() {
        return switch (direction){
            case 2 -> "src/main/resources/E.png";
            case 1 -> "src/main/resources/NE.png";
            case 6 -> "src/main/resources/W.png";
            case 7 -> "src/main/resources/NW.png";
            case 4 -> "src/main/resources/S.png";
            case 3 -> "src/main/resources/SE.png";
            case 5 -> "src/main/resources/SW.png";
            case 0 -> "src/main/resources/N.png";
            default -> throw new IllegalStateException("Unexpected direction: "+ this.direction);
        };
    }

    public int getDirection(){
        return this.direction;
    }
    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }
    public void move() throws FileNotFoundException {
        int d = genome[currentGene];
        switch (d) {
            case 0 -> {
                Vector2d newPosition = this.map.move(this);
                positionChanged(this.position, newPosition);
                this.position = newPosition;
            }
            default -> {
                this.direction = (this.direction + direction) % 8;
                positionChanged(this.position, this.position);
            }
        }
        this.nextGene();
    }
    public void reverseDirection(){this.direction = (this.direction+4)%8;}
    public String getLabel(){
        return getPosition().toString();
    }
    void positionChanged(Vector2d oldPosition, Vector2d newPosition) throws FileNotFoundException {
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
    public void nextGene() {
        currentGene = (currentGene + 1) % genome.length;
    }

}

// needed to compare animals in TreeSet in map;
class AnimalComparatorByPositionX implements Comparator<Animal> {
    public int compare(Animal o1, Animal o2) {
        return o1.getPosition().x - o2.getPosition().x;
    }
}
