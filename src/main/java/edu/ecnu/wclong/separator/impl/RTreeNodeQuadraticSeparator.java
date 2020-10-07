package edu.ecnu.wclong.separator.impl;

import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.separator.RTreeNodeSeparator;
import edu.ecnu.wclong.sdo.Rectangle;
import edu.ecnu.wclong.sdo.dto.PickSeedsResult;
import edu.ecnu.wclong.util.RectangleUtil;

import java.util.List;

public class RTreeNodeQuadraticSeparator implements RTreeNodeSeparator {

    @Override
    public <T> PickSeedsResult<T> pickSeeds(List<RTreeEntry<T>> entries) {
        RTreeEntry<T> seed1 = null;
        RTreeEntry<T> seed2 = null;
        double diff = -Double.MAX_VALUE;

        for (int i = 0; i < entries.size(); ++i) {
            for (int j = 0; j < entries.size(); ++j) {
                if (i == j) continue;

                Rectangle rectangle1 = entries.get(i).getRectangle();
                Rectangle rectangle2 = entries.get(j).getRectangle();

                double currentDiff = RectangleUtil.getBoundedRectangleByTwoRectangles(
                        rectangle1,
                        rectangle2
                ).getArea() - rectangle1.getArea() - rectangle2.getArea();

                if (currentDiff > diff) {
                    seed1 = entries.get(i);
                    seed2 = entries.get(j);
                }
            }
        }

        return new PickSeedsResult<>(seed1, seed2);
    }


}
