package agh.ics.oop;

import java.util.HashMap;

/**
 * This object stores statistics of one {@link SimulationEngine} and its {@link IMap} object
 */
public class MapStats {
	final private IEngine engine;
	final private IMap map;
	private int simulationTime = 0;
	private int numOfAnimals = 0;
	public int sumEnergy = 0;
	private int numOfDeadAnimals = 0;
	private int sumLifespan = 0;
	private final HashMap<int[], Integer> genomesPopularity = new HashMap<>();

	public MapStats(IEngine engine, IMap map) {
		this.engine = engine;
		this.map = map;
	}
	public int getSimulationTime() {
		return simulationTime;
	}
	public int getNumOfAnimals() {
		return numOfAnimals;
	}
	public int getNumOfGrasses() {
		return this.map.getGrassesNum();
	}
	public int getFreeTilesNum() {
		return this.map.getFreeTilesNum();
	}
	public double getAvgEnergy() {
		return (double) this.sumEnergy / this.numOfAnimals;
	}
	public double getAvgLifespan() {
		return (double) this.sumLifespan / this.numOfDeadAnimals;
	}
	public int[] getMostPopularGenome() {
		return genomesPopularity.entrySet().stream().max((ent1, ent2) -> ent1.getValue() - ent2.getValue()).get().getKey();
	}
	public int getMostPopularGenomeNum() {
		return genomesPopularity.entrySet().stream().max((ent1, ent2) -> ent1.getValue() - ent2.getValue()).get().getValue();
	}

	public void addSimulationTime() {
		simulationTime++;
	}

	public void animalBorn(Animal animal) {
		this.numOfAnimals++;
		int[] genome = animal.getGenome();

		genomesPopularity.putIfAbsent(genome, 0);
		genomesPopularity.put(genome, genomesPopularity.get(genome) + 1);
	}

	public void animalAboutToDie(Animal animal) {
		this.numOfAnimals--;
		this.numOfDeadAnimals++;
		this.sumLifespan += animal.getAge();

		int[] genome = animal.getGenome();

		genomesPopularity.replace(genome, genomesPopularity.get(genome) - 1);
	}

	public void deltaSumEnergy(int energyChange) {
		this.sumEnergy += energyChange;
	}
}
