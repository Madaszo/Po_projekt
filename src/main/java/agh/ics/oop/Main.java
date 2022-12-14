package agh.ics.oop;

import agh.ics.oop.rules.*;

public class Main {
	public static void main(String[] args) {
		IRuleGenomeExecution IRGE = new DeterministicGenomeExecutioner();
		final WorldMap map = new WorldMap(10, 10,
				new GreenEquator(),
				new FullRandomMutationer(),
				IRGE,
				new GlobeConstraint(10, 10));

		Animal dog = new Animal(map, new Vector2d(0, 0), IRGE, 20);
		Animal pig = new Animal(map, new Vector2d(1, 0), IRGE, 30);


		System.out.println(map);
	}

}
