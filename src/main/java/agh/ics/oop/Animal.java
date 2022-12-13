package agh.ics.oop;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;

public class Animal implements IMapElement{
    private final IMap map;
    private MapDirection direction = MapDirection.NORTH;
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
            case NORTH -> {
                return "^";
            }
            case SOUTH -> {
                return "v";
            }
            case WEST -> {
                return "<";
            }
            case EAST -> {
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
    public void move(Gene direction) throws FileNotFoundException {
        switch (direction) {
            case LEFT -> {
                this.direction = this.direction.previous();
                positionChanged(this.position,this.position);
            }

            case RIGHT -> {
                this.direction = this.direction.next();
                positionChanged(this.position,this.position);
            }
            case FORWARD -> {
                Vector2d tmp = this.direction.toUnitVector().add(this.position);
                if(this.map.canMoveTo(tmp)){
                    boolean b = this.map.eat(tmp);
                    positionChanged(this.position,tmp);
                    this.position = tmp;
                    if(b){
                        this.map.grassify();
                    }
                }
            }
            case BACKWARD -> {
                Vector2d tmp = this.direction.toUnitVector().opposite().add(this.position);
                if(this.map.canMoveTo(tmp)) {
                    boolean b = this.map.eat(tmp);
                    positionChanged(this.position,tmp);
                    this.position = tmp;
                    if(b){
                        this.map.grassify();
                    }
                }
            }
            case IGNORE -> {
            }
        }
    }
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
    public void massMove(MoveDirection[] moveDirections) throws FileNotFoundException {
        for(MoveDirection moveDirection: moveDirections){
            this.move(moveDirection);
        }
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
