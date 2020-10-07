package edu.ecnu.wclong.util;

import edu.ecnu.wclong.sdo.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RectangleUtil {

    public static Rectangle getBoundedRectangleByChildrenRectangles(List<Rectangle> childrenRectangles) {
        if (null == childrenRectangles || childrenRectangles.isEmpty()) {
            throw new IllegalArgumentException("children rectangles can not be empty");
        }

        int dimension = childrenRectangles.get(0).getDimension();
        BoundVector boundVector = new BoundVector(dimension);

        for (int i = 0; i < dimension; ++i) {
            double lowest = Double.MAX_VALUE;
            double highest = -Double.MAX_VALUE;

            for (Rectangle rectangle : childrenRectangles) {
                Bound currentBound = rectangle.getBoundByIndex(i);
                lowest = Double.min(lowest, currentBound.getLowerBound());
                highest = Double.max(highest, currentBound.getHigherBound());
            }
            boundVector.setDimensionBound(i, new Bound(lowest, highest));
        }

        return new Rectangle(boundVector);
    }

    public static <T> Rectangle getBoundedRectangleByChildrenEntries(List<RTreeEntry<T>> entries) {
        List<Rectangle> childrenRectangles = new ArrayList<>();

        for (RTreeEntry<T> entry : entries) {
            if (null != entry.getRectangle()) {
                childrenRectangles.add(entry.getRectangle());
            }
        }

        return getBoundedRectangleByChildrenRectangles(childrenRectangles);
    }

    public static <T> Rectangle getBoundedRectangleByChildrenNode(RTreeNode<T> rTreeNode) {
        if (null == rTreeNode) return null;

        return getBoundedRectangleByChildrenEntries(rTreeNode.getEntries());
    }

    public static Rectangle getBoundedRectangleByTwoRectangles(Rectangle rectangle1, Rectangle rectangle2) {
        List<Rectangle> rectangles = Arrays.asList(rectangle1, rectangle2);
        return getBoundedRectangleByChildrenRectangles(rectangles);
    }
}
