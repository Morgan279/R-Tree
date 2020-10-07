package edu.ecnu.wclong.sdo.alterable;

import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.RTreeNode;
import edu.ecnu.wclong.sdo.Rectangle;

import java.util.List;

public class RTreeLeafNode<T> extends RTreeNode<T> {

    public RTreeLeafNode(List<RTreeEntry<T>> entries, Rectangle rectangle, RTreeEntry<T> parent) {
        this.entries = entries;
        this.rectangle = rectangle;
        this.parent = parent;
    }

    public RTreeLeafNode(List<RTreeEntry<T>> entries, Rectangle rectangle) {
        this.entries = entries;
        this.rectangle = rectangle;
    }

    @Override
    public boolean isLeafNode() {
        return true;
    }

}
