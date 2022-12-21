package agh.ics.oop;

import agh.ics.oop.rules.DeterministicGenomeExecutioner;
import agh.ics.oop.rules.FullRandomMutationer;
import agh.ics.oop.rules.GlobeConstraint;
import agh.ics.oop.rules.GreenEquator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnimalTest {
    IMap map = new WorldMap(10, 10, 3, 3, 6, 10,
            new GreenEquator(),
            new FullRandomMutationer(),
            new DeterministicGenomeExecutioner(),
            new GlobeConstraint(10, 10));

    @Test
    public void testCreateGenome() {
        Animal animal1 = new Animal(map, new Vector2d(0, 0), 40, new int[]{1, 2, 3, 4, 5, 6});
        Animal animal2 = new Animal(map, new Vector2d(0, 0), 20, new int[]{0, 0, 0, 0, 0, 0});
        int[] randGenome = animal1.createGenome(animal2);
        Assertions.assertEquals(6, randGenome.length);
    }

    @Test
    public void testProcreate() {
        Animal animal1 = new Animal(map, new Vector2d(1, 1), 30, new int[]{1, 2, 3, 4, 5, 6});
        Animal animal2 = new Animal(map, new Vector2d(1, 1), 19, new int[]{1, 2, 3, 4, 5, 6});
        Animal baby = animal1.procreate();

        Assertions.assertNotNull(baby);
        Assertions.assertEquals(baby.getEnergy(), animal1.getEnergy() + animal2.getEnergy());
    }

    @Test
    public void testGetPath() {
        Animal animal1 = new Animal(map, new Vector2d(2, 2), 30, new int[]{1, 2, 3, 4, 5, 6});
        Assertions.assertNotNull( animal1.getPath());
    }

    @Test
    public void testEat() throws Exception {
        Animal animal1 = new Animal(map, new Vector2d(3, 3), 30, new int[]{1, 2, 3, 4, 5, 6});
        int grassEaten = animal1.getEatenGrass();
        int energy = animal1.getEnergy();

        animal1.eat();

        Assertions.assertEquals(grassEaten, animal1.getEatenGrass());
        Assertions.assertEquals(energy, animal1.getEnergy());

        map.place(new Grass(new Vector2d(3, 3)));

        animal1.eat();

        Assertions.assertEquals(grassEaten, animal1.getEatenGrass() - 1);
        Assertions.assertEquals(energy, animal1.getEnergy() - map.getGrassGain());
    }

    @Test
    public void testMove() {
        Animal animal1 = new Animal(map, new Vector2d(3, 3), 30, new int[]{1, 2, 3, 4, 5, 6});
        int age = animal1.getAge();
        int energy = animal1.getEnergy();

        animal1.move();

        Assertions.assertEquals(age, animal1.getAge() - 1);
        Assertions.assertEquals(energy, animal1.getEnergy() + 1);
    }

}
