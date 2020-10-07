package edu.ecnu.wclong;

import edu.ecnu.wclong.sdo.Bound;
import edu.ecnu.wclong.sdo.BoundVector;
import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.Rectangle;
import edu.ecnu.wclong.separator.impl.RTreeNodeLinearSeparator;
import edu.ecnu.wclong.util.RTreeEntryUtil;
import edu.ecnu.wclong.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RTreeTest {

    private RTree<Integer> rTree;

    @BeforeEach
    public void initRTree() {
        rTree = new RTree<>(3);
    }

    @Test
    public void search() {

    }

    @Test
    public void insert() {
        RTree<Integer> linearRTree = new RTree<>(3, new RTreeNodeLinearSeparator());

        for (int i = 0; i < 50; ++i) {
            RTreeEntry<Integer> entry = generateRandomEntry();
            rTree.insert(entry);
            linearRTree.insert(RTreeEntryUtil.copyEntry(entry));
        }
        //RTreeUtil.printRTree(rTree);
        //RTreeUtil.printRTreeRectangle(rTree);
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
        int value = RandomUtil.getIntegerRandomNumber(2, 1000);
        int dimension = 2;
        BoundVector boundVector = new BoundVector(dimension);
        for (int i = 0; i < dimension; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        Rectangle rectangle = new Rectangle(boundVector);
        return new RTreeEntry<>(rectangle, value);
    }
}
