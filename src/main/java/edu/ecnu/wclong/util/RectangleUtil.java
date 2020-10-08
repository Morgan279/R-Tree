package edu.ecnu.wclong.util;

import edu.ecnu.wclong.sdo.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RectangleUtil {

    public static Rectangle getBoundedRectangleByRectangleList(List<Rectangle> rectangleList) {
        if (null == rectangleList || rectangleList.isEmpty()) {
            throw new IllegalArgumentException("children rectangles can not be empty");
        }

        int dimension = rectangleList.get(0).getDimension();
        BoundVector boundVector = new BoundVector(dimension);

        for (int i = 0; i < dimension; ++i) {
            double lowest = Double.MAX_VALUE;
            double highest = -Double.MAX_VALUE;

            for (Rectangle rectangle : rectangleList) {
                Bound currentBound = rectangle.getBoundByIndex(i);
                lowest = Double.min(lowest, currentBound.getLowerBound());
                highest = Double.max(highest, currentBound.getHigherBound());
            }
            boundVector.setDimensionBound(i, new Bound(lowest, highest));
        }

        return new Rectangle(boundVector);
    }

    public static <T> Rectangle getBoundedRectangleByEntries(List<RTreeEntry<T>> entries) {
        List<Rectangle> rectangleList = new ArrayList<>();

        for (RTreeEntry<T> entry : entries) {
            if (null != entry.getRectangle()) {
                rectangleList.add(entry.getRectangle());
            }
        }

        return getBoundedRectangleByRectangleList(rectangleList);
    }

    public static <T> Rectangle getBoundedRectangleByChildrenNode(RTreeNode<T> rTreeNode) {
        if (null == rTreeNode) return null;

        return getBoundedRectangleByEntries(rTreeNode.getEntries());
    }

    public static Rectangle getBoundedRectangleByTwoRectangles(Rectangle rectangle1, Rectangle rectangle2) {
        List<Rectangle> rectangles = Arrays.asList(rectangle1, rectangle2);
        return getBoundedRectangleByRectangleList(rectangles);
    }
}
