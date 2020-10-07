package edu.ecnu.wclong.sdo;

import java.util.Objects;

public class Bound {
    private double lowerBound;

    private double higherBound;

    public Bound(double lowerBound, double higherBound) {
        if (lowerBound >= higherBound) {
            throw new IllegalArgumentException("higher bound must greater than lower bound");
        }

        this.lowerBound = lowerBound;
        this.higherBound = higherBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getHigherBound() {
        return higherBound;
    }

    public boolean isIntersect(Bound bound) {
        return this.getLowerBound() > bound.getHigherBound()
                || this.getHigherBound() < bound.getLowerBound();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bound)) return false;
        Bound bound = (Bound) o;
        return Double.compare(bound.getLowerBound(), getLowerBound()) == 0 &&
                Double.compare(bound.getHigherBound(), getHigherBound()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLowerBound(), getHigherBound());
    }
}
