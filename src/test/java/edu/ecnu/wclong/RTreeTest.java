package edu.ecnu.wclong;

import edu.ecnu.wclong.sdo.Bound;
import edu.ecnu.wclong.sdo.BoundVector;
import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.Rectangle;
import edu.ecnu.wclong.separator.impl.RTreeNodeLinearSeparator;
import edu.ecnu.wclong.util.RTreeEntryUtil;
import edu.ecnu.wclong.util.RTreeUtil;
import edu.ecnu.wclong.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RTreeTest {

    private RTree<Integer> rTree;

    @BeforeEach
    public void initRTree() {
        rTree = new RTree<>(5);
    }

    @Test
    public void search() {

    }

    @Test
    public void insert() {
        RTree<Integer> linearRTree = new RTree<>(5, new RTreeNodeLinearSeparator());

        for (int i = 0; i < 10; ++i) {
            RTreeEntry<Integer> entry = generateRandomEntry();
            rTree.insert(entry);
            linearRTree.insert(RTreeEntryUtil.copyEntry(entry));
        }
        RTreeUtil.printRTree(rTree);
        RTreeUtil.printRTree(linearRTree);
        RTreeUtil.printRTreeRectangle(rTree);
        RTreeUtil.printRTreeRectangle(linearRTree);
        System.out.println(rTree.getRootNode().getRectangle().getArea());
        System.out.println(linearRTree.getRootNode().getRectangle().getArea());
    }

    @Test
    public void delete() {

    }

    @Test
    public void update() {

    }

    private RTreeEntry<Integer> generateRandomEntry() {
        int dimension = 2;
        int value = RandomUtil.getIntegerRandomNumber(dimension, 100);
        BoundVector boundVector = new BoundVector(dimension);
        for (int i = 0; i < dimension; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        Rectangle rectangle = new Rectangle(boundVector);
        return new RTreeEntry<>(rectangle, value);
    }

    private RTreeEntry<Integer> generateRandomEntry(int index) {
        int dimension = 2;
        int values[] = {27, 43, 20, 48, 50, 54, 64, 69, 93, 91};
        int value = values[index];
        BoundVector boundVector = new BoundVector(dimension);
        for (int i = 0; i < dimension; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        Rectangle rectangle = new Rectangle(boundVector);
        return new RTreeEntry<>(rectangle, value);
    }
}
