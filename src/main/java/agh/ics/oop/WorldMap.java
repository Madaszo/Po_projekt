package agh.ics.oop;

import agh.ics.oop.rules.IRuleGenomeExecution;
import agh.ics.oop.rules.IRuleMoveConstraints;
import agh.ics.oop.rules.IRuleMutations;
import agh.ics.oop.rules.IRuleSpawnGrass;

import java.util.*;

// todo WorldMap should implement IPositionChangeObserver
public class WorldMap implements IMap {
	// a lot of animals can be in one position, so we cannot use any sort of HashMap, because we need to keep
	// animals in some kind of order
	// todo Some better way of ordering Animals and their positions?
	private final Map<Vector2d, ArrayList<Animal>> animals = new HashMap<Vector2d, ArrayList<Animal>>();
	final private Map<Vector2d, Grass> grasses = new HashMap<>();
	final int width;
	final int height;
	// map rules (we ask them what to do in certain situations
	IRuleSpawnGrass grassSpawner;
	IRuleMutations mutater;
	IRuleGenomeExecution genomeExecutioner;
	IRuleMoveConstraints moveConstrainer;

	public WorldMap(int width, int height,
					IRuleSpawnGrass grassSpawner,
					IRuleMutations mutater,
					IRuleGenomeExecution genomeExecutioner,
					IRuleMoveConstraints moveConstrainer) {
		this.width = width;
		this.height = height;
		this.grassSpawner = grassSpawner;
		this.mutater = mutater;
		this.genomeExecutioner = genomeExecutioner;
		this.moveConstrainer = moveConstrainer;

		// animals initialisation
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				animals.put(new Vector2d(i, j), new ArrayList<>());
			}

		}
	}


	public void place(IMapElement mapOb) {
		if (mapOb instanceof Animal) {
			animals.get(mapOb.getPosition()).add((Animal) mapOb);
		}

		if (mapOb instanceof Grass) {
			if (!isOccupiedByGrass(mapOb.getPosition())) {
				grasses.put(mapOb.getPosition(), (Grass) mapOb);
			} else {
				throw new RuntimeException("The Grass object couldn't have been added, because there is already a grass object on position: " + mapOb);
			}
		}
		throw new RuntimeException("Object " + mapOb + "is not of instance Animal or Grass, so map doesn't know what to do");
	}

	/* not necessary right now
	 * An Animal has changed position, so he notified this map
	 * @param oldPosition the position the object left
	 * @param newPosition the position the object moved to

	public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
		Animal animal = animals.get(oldPosition);
		animals.remove(oldPosition);
		animals.put(newPosition, animal);
		mapBoundary.positionChanged(oldPosition, newPosition);
		hasMovedTo(newPosition);
	}*/

	public boolean isOccupied(Vector2d position) {
		return isOccupiedByAnimal(position) || isOccupiedByGrass(position);
	}

	public Object objectAt(Vector2d position) {
		final Object ob = animalsAt(position);
		if (ob != null) {
			return ob;
		}
		return grassAt(position);
	}

	@Override
	public void spawnGrass(int n) {
		for (Vector2d position : grassSpawner.findTilesToSpawnGrass(n)) {
			this.place(new Grass(position));
		}
	}

	@Override
	public Vector2d move(Animal animal) {
		return moveConstrainer.constraints(animal);
	}

	public boolean isOccupiedByAnimal(Vector2d position) {
		return animalsAt(position) != null;
	}

	public List<Animal> animalsAt(Vector2d position) {
		if (!animals.get(position).isEmpty()) {
			return Collections.unmodifiableList(animals.get(position));
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
		return mapVisualizer.draw(new Vector2d(0, 0), new Vector2d(width, height));
	}

}
