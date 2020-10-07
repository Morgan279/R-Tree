package edu.ecnu.wclong.util;

import edu.ecnu.wclong.RTree;
import edu.ecnu.wclong.sdo.Bound;
import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.RTreeNode;
import edu.ecnu.wclong.sdo.Rectangle;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RTreeUtil {
    public static class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static int cnt = 0;


    public static <T> void printRTree(RTree<T> rTree) {
        print(rTree.getRootNode(), 1);
    }

    private static <T> void print(RTreeNode<T> node, int level) {
        if (null == node) return;


        for (int i = 1; i < level; ++i) {
            System.out.print("\t");
        }
        System.out.print(node + " ");
        System.out.println();
        for (RTreeEntry<T> rTreeEntry : node.getEntries()) {
            if (node.isLeafNode()) {
                for (int i = 1; i < level; ++i) {
                    System.out.print("\t");
                }
                ++cnt;
                System.out.print("======>" + rTreeEntry.getValue());
                System.out.println();
            }
            print(rTreeEntry.getChildren(), level + 1);
        }
    }

    public static <T> void printRTreeRectangle(RTree<T> rTree) {
        RTreeNode<T> rootNode = rTree.getRootNode();
        StringBuilder stringBuilder = new StringBuilder();
        if (rootNode.getRectangle().getDimension() != 2) {
            throw new IllegalStateException("print only available for 2 dimensions");
        }

        Set<Point> counter = new HashSet<>();
        countBound(rootNode, counter);
        Bound bound1 = rootNode.getRectangle().getBoundByIndex(0);
        Bound bound2 = rootNode.getRectangle().getBoundByIndex(1);
        int maxX = (int) bound1.getHigherBound();
        int maxY = (int) bound2.getHigherBound();
        int minX = (int) bound1.getLowerBound();
        int minY = (int) bound2.getLowerBound();
        for (int i = minX; i <= maxX; ++i) {
            for (int j = minY; j <= maxY; ++j) {
                if (counter.contains(new RTreeUtil.Point(i, j))) {
                    stringBuilder.append("`");
                } else {
                    stringBuilder.append(" ");
                }
            }
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    private static <T> void countBound(RTreeNode<T> node, Set<Point> counter) {
        if (null == node) return;

        count(node.getRectangle(), counter);
        for (RTreeEntry<T> rTreeEntry : node.getEntries()) {
            count(rTreeEntry.getRectangle(), counter);
            countBound(rTreeEntry.getChildren(), counter);
        }
    }

    private static void count(Rectangle rectangle, Set<Point> counter) {
        Bound bound1 = rectangle.getBoundByIndex(0);
        Bound bound2 = rectangle.getBoundByIndex(1);
        int maxX = (int) bound1.getHigherBound();
        int maxY = (int) bound2.getHigherBound();
        int minX = (int) bound1.getLowerBound();
        int minY = (int) bound2.getLowerBound();
        for (int i = minX; i <= maxX; ++i) {
            for (int j = minY; j <= maxY; ++j) {
                if (i == minX || i == maxX || j == minY || j == maxY) {
                    counter.add(new Point(i, j));
                }
            }
        }
    }

}
