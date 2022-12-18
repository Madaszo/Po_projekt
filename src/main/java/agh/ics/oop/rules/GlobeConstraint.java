package agh.ics.oop.rules;

import agh.ics.oop.Animal;
import agh.ics.oop.Vector2d;

public class GlobeConstraint extends AbstractRuleMoveConstraints {

	public GlobeConstraint(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public Vector2d constraints(Animal animal) {
		Vector2d potential = animal.getPosition().add(animal.getDirection().directionsToVector());
		if(potential.y < 0 || potential.y > height){
			animal.reverseDirection();
			return animal.getPosition();
		}
		else{
			int tmp = potential.x;
			while (tmp < 0) {
				tmp += width;
			}
			return new Vector2d(tmp%width, potential.y);
		}
	}
}
