package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    List<Animal> animals = new ArrayList<>();
    public final IMap map;
    public final MapStats mapStats;
    public SimulationEngine(IMap map,int animalsNumber){
        this.map = map;
        this.mapStats = new MapStats((IEngine) this, this.map);
    }

}
