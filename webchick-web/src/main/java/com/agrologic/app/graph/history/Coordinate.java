

package com.agrologic.app.graph.history;

public class Coordinate<T extends Comparable<T>> implements Comparable {
    T x, y;

    public Coordinate(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    @Override
    public int compareTo(Object o) {
        Coordinate<T> other = (Coordinate) o;

        return this.y.compareTo(other.y);
    }
}



