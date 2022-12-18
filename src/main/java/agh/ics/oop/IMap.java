package agh.ics.oop;

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
     *            The animal to place on the map.
     * @throws RuntimeException Throws exception if the object couldn't place correctly
     */
    void place(IMapElement object);

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

    /**
     * Return a string representation of the tile on this position
     *
     * @param position
     *            We want the string representation of this position.
     * @return String representation of this position
     */
    String positionRepresentation(Vector2d position);

    /**
     * Spawn n grass patches on the map.
     *
     * @param n Number of grass patches to spawn
     */
    void spawnGrass(int n);
    Vector2d move(Animal animal);

	void nextGene(Animal animal);
}