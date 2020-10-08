package edu.ecnu.wclong.separator.impl;

import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.Rectangle;
import edu.ecnu.wclong.sdo.dto.PickSeedsResult;
import edu.ecnu.wclong.separator.RTreeNodeSeparator;
import edu.ecnu.wclong.util.RTreeNodeSplitUtil;
import edu.ecnu.wclong.util.RectangleUtil;

import java.util.List;

public class RTreeNodeQuadraticSeparator implements RTreeNodeSeparator {

    @Override
    public <T> PickSeedsResult<T> pickSeeds(List<RTreeEntry<T>> entries) {
        RTreeEntry<T> seed1 = null;
        RTreeEntry<T> seed2 = null;
        double largestDiff = -Double.MAX_VALUE;

        for (int i = 0; i < entries.size(); ++i) {
            for (int j = 0; j < entries.size(); ++j) {
                if (i == j) continue;

                Rectangle rectangle1 = entries.get(i).getRectangle();
                Rectangle rectangle2 = entries.get(j).getRectangle();

                double currentDiff = RectangleUtil.getBoundedRectangleByTwoRectangles(
                        rectangle1,
                        rectangle2
                ).getArea() - rectangle1.getArea() - rectangle2.getArea();

                if (currentDiff > largestDiff) {
                    seed1 = entries.get(i);
                    seed2 = entries.get(j);
                    largestDiff = currentDiff;
                }
            }
        }

        return new PickSeedsResult<>(seed1, seed2);
    }

    @Override
    public <T> RTreeEntry<T> pickNext(List<RTreeEntry<T>> entries, List<RTreeEntry<T>> splitList1, List<RTreeEntry<T>> splitList2) {
        RTreeEntry<T> nextEntry = entries.get(0);
        double maxDiff = RTreeNodeSplitUtil.calculateAreaIncreaseDiff(nextEntry, splitList1, splitList2);
        for (int i = 1; i < entries.size(); ++i) {
            double diff = RTreeNodeSplitUtil.calculateAreaIncreaseDiff(entries.get(i), splitList1, splitList2);
            if (diff > maxDiff) {
                nextEntry = entries.get(i);
            }
        }
        return nextEntry;
    }


}
