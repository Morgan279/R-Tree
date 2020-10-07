package edu.ecnu.wclong.algorithm;

import edu.ecnu.wclong.sdo.*;
import edu.ecnu.wclong.sdo.dto.RTreeNodeSplitResult;
import edu.ecnu.wclong.separator.RTreeNodeSeparator;

import java.util.List;
import java.util.Set;

public interface RTreeAlgorithm {
    <T> void search(Rectangle searchRectangle, RTreeNode<T> rootNode, Set<RTreeEntry<T>> resultSet);

    <T> RTreeNode<T> chooseLeaf(RTreeNode<T> rootNode, RTreeEntry<T> newEntry);

    <T> RTreeNode<T> findLeaf(RTreeNode<T> rootNode, RTreeEntry<T> targetEntry);

    <T> RTreeNodeSplitResult<T> splitNode(RTreeNodeSeparator rTreeNodeSeparator, List<RTreeEntry<T>> entries);

}
