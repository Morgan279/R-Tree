package edu.ecnu.wclong.sdo.alterable;

import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.RTreeNode;
import edu.ecnu.wclong.sdo.Rectangle;
import edu.ecnu.wclong.util.RectangleUtil;

import java.util.List;

public class RTreeNonLeafNode<T> extends RTreeNode<T> {

    public RTreeNonLeafNode(List<RTreeEntry<T>> entries, RTreeEntry<T> parent) {
        this.entries = entries;
        this.parent = parent;
        this.rectangle = generateRectangle(entries);
    }

    public RTreeNonLeafNode(List<RTreeEntry<T>> entries) {
        this.entries = entries;
        this.parent = null;
        this.rectangle = generateRectangle(entries);
    }

    private Rectangle generateRectangle(List<RTreeEntry<T>> entries) {
        if (null == entries || entries.isEmpty()) return null;

        return RectangleUtil.getBoundedRectangleByChildrenEntries(entries);
    }


    @Override
    public boolean isLeafNode() {
        return false;
    }
}
