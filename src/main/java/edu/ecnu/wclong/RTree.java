package edu.ecnu.wclong;

import edu.ecnu.wclong.algorithm.RTreeAlgorithm;
import edu.ecnu.wclong.algorithm.impl.RTreeAlgorithmImpl;
import edu.ecnu.wclong.sdo.RTreeEntry;
import edu.ecnu.wclong.sdo.RTreeNode;
import edu.ecnu.wclong.sdo.Rectangle;
import edu.ecnu.wclong.sdo.alterable.RTreeLeafNode;
import edu.ecnu.wclong.sdo.alterable.RTreeNonLeafNode;
import edu.ecnu.wclong.sdo.dto.RTreeNodeSplitResult;
import edu.ecnu.wclong.separator.RTreeNodeSeparator;
import edu.ecnu.wclong.separator.impl.RTreeNodeQuadraticSeparator;
import edu.ecnu.wclong.util.RTreeNodeSplitUtil;
import edu.ecnu.wclong.util.RectangleUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RTree<T> {

    private RTreeNode<T> rootNode;

    private RTreeAlgorithm rTreeAlgorithm;

    private RTreeNodeSeparator separator;

    //M
    private int loadFactor;

    public RTree() {
        this.loadFactor = 50;
        this.rTreeAlgorithm = new RTreeAlgorithmImpl();
        this.rootNode = new RTreeLeafNode<>(new ArrayList<>(), null);
    }

    public RTree(int loadFactor) {
        checkLoadFactor(loadFactor);

        this.loadFactor = loadFactor;
        this.separator = new RTreeNodeQuadraticSeparator();
        this.rTreeAlgorithm = new RTreeAlgorithmImpl();
        this.rootNode = new RTreeLeafNode<>(new ArrayList<>(), null);
    }

    public RTree(int loadFactor, RTreeNodeSeparator separator) {
        checkLoadFactor(loadFactor);

        this.loadFactor = loadFactor;
        this.separator = separator;
        this.rTreeAlgorithm = new RTreeAlgorithmImpl();
        this.rootNode = new RTreeLeafNode<>(new ArrayList<>(), null);
    }

    public RTree(int loadFactor, RTreeNodeSeparator separator, RTreeAlgorithm rTreeAlgorithm) {
        checkLoadFactor(loadFactor);

        this.loadFactor = loadFactor;
        this.separator = separator;
        this.rTreeAlgorithm = rTreeAlgorithm;
        this.rootNode = new RTreeLeafNode<>(new ArrayList<>(), null);
    }

    public Set<RTreeEntry<T>> search(Rectangle searchRectangle) {
        Set<RTreeEntry<T>> resultSet = new HashSet<>();
        rTreeAlgorithm.search(searchRectangle, rootNode, resultSet);
        return resultSet;
    }

    public void insert(RTreeEntry<T> newEntry) {
        RTreeNode<T> targetNode = rTreeAlgorithm.chooseLeaf(rootNode, newEntry);
        newEntry.setLocatedNode(targetNode);
        targetNode.addEntry(newEntry);
        splitNodeIfNeed(targetNode);
    }


    /**
     * @param targetEntry targetEntry that need be deleted
     * @return true if delete successfully; false, if targetEntry is not found
     */
    public boolean delete(RTreeEntry<T> targetEntry) {
        RTreeNode<T> targetNode = rTreeAlgorithm.findLeaf(rootNode, targetEntry);
        if (null == targetNode) return false;

        boolean isDeleted = targetNode.removeEntry(targetEntry);
        if (isDeleted) {
            condenseTree(targetNode, new HashSet<>());
            shortenTreeIfNeed();
        }

        return isDeleted;
    }

    public boolean delete(String entryId) {
        RTreeEntry<T> targetEntry = new RTreeEntry<>(entryId);
        return delete(targetEntry);
    }

    public void update(RTreeEntry<T> oldEntry, RTreeEntry<T> newEntry) {
        if (this.delete(oldEntry)) {
            this.insert(newEntry);
        }
    }

    public RTreeNode<T> getRootNode() {
        return rootNode;
    }

    private boolean isNeedSplit(int nodeSize) {
        return nodeSize == loadFactor + 1;
    }

    private void splitNodeIfNeed(RTreeNode<T> targetNode) {
        if (isNeedSplit(targetNode.getEntries().size())) {
            splitNode(targetNode);
        }
    }

    private void splitNode(RTreeNode<T> targetNode) {
        RTreeNodeSplitResult<T> rTreeNodeSplitResult = rTreeAlgorithm.splitNode(
                separator,
                targetNode.getEntries()
        );

        if (targetNode.isLeafNode()) {
            splitLeafNode(targetNode, rTreeNodeSplitResult);
        } else {
            splitNonLeafNode(targetNode, rTreeNodeSplitResult);
        }
    }

    private void splitLeafNode(RTreeNode<T> targetNode, RTreeNodeSplitResult<T> rTreeNodeSplitResult) {
        createNewNodeIfSplitNodeIsRootNode(targetNode);

        RTreeEntry<T> targetNodeParent = targetNode.getParent();
        targetNode = new RTreeLeafNode<>(
                rTreeNodeSplitResult.getSplitList1(),
                RectangleUtil.getBoundedRectangleByEntries(
                        rTreeNodeSplitResult.getSplitList1()
                ),
                targetNodeParent
        );
        targetNodeParent.setChildren(targetNode);
        RTreeNodeSplitUtil.setSplitEntriesLocatedNode(targetNode.getEntries(), targetNode);

        RTreeNode<T> newNode = new RTreeLeafNode<>(
                rTreeNodeSplitResult.getSplitList2(),
                RectangleUtil.getBoundedRectangleByEntries(
                        rTreeNodeSplitResult.getSplitList2()
                )
        );
        RTreeNodeSplitUtil.setSplitEntriesLocatedNode(newNode.getEntries(), newNode);

        updateNewNodeParentNodeAndAscend(newNode, targetNodeParent);
    }

    private void splitNonLeafNode(RTreeNode<T> targetNode, RTreeNodeSplitResult<T> rTreeNodeSplitResult) {
        createNewNodeIfSplitNodeIsRootNode(targetNode);

        RTreeEntry<T> targetNodeParent = targetNode.getParent();
        targetNode = new RTreeNonLeafNode<>(
                rTreeNodeSplitResult.getSplitList1(),
                targetNodeParent
        );
        targetNodeParent.setChildren(targetNode);
        RTreeNodeSplitUtil.setSplitEntriesLocatedNode(targetNode.getEntries(), targetNode);

        RTreeNode<T> newNode = new RTreeNonLeafNode<>(
                rTreeNodeSplitResult.getSplitList2()
        );
        RTreeNodeSplitUtil.setSplitEntriesLocatedNode(newNode.getEntries(), newNode);

        updateNewNodeParentNodeAndAscend(newNode, targetNodeParent);
    }

    private void createNewNodeIfSplitNodeIsRootNode(RTreeNode<T> targetNode) {
        if (targetNode == rootNode) {
            RTreeNode<T> newRootNode = new RTreeNonLeafNode<>(new ArrayList<>());
            RTreeEntry<T> newRootNodeEntry = new RTreeEntry<>(newRootNode, targetNode);
            newRootNode.addEntry(newRootNodeEntry);
            targetNode.setParent(newRootNodeEntry);
            rootNode = newRootNode;
        }
    }

    private void updateNewNodeParentNodeAndAscend(RTreeNode<T> newNode, RTreeEntry<T> targetNodeParent) {
        RTreeEntry<T> newNodeParent = new RTreeEntry<>(
                targetNodeParent.getLocatedNode(),
                newNode
        );
        newNode.setParent(newNodeParent);
        newNodeParent.getLocatedNode().addEntry(newNodeParent);
        splitNodeIfNeed(newNodeParent.getLocatedNode());
    }

    private void condenseTree(RTreeNode<T> node, Set<RTreeNode<T>> eliminatedNodeSet) {
        if (node == rootNode) {
            eliminatedNodeSet.forEach(eliminatedNode -> {
                for (RTreeEntry<T> entry : eliminatedNode.getEntries()) {
                    this.insert(entry);
                }
            });
            return;
        }

        if (node.getEntries().size() < loadFactor / 2) {
            node.getParent().setChildren(null);
            if (!node.isLeafNode()) {
                node.getParent().getLocatedNode().removeEntry(node.getParent());
            }
            eliminatedNodeSet.add(node);
        }

        condenseTree(node.getParent().getLocatedNode(), eliminatedNodeSet);
    }


    private void shortenTreeIfNeed() {
        if (rootNode.getEntries().size() == 1) {
            //shorten tree
            RTreeNode<T> rootChildNode = rootNode.getEntries().get(0).getLocatedNode();
            rootChildNode.setParent(null);
            rootNode = rootChildNode;
        }
    }

    private void checkLoadFactor(int loadFactor) {
        if (loadFactor < 1) {
            throw new IllegalArgumentException("load factor must greater than 0");
        }
    }

}
