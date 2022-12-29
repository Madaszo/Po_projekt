package agh.ics.oop;

import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;
    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }
    public String toString() {
        return '('+String.valueOf(this.x)+','+ this.y +')';
    }
    public boolean precedes(Vector2d other){
        return (this.x <= other.x) && (this.y <= other.y);
    }
    public boolean follows(Vector2d other){
        return (this.x >= other.x) && (this.y >= other.y);
    }
    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }
    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }
    public Vector2d upperRight(Vector2d other){
        return new Vector2d(Math.max(this.x , other.x), Math.max(this.y,other.y));
    }
    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(Math.min(this.x , other.x), Math.min(this.y,other.y));
    }
    public Vector2d opposite(){
        return new Vector2d(-1*this.x,-1*this.y);
    }
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Vector2d v2 = (Vector2d) other;
        return (this.x == v2.x) && (this.y == v2.y);
    }
    public boolean between(Vector2d[] vectors){
        System.out.println("=============");
        System.out.println(this);
        System.out.println(vectors[0].lowerLeft(vectors[1]).lowerLeft(this));
        System.out.println(vectors[0].lowerLeft(vectors[1]));
        System.out.println(vectors[0].upperRight(vectors[1]).upperRight(this));
        System.out.println(vectors[0].upperRight(vectors[1]));

        if(vectors[0].lowerLeft(vectors[1]).lowerLeft(this).equals(vectors[0].lowerLeft(vectors[1])) &&
                vectors[0].upperRight(vectors[1]).upperRight(this).equals(vectors[0].upperRight(vectors[1]))){
            return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
