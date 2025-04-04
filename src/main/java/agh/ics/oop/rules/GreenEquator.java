package agh.ics.oop.rules;

import agh.ics.oop.Grass;
import agh.ics.oop.IMap;
import agh.ics.oop.Vector2d;

import java.util.Random;

public class GreenEquator implements IRuleSpawnGrass{
	@Override
	public void findTilesToSpawnGrass(int n, IMap map) {
		// todo Implement way of choosing appropiate grass places
		int h = map.getHeight();
		int w = map.getWidth();
		int j,y,lic;
		Vector2d tmp;
		for(int i = 0; i<n;i++){
			lic = 0;
			do {
				lic++;
				Random rand = new Random();
				j = rand.nextInt(100);
				if (j > 80) {
					do {
						y = rand.nextInt(h);
					} while ((y > (h / 2) - (h  / 10)) && (y < (h / 2) + (h / 10)));
					tmp = new Vector2d(rand.nextInt(w), y);
				}else{
					do {
						y = rand.nextInt(h);
					} while (!((y >= (h / 2) - (h  / 10)) && (y < (h / 2) + (h / 10))));
					tmp = new Vector2d(rand.nextInt(w), y);
				}
				if(lic>100){
					return;
				}
			} while (map.isOccupiedByGrass(tmp));
			map.place(new Grass(tmp));
		}
	}

	@Override
	public Vector2d[] greenerGrass(IMap map) {
		Vector2d[] w = new Vector2d[2];
		int h =map.getHeight();
		int wi = map.getWidth();
		w[0] = new Vector2d(0,(h/2)-(h/10));
		w[1] = new Vector2d(wi,(h/2)+(h/10)-1);
		return w;
	}
}
