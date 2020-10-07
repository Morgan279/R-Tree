package edu.ecnu.wclong.sdo;


import java.util.Arrays;
import java.util.List;

public class Rectangle {

    private int dimension;

    private Double area;

    private BoundVector boundVector;

    public Rectangle(BoundVector boundVector) {
        if (null == boundVector || boundVector.getVectorDimension() < 2) {
            throw new IllegalArgumentException("rectangle dimension must greater than 1");
        }

        this.boundVector = boundVector;
        this.dimension = boundVector.getVectorDimension();
        this.area = null;
    }

    public int getDimension() {
        return dimension;
    }

    public Bound getBoundByIndex(int dimension) {
        return boundVector.getBoundByDimension(dimension);
    }

    public boolean isOverlap(Rectangle rectangle) {
        checkDimension(rectangle);

        for (int i = 0; i < dimension; ++i) {
            if (this.getBoundByIndex(i).isIntersect(rectangle.getBoundByIndex(i)))
                return true;
        }
        return false;
    }

    public double getArea() {
        //lazy calculate
        if (null == this.area) {
            this.area = this.calculateArea(Arrays.asList(boundVector.getVector()));
        }
        return this.area;
    }

    private double calculateArea(List<Bound> bounds) {
        return bounds.stream()
                .map(bound -> bound.getHigherBound() - bound.getLowerBound())
                .reduce((product, diff) -> product * diff)
                .orElseThrow(() -> new RuntimeException("calculate area failed"));
    }

    private void checkDimension(Rectangle rectangle) {
        if (this.dimension != rectangle.getDimension()) {
            throw new IllegalArgumentException("The dimensions of the two rectangles do not match.");
        }
    }
}
