package agh.ics.oop;

import java.util.ArrayList;
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
    private final int fedAnimal;
    private int eatenGrass = 0;
    public int offspringNum = 0;
    private final int neededEnergy;
    ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(IMap map, Vector2d initialPosition, int energy, int[] genome, int fedAnimal,int neededEnergy){
        this.map = map;
        this.position = initialPosition;
        this.map.place(this);
        this.energy = energy;
        this.genome = genome;
        this.currentGene = random.nextInt(map.getGenomeLength());
        this.fedAnimal = fedAnimal;
        this.neededEnergy = neededEnergy;
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
            sodoma.sort(new AnimalComparator());
            if(this.energy > fedAnimal && sodoma.get(1).energy>fedAnimal){
                Animal baby =
                        new Animal(
                                map,
                                this.position,
                                this.energy/2 + sodoma.get(1).energy/2,
                                this.createGenome(sodoma.get(1)), fedAnimal,neededEnergy);
                this.energy -= this.energy/2;
                sodoma.get(1).energy -= sodoma.get(1).getEnergy()/2;
                baby.direction = MapDirection.fromInt(random.nextInt(8));
                offspringNum++;
                return baby;

            }
        }
        return null;
    }

    @Override
    public String getPath() {
        return switch (direction){
            case EAST -> "src\\main\\resources\\images\\E.png";
            case NORTHEAST -> "src\\main\\resources\\images\\NE.png";
            case WEST -> "src\\main\\resources\\images\\W.png";
            case NORTHWEST -> "src\\main\\resources\\images\\NW.png";
            case SOUTH -> "src\\main\\resources\\images\\S.png";
            case SOUTHEAST -> "src\\main\\resources\\images\\SE.png";
            case SOUTHWEST -> "src\\main\\resources\\images\\SW.png";
            case NORTH -> "src\\main\\resources\\images\\N.png";
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

class AnimalComparator implements Comparator<Animal> {

    @Override
    public int compare(Animal a, Animal b) {
        if (a.getEnergy() != b.getEnergy()) {return b.getEnergy() - a.getEnergy();}
        if (a.getAge() != b.getAge()) {return b.getAge() - a.getAge();}
        if (a.offspringNum != b.offspringNum) {return b.offspringNum - a.offspringNum;}
        return 0;
    }

}
