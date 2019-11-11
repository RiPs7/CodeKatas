package main.datastructure.impl;

import java.util.ArrayList;
import java.util.List;

import static main.datastructure.impl.RedBlackTree.RedBlackNode.COLOR.BLACK;
import static main.datastructure.impl.RedBlackTree.RedBlackNode.COLOR.RED;

public class RedBlackTree {

    private RedBlackNode root;

    private void rotateLeft (RedBlackNode node) {
        RedBlackNode right = node.right;
        node.right = right.left;
        if (right.left != null) {
            right.left.parent = node;
        }
        right.parent = node.parent;
        if (node.parent == null) {
            root = right;
        } else {
            if (node == node.parent.left) {
                node.parent.left = right;
            } else {
                node.parent.right = right;
            }
        }
        right.left = node;
        node.parent = right;
    }

    private void rotateRight (RedBlackNode node) {
        RedBlackNode left = node.left;
        node.left = left.right;
        if (left.right != null) {
            left.right.parent = node;
        }
        left.parent = node.parent;
        if (node.parent == null) {
            root = left;
        } else {
            if (node == node.parent.right) {
                node.parent.right = left;
            } else {
                node.parent.left = left;
            }
        }
        left.right = node;
        node.parent = left;
    }

    public void insert (Object value) {
        final RedBlackNode newNode = new RedBlackNode(value);
        if (root == null) {
            root = newNode;
        } else {
            insert(newNode);
        }
    }

    private void insert (RedBlackNode newNode) {
        // Traverse down the tree to insert it.
        RedBlackNode current = root;
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
        newNode.parent = current;
        newNode.color = RED;
        while (newNode != root && newNode.parent.color == RED) {
            if (newNode.parent == newNode.parent.parent.left) {
                RedBlackNode uncle = newNode.parent.parent.right;
                if (uncle != null && uncle.color == RED) {
                    newNode.parent.color = BLACK;
                    uncle.color = BLACK;
                    newNode.parent.parent.color = RED;
                    newNode = newNode.parent.parent;
                } else {
                    if (newNode == newNode.parent.right) {
                        newNode = newNode.parent;
                        rotateLeft(newNode);
                    } else {
                        newNode.parent.color = BLACK;
                        newNode.parent.parent.color = RED;
                        rotateRight(newNode.parent.parent);
                    }
                }
            } else if (newNode.parent == newNode.parent.parent.right) {
                RedBlackNode uncle = newNode.parent.parent.left;
                if (uncle != null && uncle.color == RED) {
                    newNode.parent.color = BLACK;
                    uncle.color = BLACK;
                    newNode.parent.parent.color = RED;
                    newNode = newNode.parent.parent;
                } else {
                    if (newNode == newNode.parent.left) {
                        newNode = newNode.parent;
                        rotateRight(newNode);
                    } else {
                        newNode.parent.color = BLACK;
                        newNode.parent.parent.color = RED;
                        rotateLeft(newNode.parent.parent);
                    }
                }
            }
            root.color = BLACK;
        }
    }

    public List<Object> traverseInOrder () {
        return traverseInOrderRecursive(root);
    }

    private List<Object> traverseInOrderRecursive (RedBlackNode node) {
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

    static class RedBlackNode {
        enum COLOR {
            RED,
            BLACK
        }

        Object value;
        RedBlackNode left, right, parent;
        COLOR color;

        RedBlackNode (Object value) {
            this.value = value;
        }
    }
}
