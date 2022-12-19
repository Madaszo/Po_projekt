package agh.ics.oop;

public interface IPositionChangeObserver {
    void positionChanged(Animal movedAnimal, Vector2d oldPosition, Vector2d newPosition);
}
