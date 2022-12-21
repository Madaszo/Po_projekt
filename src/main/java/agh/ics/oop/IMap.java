package agh.ics.oop;

import java.util.ArrayList;
import java.util.Map;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 *
 */
public interface IMap {

    /**
     * Place an object on the map.
     *
     * @param object
     *            The {@link IMapElement} to place on the map.
     * @throws RuntimeException Throws exception if the object couldn't place correctly
     */
    void place(IMapElement object);

    /**
     * Remove an object from the map.
     *
     * @param object
     *            The {@link IMapElement} to remove from the map.
     * @throws RuntimeException Throws exception if the object couldn't place correctly
     */
    void remove(IMapElement object) throws Exception;

    /**
     * An Animal has changed position, so it notifies this map
     * @param movedAnimal The animal that changed position
     * @param oldPosition The position the animal left
     * @param newPosition The position the animal moved to
     */
    void positionChanged(Animal movedAnimal, Vector2d oldPosition, Vector2d newPosition);
    void randomAnimals(int n);
    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);
    Map<Vector2d, ArrayList<Animal>> getAnimals();
    int getGrassGain();
    int mutate();
    ArrayList<Animal> animalsAt(Vector2d position);
    /**
     * Return a string representation of the tile on this position
     *
     * @return String representation of this position
     */
    int getHeight();
    int getWidth();
    boolean isOccupiedByGrass(Vector2d position);
    Grass getGrass(Vector2d position);
    int getEnergyGain();
    String positionRepresentation(Vector2d position);

    /**
     * Spawn n grass patches on the map.
     *
     * @param n Number of grass patches to spawn
     */
    void spawnGrass(int n);
    Vector2d move(Animal animal);

    void nextGene(Animal animal);

    int getGrassesNum();

    int getFreeTilesNum();
}