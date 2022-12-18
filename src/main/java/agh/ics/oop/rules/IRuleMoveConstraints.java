package agh.ics.oop.rules;

import agh.ics.oop.Animal;
import agh.ics.oop.Vector2d;

public interface IRuleMoveConstraints {
	Vector2d constraints(Animal animal);
}
