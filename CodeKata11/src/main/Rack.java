package main;

import java.util.List;

class Rack {

    private BinaryTree binaryTree;

    Rack () {
        binaryTree = new BinaryTree();
    }

    void add (Object obj) {
        binaryTree.insert(obj);
    }

    List<Object> order () {
        return binaryTree.traverseInOrder();
    }

}
