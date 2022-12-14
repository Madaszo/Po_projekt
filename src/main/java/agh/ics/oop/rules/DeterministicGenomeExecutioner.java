package agh.ics.oop.rules;

import agh.ics.oop.Animal;

public class DeterministicGenomeExecutioner implements IRuleGenomeExecution{

	@Override
	public void nextGene(Animal animal) {
		animal.currentGene = (animal.currentGene+1)%8;
	}
}
