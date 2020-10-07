package edu.ecnu.wclong.sdo;

public class BoundVector {

    private Bound[] vector;

    public BoundVector(int dimension) {
        vector = new Bound[dimension];
    }

    public void setDimensionBound(int dimension, Bound bound) {
        checkDimension(dimension);
        vector[dimension] = bound;
    }

    public Bound getBoundByDimension(int dimension) {
        checkDimension(dimension);
        return vector[dimension];
    }

    public int getVectorDimension() {
        return this.vector.length;
    }

    public Bound[] getVector() {
        return this.vector;
    }

    private void checkDimension(int dimension) {
        if (dimension < 0 || dimension >= this.vector.length) {
            throw new IndexOutOfBoundsException("dimension out of bounds (dimension start from 0)");
        }
    }
}
