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
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    public Vector2d directionsToVector(){
        return switch (this){
            case NORTH -> new Vector2d(0,1);
            case NORTHEAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1,0);
            case SOUTHEAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case SOUTHWEST -> new Vector2d(-1,-1);
            case WEST -> new Vector2d(-1,0);
            case NORTHWEST -> new Vector2d(-1,1);
        };
    }
}
