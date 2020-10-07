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
        this.rectangle = RectangleUtil.getBoundedRectangleByChildrenEntries(entries);
    }

    public boolean removeEntry(RTreeEntry<T> targetEntry) {
        boolean isDeleted = this.entries.remove(targetEntry);
        if (isDeleted) {
            //update rectangle when target Entry is deleted
            this.rectangle = RectangleUtil.getBoundedRectangleByChildrenNode(targetEntry.getChildren());
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
