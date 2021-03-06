package edu.ecnu.wclong.util;

import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.RTreeNode;

import java.util.ArrayList;
import java.util.List;

public class RTreeNodeSplitUtil {

    public static <T> void setSplitEntriesLocatedNode(List<RTreeEntry<T>> entries, RTreeNode<T> locatedNode) {
        entries.forEach(entry -> entry.setLocatedNode(locatedNode));
    }

    public static <T> List<RTreeEntry<T>> pickGroup(RTreeEntry<T> nextEntry, List<RTreeEntry<T>> splitList1, List<RTreeEntry<T>> splitList2, int M) {
        int upperBound = M + 1 - M / 2;
        if (splitList1.size() == upperBound) return splitList2;
        if (splitList2.size() == upperBound) return splitList1;

        double enlargeArea1 = RTreeNodeSplitUtil.calculateEnlargeArea(nextEntry, splitList1);
        double enlargeArea2 = RTreeNodeSplitUtil.calculateEnlargeArea(nextEntry, splitList2);

        if (enlargeArea1 == enlargeArea2) {
            double splitArea1 = RectangleUtil.getBoundedRectangleByEntries(splitList1).getArea();
            double splitArea2 = RectangleUtil.getBoundedRectangleByEntries(splitList2).getArea();

            if (splitArea1 == splitArea2) {
                return splitList1.size() < splitList2.size() ? splitList1 : splitList2;
            }

            return splitArea1 < splitArea2 ? splitList1 : splitList2;
        }

        return enlargeArea1 < enlargeArea2 ? splitList1 : splitList2;
    }

    public static <T> double calculateAreaIncreaseDiff(RTreeEntry<T> entry, List<RTreeEntry<T>> splitList1, List<RTreeEntry<T>> splitList2) {
        double d1 = calculateEnlargeArea(entry, splitList1);
        double d2 = calculateEnlargeArea(entry, splitList2);
        return Math.abs(d1 - d2);
    }

    public static <T> double calculateEnlargeArea(RTreeEntry<T> entry, List<RTreeEntry<T>> entries) {
        if (null == entry || null == entries || entries.isEmpty()) {
            throw new IllegalArgumentException("entry, entries cannot be empty or null");
        }

        List<RTreeEntry<T>> aux = new ArrayList<>(entries);
        aux.add(entry);
        return RectangleUtil.getBoundedRectangleByEntries(aux).getArea()
                - RectangleUtil.getBoundedRectangleByEntries(entries).getArea();
    }

}
