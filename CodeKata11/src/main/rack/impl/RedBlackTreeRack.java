package main.rack.impl;

import main.datastructure.impl.RedBlackTree;
import main.rack.Rack;

import java.util.List;

public class RedBlackTreeRack implements Rack {

    private RedBlackTree tree;

    public RedBlackTreeRack () {
        tree = new RedBlackTree();
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
