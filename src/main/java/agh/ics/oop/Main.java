package agh.ics.oop;

import agh.ics.oop.rules.DeterministicGenomeExecutioner;
import agh.ics.oop.rules.FullRandomMutationer;
import agh.ics.oop.rules.GlobeConstraint;
import agh.ics.oop.rules.GreenEquator;

public class Main {
	public static void main(String[] args) {
		final IMap map = new WorldMap(10, 10,
				new GreenEquator(),
				new FullRandomMutationer(),
				new DeterministicGenomeExecutioner(),
				new GlobeConstraint(10, 10));
		//coś tam
	}

}
