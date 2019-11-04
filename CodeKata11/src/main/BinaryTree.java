package main;

import java.util.ArrayList;
import java.util.List;

class BinaryTree {

    private static class Node {
        private Object value;
        private Node left, right;

        Node (Object value) {
            this.value = value;
        }
    }

    private Node root;

    BinaryTree () {
    }

    void insert (Object value) {
        final Node newNode = new Node(value);
        if (root == null) {
            root = newNode;
        } else {
            insert(newNode);
        }
    }

    private void insert (final Node newNode) {
        // Traverse down the tree to insert it.
        Node current = root;
        while (current.left != null || current.right != null) { // while at least one child is not null
            final int comparison = compare(newNode.value, current.value);
            if (current.left == null && comparison == -1) { // if the new node is meant to go left but left child is
                // null, then insert it and break.
                current.left = newNode;
                break;
            } else if (current.right == null && comparison == 1) { // if the new node is meant to go right but right
                // child
                // is null then insert it and break.
                current.right = newNode;
                break;
            }
            // otherwise keep traversing.
            if (current.left != null && comparison == -1) {
                current = current.left;
            } else if (current.right != null && comparison == 1) {
                current = current.right;
            }
        }
        // if both children are null, insert it according to the comparison.
        final int comparison = compare(newNode.value, current.value);
        if (current.left == null && comparison == -1) { // if the new node is meant to go left but left child is
            // null, then insert it and break.
            current.left = newNode;
        } else if (current.right == null && comparison == 1) { // if the new node is meant to go right but right
            // child
            // is null then insert it and break.
            current.right = newNode;
        }
    }

    List<Object> traverseInOrder () {
        return traverseInOrderRecursive(root);
    }

    private List<Object> traverseInOrderRecursive (Node node) {
        List<Object> traverseInOrderFromNode = new ArrayList<>();
        if (node.left != null) {
            traverseInOrderFromNode.addAll(traverseInOrderRecursive(node.left));
        }
        traverseInOrderFromNode.add(node.value);
        if (node.right != null) {
            traverseInOrderFromNode.addAll(traverseInOrderRecursive(node.right));
        }
        return traverseInOrderFromNode;
    }

    private int compare (Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) {
            return (int) a - (int) b < 0 ? -1 : 1;
        } else if (a instanceof Double && b instanceof Double) {
            return (double) a - (double) b < 0 ? -1 : 1;
        } else if (a instanceof Character && b instanceof Character) {
            return (char) a - (char) b < 0 ? -1 : 1;
        }
        throw new IllegalArgumentException("Unrecognized argument type. Only integers and strings are supported");
    }


}
