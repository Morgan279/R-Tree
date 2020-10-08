package edu.ecnu.wclong.sdo;

import edu.ecnu.wclong.util.RectangleUtil;

import java.util.List;

public abstract class RTreeNode<T> {
    protected List<RTreeEntry<T>> entries;

    protected Rectangle rectangle;

    protected RTreeEntry<T> parent;

    public void addEntry(RTreeEntry<T> newEntry) {
        this.entries.add(newEntry);
        //update rectangle when new Entry is added
        this.updateParentRectangleOnEntryChange();
    }

    public void updateParentRectangleOnEntryChange() {
        //update current node's rectangle when new Entry is changed
        this.rectangle = RectangleUtil.getBoundedRectangleByEntries(entries);
        //update parent's rectangle when new Entry is changed
        if (null != this.parent) {
            this.parent.onChildrenChange();
        }
    }

    public boolean removeEntry(RTreeEntry<T> targetEntry) {
        boolean isDeleted = this.entries.remove(targetEntry);
        if (isDeleted) {
            this.updateParentRectangleOnEntryChange();
        }
        return isDeleted;
    }

    public List<RTreeEntry<T>> getEntries() {
        return this.entries;
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    public RTreeEntry<T> getParent() {
        return this.parent;
    }

    public void setParent(RTreeEntry<T> parent) {
        this.parent = parent;
    }

    public abstract boolean isLeafNode();
}
