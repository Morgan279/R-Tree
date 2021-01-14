package edu.ecnu.wclong;

import edu.ecnu.wclong.sdo.Bound;
import edu.ecnu.wclong.sdo.BoundVector;
import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.Rectangle;
import edu.ecnu.wclong.util.RTreeUtil;
import edu.ecnu.wclong.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;


class RTreeTest {

    private RTree<Integer> rTree;

    private static final int DIMENSION = 2;

    @BeforeEach
    public void initRTree() {
        rTree = new RTree<>(5);
        insert();
    }

    @Test
    public void test(){
        insert();
        RTreeUtil.printRTreeRectangle(rTree);
    }

    @Test
    public void search() {
        BoundVector boundVector = new BoundVector(DIMENSION);
        int value = RandomUtil.getIntegerRandomNumber(DIMENSION, 100);
        for (int i = 0; i < DIMENSION; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        Set<RTreeEntry<Integer>> results =  rTree.search(new Rectangle(boundVector));
    }

    public void insert() {
        for (int i = 0; i < 10; ++i) {
            RTreeEntry<Integer> entry = generateRandomEntry();
            rTree.insert(entry);
        }
    }

    @Test
    public void delete() {
        int value = RandomUtil.getIntegerRandomNumber(DIMENSION, 100);
        BoundVector boundVector = new BoundVector(DIMENSION);
        for (int i = 0; i < DIMENSION; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        RTreeEntry<Integer> rTreeEntry = new RTreeEntry<>(new Rectangle(boundVector),value);
        System.out.println(rTree.delete(rTreeEntry));
    }

    @Test
    public void update() {
        BoundVector boundVector = new BoundVector(DIMENSION);
        int value = RandomUtil.getIntegerRandomNumber(DIMENSION, 100);
        for (int i = 0; i < DIMENSION; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        Set<RTreeEntry<Integer>> results =  rTree.search(new Rectangle(boundVector));
        RTreeEntry<Integer> rTreeEntry = results.stream().findAny().orElseThrow(() -> new RuntimeException("find nothing"));
        rTree.update(rTreeEntry.getId(),RandomUtil.getIntegerRandomNumber());
    }

    private RTreeEntry<Integer> generateRandomEntry() {
        int value = RandomUtil.getIntegerRandomNumber(DIMENSION, 50);
        BoundVector boundVector = new BoundVector(DIMENSION);
        for (int i = 0; i < DIMENSION; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        Rectangle rectangle = new Rectangle(boundVector);
        return new RTreeEntry<>(rectangle, value);
    }

}
