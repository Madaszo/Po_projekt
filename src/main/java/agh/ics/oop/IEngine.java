package agh.ics.oop;



public interface IEngine {
    void run(int i) throws Exception;
    void killAnimals() throws Exception;
    void moveAnimals();
    void grassify();
    void eat() throws Exception;
    void procreate();
}
