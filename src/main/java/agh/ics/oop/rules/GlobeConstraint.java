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

	@Override
	public Vector2d constraints(Animal animal) {
		Vector2d potencial = animal.getPosition().add(new Vector2d(0,0).directionsToVector(animal.getDirection()));
		if(potencial.y < 0 || potencial.y > height){
			animal.reverseDirection();
			return animal.getPosition();
		}
		else{
			return new Vector2d(potencial.x%width,potencial.y);
		}
	}

}
