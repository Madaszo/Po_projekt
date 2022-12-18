package agh.ics.oop;

import agh.ics.oop.rules.*;

import java.io.FileNotFoundException;

public class Main {
	public static void main(String[] args) {
		final WorldMap map = new WorldMap(10, 10,
				new GreenEquator(),
				new FullRandomMutationer(),
				new DeterministicGenomeExecutioner(),
				new GlobeConstraint(10, 10));

		Animal dog = new Animal(map, new Vector2d(0, 0), 20, new int[]{0, 1, 2, 3});
		Animal pig = new Animal(map, new Vector2d(1, 0), 30, new int[]{0, 0});

		try {
			System.out.println(map);
			dog.move();
			System.out.println(map);
			pig.move();
			System.out.println(map);
			dog.move();
			System.out.println(map);
			pig.move();
			System.out.println(map);
			dog.move();
			System.out.println(map);
			pig.move();


		} catch (FileNotFoundException e) {
			System.out.println("ERROR!");
		}

		System.out.println(map);
	}

}
