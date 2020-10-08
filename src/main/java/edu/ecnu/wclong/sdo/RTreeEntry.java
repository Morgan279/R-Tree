package edu.ecnu.wclong.sdo;


import java.util.Objects;
import java.util.UUID;

public class RTreeEntry<T> {

    private String id;

    private Rectangle rectangle;

    private T value;

    private RTreeNode<T> children;

    private RTreeNode<T> locatedNode;

    public RTreeEntry(Rectangle rectangle, T value) {
        this.rectangle = rectangle;
        this.value = value;
        this.id = UUID.randomUUID().toString();
    }

    public RTreeEntry(Rectangle rectangle, RTreeNode<T> locatedNode, T value) {
        this.rectangle = rectangle;
        this.locatedNode = locatedNode;
        this.value = value;
        this.id = UUID.randomUUID().toString();
    }

    public RTreeEntry(RTreeNode<T> locatedNode, RTreeNode<T> children) {
        if (null == children) {
            throw new IllegalArgumentException("cannot init entry when children is null");
        }

        this.rectangle = children.getRectangle();
        this.locatedNode = locatedNode;
        this.children = children;
        this.id = UUID.randomUUID().toString();
    }

    public RTreeEntry(String id) {
        this.id = id;
    }

    public void setChildren(RTreeNode<T> children) {
        this.children = children;
        this.onChildrenChange();
    }

    public void onChildrenChange() {
        this.rectangle = children.getRectangle();
        //ascend
        this.locatedNode.updateParentRectangleOnEntryChange();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RTreeEntry)) return false;
        RTreeEntry<?> entry = (RTreeEntry<?>) o;
        return getId().equals(entry.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public String getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public RTreeNode<T> getChildren() {
        return children;
    }

    public RTreeNode<T> getLocatedNode() {
        return locatedNode;
    }

    public void setLocatedNode(RTreeNode<T> locatedNode) {
        this.locatedNode = locatedNode;
    }
}
