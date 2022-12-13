package agh.ics.oop.rules;

import agh.ics.oop.Animal;
import agh.ics.oop.Vector2d;

public class GlobeConstraint extends AbstractRuleMoveConstraints {

	public GlobeConstraint(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void animalMoved(Animal animal, Vector2d position) {

	}
}
