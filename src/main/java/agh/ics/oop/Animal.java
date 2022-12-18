package agh.ics.oop;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Animal implements IMapElement{
    // with default values
    private MapDirection direction = MapDirection.NORTH;
    private int age = 0;
    public int currentGene = 0;
    ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    // without default values
    final private int[] genome;
    private final IMap map;
    private int energy;
    private Vector2d position;

    public Animal(IMap map, Vector2d initialPosition, int energy, int[] genome){
        this.map = map;
        this.position = initialPosition;
        this.map.place(this);
        this.energy = energy;
        this.genome = genome;
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

    public int getGenomeLength() {
        return genome.length;
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }
    public void move() throws FileNotFoundException {
        int d = genome[currentGene];

        // rotate according to currently activated gene
        this.direction = this.direction.rotate(d);

        // move one tile forward according to current orientation
        Vector2d oldPosition = this.position;
        this.position = this.map.move(this);

        // select next gene to read
        this.map.nextGene(this);

        positionChanged(oldPosition, this.position);
    }
    public void reverseDirection(){this.direction = this.direction.rotate(4);}
    public String getLabel(){
        return getPosition().toString();
    }
    void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for(IPositionChangeObserver observer : observers){
            observer.positionChanged(this, oldPosition, newPosition);
        }
    }
    void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }
}
