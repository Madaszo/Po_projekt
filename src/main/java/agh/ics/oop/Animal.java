package agh.ics.oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Animal implements IMapElement{
    private final IMap map;
    private Vector2d position;
    private final Random random = new Random();
    private MapDirection direction = MapDirection.fromInt(random.nextInt(8));
    final private int[] genome;
    public int currentGene;
    private int energy;
    private int age = 0;
    private int eatenGrass = 0;
    public int OffspringNum = 0;
    ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(IMap map, Vector2d initialPosition, int energy, int[] genome){
        this.map = map;
        this.position = initialPosition;
        this.map.place(this);
        this.energy = energy;
        this.genome = genome;
        this.currentGene = random.nextInt(map.getGenomeLength());
    }
    public int getEnergy(){return energy;}
    public String toString() {
        return direction.toString();
    }
    public Vector2d getPosition(){
        return this.position;
    }


    public int[] createGenome(Animal a){
        int[] r = new int[genome.length];
        int n = energy*genome.length/(a.energy+energy);
        for (int i = 0; i < genome.length; i++){
            if (i<n){
                r[i] = genome[i];
            }else {
                r[i] = a.genome[i];
            }
        }
        Random random = new Random();
        int tmp = random.nextInt(genome.length);
        for(int i = 0; i < tmp; i++){
            r[random.nextInt(genome.length)]=map.mutate();
        }
        return r;
    }

    public Animal procreate(){
        if(map.animalsAt(this.getPosition()).size()>1){
            ArrayList<Animal> sodoma = map.animalsAt(this.getPosition());
            sodoma.sort(Comparator.comparing(Animal::getEnergy));
            Collections.reverse(sodoma);
            if(this.energy > 10 && sodoma.get(1).energy>10){
                Animal baby =
                        new Animal(
                                map,
                                this.position,
                                this.energy/2+sodoma.get(1).energy/2,
                                this.createGenome(sodoma.get(1)));
                this.energy/=2;
                sodoma.get(1).energy/=2;
                baby.direction = MapDirection.fromInt(random.nextInt(8));
                OffspringNum++;
                return baby;

            }
        }
        return null;
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

    public int[] getGenome() { return genome; }

    public int getAge(){ return this.age; }

    public int getEatenGrass() { return this.eatenGrass; }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }
    public void eat() throws Exception {
        if (map.isOccupiedByGrass(this.getPosition())){
            map.remove(map.getGrass(this.getPosition()));
            energy += map.getEnergyGain();
            this.eatenGrass++;
        }
    }
    public void move() {
        int d = genome[currentGene];

        // rotate according to currently activated gene
        this.direction = this.direction.rotate(d);

        // move one tile forward according to current orientation
        Vector2d oldPosition = this.position;
        this.position = this.map.move(this);

        // select next gene to read
        this.map.nextGene(this);

        positionChanged(oldPosition, this.position);
        energy--;
        age++;
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
