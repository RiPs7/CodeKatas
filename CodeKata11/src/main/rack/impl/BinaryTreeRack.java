package main.rack.impl;

import main.datastructure.impl.BinaryTree;
import main.rack.Rack;

import java.util.List;

public class BinaryTreeRack implements Rack {

    private BinaryTree tree;

    public BinaryTreeRack () {
        tree = new BinaryTree();
    }

    @Override
    public void add (Object obj) {
        tree.insert(obj);
    }

    @Override
    public List<Object> order () {
        return tree.traverseInOrder();
    }

}
