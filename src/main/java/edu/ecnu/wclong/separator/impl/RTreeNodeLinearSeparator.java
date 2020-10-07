package edu.ecnu.wclong.separator.impl;

import edu.ecnu.wclong.sdo.Bound;
import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.dto.PickSeedsResult;
import edu.ecnu.wclong.separator.RTreeNodeSeparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RTreeNodeLinearSeparator implements RTreeNodeSeparator {

    @Override
    public <T> PickSeedsResult<T> pickSeeds(List<RTreeEntry<T>> entries) {
        int dimension = entries.get(0).getRectangle().getDimension();

        double[] highestLowSide = new double[dimension];
        double[] lowestHighSide = new double[dimension];
        double[] dimensionWidth = new double[dimension];
        Map<Integer, RTreeEntry<T>> highestLowSideEntryMap = new HashMap<>();
        Map<Integer, RTreeEntry<T>> lowestHighSideEntryMap = new HashMap<>();

        for (int i = 0; i < dimension; ++i) {
            highestLowSide[i] = -Double.MAX_VALUE;
            lowestHighSide[i] = Double.MAX_VALUE;
            dimensionWidth[i] = 0;

            for (RTreeEntry<T> entry : entries) {
                Bound bound = entry.getRectangle().getBoundByIndex(i);
                dimensionWidth[i] += bound.getHigherBound() - bound.getLowerBound();

                if (bound.getLowerBound() > highestLowSide[i]) {
                    highestLowSide[i] = bound.getLowerBound();
                    highestLowSideEntryMap.put(i, entry);
                }

                if (bound.getHigherBound() < lowestHighSide[i]) {
                    lowestHighSide[i] = bound.getHigherBound();
                    lowestHighSideEntryMap.put(i, entry);
                }
            }
        }

        int extremeSeparationDimension = 0;
        double extremeSeparation = Math.abs(highestLowSide[0] - lowestHighSide[0]) / dimensionWidth[0];
        for (int i = 1; i < dimension; ++i) {
            double separation = Math.abs(highestLowSide[i] - lowestHighSide[i]) / dimensionWidth[i];
            if (separation > extremeSeparation) {
                extremeSeparation = separation;
                extremeSeparationDimension = i;
            }
        }

        return new PickSeedsResult<>(
                highestLowSideEntryMap.get(extremeSeparationDimension),
                lowestHighSideEntryMap.get(extremeSeparationDimension)
        );
    }

}
