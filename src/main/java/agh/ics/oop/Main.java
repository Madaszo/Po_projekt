package agh.ics.oop;

import agh.ics.oop.rules.*;


public class Main {
	public static void main(String[] args) {
		try {
			WorldMap map = new WorldMap(10, 10, 3, 3,8,10,
					new GreenEquator(),
					new FullRandomMutationer(),
					new DeterministicGenomeExecutioner(),
					new GlobeConstraint(10, 10));
			map.spawnGrass(10);
			Animal dog = new Animal(map, new Vector2d(0, 1), 20, new int[]{2, 1, 3, 7, 6, 9, 4, 2});
			Animal pig = new Animal(map, new Vector2d(1, 0), 30, new int[]{0, 0, 4, 2, 2, 1, 3, 7});
			map.randomAnimals(100);
			System.out.println(map);
			SimulationEngine SE = new SimulationEngine(map);
			SE.run(31);

		}
		catch (Exception exception){
			System.out.println(exception);
		}
	}

}
