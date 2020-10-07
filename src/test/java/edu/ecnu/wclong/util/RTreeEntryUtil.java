package edu.ecnu.wclong.util;

import edu.ecnu.wclong.sdo.RTreeEntry;

public class RTreeEntryUtil {

    public static <T> RTreeEntry<T> copyEntry(RTreeEntry<T> source) {
        return new RTreeEntry<>(source.getRectangle(), source.getValue());
    }
}
