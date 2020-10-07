package edu.ecnu.wclong.sdo.dto;

import edu.ecnu.wclong.sdo.RTreeEntry;

public class PickSeedsResult<T> {

    private RTreeEntry<T> seed1;

    private RTreeEntry<T> seed2;

    public PickSeedsResult(RTreeEntry<T> seed1, RTreeEntry<T> seed2) {
        this.seed1 = seed1;
        this.seed2 = seed2;
    }

    public RTreeEntry<T> getSeed1() {
        return seed1;
    }

    public RTreeEntry<T> getSeed2() {
        return seed2;
    }
}
