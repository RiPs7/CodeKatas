package main;

import main.datastructure.RedBlackTree;

import java.util.List;

public class Rack {

    private RedBlackTree tree;

    public Rack () {
        tree = new RedBlackTree();
    }

    public void add (Object obj) {
        tree.insert(obj);
    }

    public List<Object> order () {
        return tree.traverseInOrder();
    }

}
