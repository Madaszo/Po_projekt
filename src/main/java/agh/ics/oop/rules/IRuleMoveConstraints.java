package agh.ics.oop.rules;

import agh.ics.oop.Animal;
import agh.ics.oop.Vector2d;

public interface IRuleMoveConstraints {
	/**
	 * Decides (according to used logic) what happens with an animal that wants to move
	 * @param animal An animal that wants to move
	 * @return The position the animal will end on
	 */
	Vector2d constraints(Animal animal);
}
