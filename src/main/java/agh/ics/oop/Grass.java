package agh.ics.oop;

public class Grass implements IMapElement {
    private final Vector2d position;
    public Grass(Vector2d position){
        this.position = position;
    }

    @Override
    public String getPath() {
        return "src/main/resources/grass.png";
    }

    @Override
    public String getLabel() {
        return "Grass";
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public String toString() {
        return "*";
    }
}
