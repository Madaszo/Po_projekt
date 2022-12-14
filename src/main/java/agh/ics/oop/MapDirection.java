package agh.ics.oop;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    @Override
    public String toString() {
        return switch (this){
            case NORTH -> "N ";
            case SOUTH -> "S ";
            case SOUTHWEST -> "SW";
            case SOUTHEAST -> "SE";
            case NORTHWEST -> "NW";
            case WEST -> "W ";
            case NORTHEAST -> "NE";
            case EAST -> "E ";
        };
    }
    public MapDirection rotate(int gene){
        MapDirection n = this;
        for(int i = 0; i < gene; i++){
             n = n.next();
        }
        return n ;
    }
    public MapDirection next(){
        return switch (this){
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHEAST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }
}
