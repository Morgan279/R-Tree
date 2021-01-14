package edu.ecnu.wclong.separator;

import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.dto.PickSeedsResult;
import edu.ecnu.wclong.sdo.dto.RTreeNodeSplitResult;
import edu.ecnu.wclong.util.RTreeNodeSplitUtil;

import java.util.ArrayList;
import java.util.List;

public interface RTreeNodeSeparator {

    default <T> RTreeNodeSplitResult<T> split(List<RTreeEntry<T>> entries) {
        int M = entries.size() - 1;
        List<RTreeEntry<T>> splitList1 = new ArrayList<>();
        List<RTreeEntry<T>> splitList2 = new ArrayList<>();
        PickSeedsResult<T> pickSeedsResult = this.pickSeeds(entries);
        splitList1.add(pickSeedsResult.getSeed1());
        splitList2.add(pickSeedsResult.getSeed2());
        entries.remove(pickSeedsResult.getSeed1());
        entries.remove(pickSeedsResult.getSeed2());

        while (!entries.isEmpty()) {
            RTreeEntry<T> nextEntry = this.pickNext(entries, splitList1, splitList2);
            RTreeNodeSplitUtil.pickGroup(nextEntry, splitList1, splitList2, M).add(nextEntry);
            entries.remove(nextEntry);
        }

        return new RTreeNodeSplitResult<>(splitList1, splitList2);
    }

    <T> PickSeedsResult<T> pickSeeds(List<RTreeEntry<T>> entries);

    <T> RTreeEntry<T> pickNext(List<RTreeEntry<T>> entries, List<RTreeEntry<T>> splitList1, List<RTreeEntry<T>> splitList2);

}
