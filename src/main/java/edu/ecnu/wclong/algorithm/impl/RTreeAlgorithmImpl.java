package edu.ecnu.wclong.algorithm.impl;

import edu.ecnu.wclong.algorithm.RTreeAlgorithm;
import edu.ecnu.wclong.sdo.*;
import edu.ecnu.wclong.sdo.dto.RTreeNodeSplitResult;
import edu.ecnu.wclong.separator.RTreeNodeSeparator;
import edu.ecnu.wclong.util.RectangleUtil;

import java.util.List;
import java.util.Set;

public class RTreeAlgorithmImpl implements RTreeAlgorithm {

    @Override
    public <T> void search(Rectangle searchRectangle, RTreeNode<T> rootNode, Set<RTreeEntry<T>> resultSet) {
        //pruning directly if the node's rectangle does not overlap the search rectangle
        if (!searchRectangle.isOverlap(rootNode.getRectangle())) return;

        for (RTreeEntry<T> entry : rootNode.getEntries()) {
            if (!searchRectangle.isOverlap(entry.getRectangle())) continue;

            if (rootNode.isLeafNode()) {
                resultSet.add(entry);
            } else {
                search(searchRectangle, entry.getChildren(), resultSet);
            }
        }
    }

    @Override
    public <T> RTreeNode<T> chooseLeaf(RTreeNode<T> rootNode, RTreeEntry<T> newEntry) {
        if (null == rootNode || rootNode.isLeafNode()) return rootNode;

        RTreeNode<T> selectedNode = null;
        double selectNodeEnlargeArea = Double.MAX_VALUE;
        for (RTreeEntry<T> entry : rootNode.getEntries()) {
            Rectangle newRectangle = RectangleUtil.getBoundedRectangleByTwoRectangles(
                    entry.getRectangle(),
                    newEntry.getRectangle()
            );

            double entryArea = entry.getRectangle().getArea();
            double enlargeArea = newRectangle.getArea() - entryArea;

            if (enlargeArea < selectNodeEnlargeArea
                    || (enlargeArea == selectNodeEnlargeArea
                    && entryArea < selectedNode.getRectangle().getArea()
            )) {
                selectNodeEnlargeArea = enlargeArea;
                selectedNode = entry.getChildren();
            }
        }

        return chooseLeaf(selectedNode, newEntry);
    }


    @Override
    public <T> RTreeNode<T> findLeaf(RTreeNode<T> rootNode, RTreeEntry<T> targetEntry) {
        for (RTreeEntry<T> entry : rootNode.getEntries()) {
            if (entry.getRectangle().isOverlap(targetEntry.getRectangle())) {
                return findLeaf(entry.getChildren(), targetEntry);
            }

            if (rootNode.isLeafNode() && entry.equals(targetEntry)) {
                return rootNode;
            }
        }
        return null;
    }

    @Override
    public <T> RTreeNodeSplitResult<T> splitNode(RTreeNodeSeparator rTreeNodeSeparator, List<RTreeEntry<T>> entries) {
        return rTreeNodeSeparator.split(entries);
    }


}
