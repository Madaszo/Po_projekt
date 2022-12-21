package agh.ics.oop;

import agh.ics.oop.rules.*;


public class Main {
	public static void main(String[] args) {
		try {
			WorldMap map = new WorldMap(10, 10, 3, 3,
					new GreenEquator(),
					new FullRandomMutationer(),
					new DeterministicGenomeExecutioner(),
					new GlobeConstraint(10, 10));
			map.spawnGrass(10);
			Animal dog = new Animal(map, new Vector2d(0, 1), 20, new int[]{2, 1, 3, 7});
			Animal pig = new Animal(map, new Vector2d(1, 0), 30, new int[]{0, 0, 4, 2});
			map.randomAnimals(10);
			System.out.println(map.animals);
			SimulationEngine SE = new SimulationEngine(map);
			SE.run(31);

		}
		catch (Exception exception){
			System.out.println(exception);
		}
	}

}
