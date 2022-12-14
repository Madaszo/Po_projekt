package agh.ics.oop.rules;

import agh.ics.oop.Animal;
import agh.ics.oop.Vector2d;

public interface IRuleMoveConstraints {
	void animalMoved(Animal animal, Vector2d position);
	Vector2d constraints(Animal animal);
}
