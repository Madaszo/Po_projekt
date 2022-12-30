package agh.ics.oop.rules;

import agh.ics.oop.IMap;
import agh.ics.oop.Vector2d;

public interface IRuleSpawnGrass {
	void findTilesToSpawnGrass(int n, IMap map);
	Vector2d[] greenerGrass(IMap map);
}
