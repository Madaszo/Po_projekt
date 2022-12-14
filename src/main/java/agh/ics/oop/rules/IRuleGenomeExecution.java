package agh.ics.oop.rules;

import agh.ics.oop.Animal;

public interface IRuleGenomeExecution {
	// todo Should this method operate directly on animal or should it just inform animal which gene should it do now?
	void nextGene(Animal animal);
}
