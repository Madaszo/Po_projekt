package agh.ics.oop;

import agh.ics.oop.rules.IRuleGenomeExecution;
import agh.ics.oop.rules.IRuleMoveConstraints;
import agh.ics.oop.rules.IRuleMutations;
import agh.ics.oop.rules.IRuleSpawnGrass;

import java.util.*;

public class WorldMap implements IMap, IPositionChangeObserver {
	public Map<Vector2d, ArrayList<Animal>> animals = new HashMap<>();
	final private Map<Vector2d, Grass> grasses = new HashMap<>();
	final int width;
	final int height;
	int freeTiles;
	int genomeLength = 8;
	int startingEnergy = 10;
	final int energyGain;
	final int grassGain;

	// map rules (we ask them what to do in certain situations)
	IRuleSpawnGrass grassSpawner;
	IRuleMutations mutator;
	IRuleGenomeExecution genomeExecutioner;
	IRuleMoveConstraints moveConstrainer;

	public WorldMap(int width, int height, int grassGain, int energyGain,
					IRuleSpawnGrass grassSpawner,
					IRuleMutations mutator,
					IRuleGenomeExecution genomeExecutioner,
					IRuleMoveConstraints moveConstrainer) {
		this.width = width;
		this.height = height;
		this.freeTiles = width * height;
		this.grassGain = grassGain;
		this.energyGain = energyGain;

		// map rules
		this.grassSpawner = grassSpawner;
		this.mutator = mutator;
		this.genomeExecutioner = genomeExecutioner;
		this.moveConstrainer = moveConstrainer;

		// animals HashMap initialisation
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				animals.put(new Vector2d(i, j), new ArrayList<>());
			}

		}
	}
	public int mutate(){return mutator.mutate();}
	@Override
	public int getGrassGain() {
		return grassGain;
	}



	public int getHeight(){return height;}
	public int getWidth(){return width;}

	public int getEnergyGain() {return energyGain;}

	@Override
	public Grass getGrass(Vector2d position) {
		return grasses.get(position);
	}

	@Override
	public Map<Vector2d, ArrayList<Animal>> getAnimals() {
		return animals;
	}

	public void place(IMapElement mapOb) throws IllegalArgumentException {
		if (mapOb.getClass() == Animal.class) {
			try {
				ArrayList<Animal> array = animals.get(mapOb.getPosition());
				if (array.isEmpty()) {
					freeTiles--;
				}
				array.add((Animal) mapOb);
				((Animal) mapOb).addObserver(this);
				return;
			} catch(IllegalArgumentException e) {
				throw new IllegalArgumentException("The animal couldn't have been added, because of an unknown error");
			}
		}

		if (mapOb.getClass() == Grass.class) {
			if (!isOccupiedByGrass(mapOb.getPosition())) {
				freeTiles--;
				grasses.put(mapOb.getPosition(), (Grass) mapOb);
				return;
			} else {
				throw new IllegalArgumentException("The Grass object couldn't have been added, because there already is a grass object on position: " + mapOb);
			}
		}
		throw new IllegalArgumentException("Object " + mapOb + "is not of instance Animal or Grass, so map doesn't know how to place it");
	}

	@Override
	public void randomAnimals(int n) {
		Random random = new Random();
		int[] genome = new int[genomeLength];
		for(int i = 0; i < n; i++){
			for (int j = 0; j < genomeLength; j++){
				genome[j] = random.nextInt(8);
			}
			Animal animal = new Animal(this,
										new Vector2d(random.nextInt(width),
										random.nextInt(height)),
										startingEnergy,genome);
		}
	}

	@Override
	public void remove(IMapElement mapOb) throws IllegalArgumentException {
		if (mapOb.getClass() == Animal.class) {
			try {
				ArrayList<Animal> array = animals.get(mapOb.getPosition());
				if (array.size() == 1) {
					freeTiles++;
				}
				array.remove((Animal) mapOb);
				return;
			} catch(Exception e) {
				throw new IllegalArgumentException("Animal " + mapOb + "couldn't have been removed from the map, because it's not assigned to this map");
			}
		}

		if (mapOb.getClass() == Grass.class) {
			try {
				freeTiles--;
				grasses.remove(mapOb.getPosition(), (Grass) mapOb);
				return;
			} catch (Exception e) {
				throw new IllegalArgumentException("The Grass object " + mapOb + " couldn't have been removed, because it's not assigned to this map");
			}
		}
		throw new IllegalArgumentException("Object " + mapOb + "is not of instance Animal or Grass, so map cannot delete it");
	}

	public void positionChanged(Animal movedAnimal, Vector2d oldPosition, Vector2d newPosition) {
		animals.get(oldPosition).remove(movedAnimal);
		System.out.println(newPosition);
		animals.get(newPosition).add(movedAnimal);
	}

	public boolean isOccupied(Vector2d position) {
		return isOccupiedByAnimal(position) || isOccupiedByGrass(position);
	}

	public String positionRepresentation(Vector2d position) {
		final List<Animal> anims = animalsAt(position);
		if (anims == null) {
			return (grassAt(position) == null) ? "  " : grassAt(position).toString() + " ";
		}
		if (anims.size() == 1) {
			return anims.get(0).toString();
		}
		if (anims.size() >= 2) {
			return anims.size() + " ";
		}
		return null;
	}

	@Override
	public void spawnGrass(int n) {
		grassSpawner.findTilesToSpawnGrass(n,this);
	}

	public void nextGene(Animal animal) {
		genomeExecutioner.nextGene(animal);
	}

	@Override
	public int getGrassesNum() {
		return grasses.size();
	}

	@Override
	public int getFreeTilesNum() {
		return freeTiles;
	}

	@Override
	public Vector2d move(Animal animal) {
		return moveConstrainer.constraints(animal);
	}

	public boolean isOccupiedByAnimal(Vector2d position) {
		return animalsAt(position) != null;
	}

	public ArrayList<Animal> animalsAt(Vector2d position) {
		if (!animals.get(position).isEmpty()) {
			return (animals.get(position));
		}
		return null;
	}

	public boolean isOccupiedByGrass(Vector2d position) {
		return grassAt(position) != null;
	}

	public Grass grassAt(Vector2d position) {
		return grasses.get(position);
	}

	public String toString() {
		MapVisualizer mapVisualizer = new MapVisualizer(this);
		return mapVisualizer.draw(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
	}

}
