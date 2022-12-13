package agh.ics.oop.rules;

import agh.ics.oop.Animal;

public class DeterministicGenomeExecutioner implements IRuleGenomeExecution{

	@Override
	public void setNextGene(Animal animal) {
		animal.nextGene();
	}
}
