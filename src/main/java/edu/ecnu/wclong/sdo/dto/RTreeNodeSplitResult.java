package edu.ecnu.wclong.sdo.dto;

import edu.ecnu.wclong.sdo.RTreeEntry;

import java.util.List;

public class RTreeNodeSplitResult<T> {

    private List<RTreeEntry<T>> splitList1;

    private List<RTreeEntry<T>> splitList2;

    public RTreeNodeSplitResult(List<RTreeEntry<T>> splitList1, List<RTreeEntry<T>> splitList2) {
        this.splitList1 = splitList1;
        this.splitList2 = splitList2;
    }

    public List<RTreeEntry<T>> getSplitList1() {
        return splitList1;
    }

    public List<RTreeEntry<T>> getSplitList2() {
        return splitList2;
    }
}
