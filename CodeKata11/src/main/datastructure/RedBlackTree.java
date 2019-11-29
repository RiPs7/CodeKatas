package main.datastructure;

import java.util.ArrayList;
import java.util.List;

import static main.datastructure.RedBlackTree.RedBlackNode.COLOR.BLACK;
import static main.datastructure.RedBlackTree.RedBlackNode.COLOR.RED;

/**
 * <p>
 * A red–black tree is a kind of self-balancing binary search tree in computer science. Each node of the binary tree
 * has an extra bit, and that bit is often interpreted as the color (red or black) of the node. These color bits are
 * used to ensure the tree remains approximately balanced during insertions and deletions.
 * </p>
 * <p>
 * Balance is preserved by painting each node of the tree with one of two colors in a way that satisfies certain
 * properties, which collectively constrain how unbalanced the tree can become in the worst case. When the tree is
 * modified, the new tree is subsequently rearranged and repainted to restore the coloring properties. The properties
 * are designed in such a way that this rearranging and recoloring can be performed efficiently.
 * </p>
 * <p>
 * The balancing of the tree is not perfect, but it is good enough to allow it to guarantee searching in O(log n)
 * time, where n is the total number of elements in the tree. The insertion and deletion operations, along with the
 * tree rearrangement and recoloring, are also performed in O(log n) time.
 * </p>
 * <p>
 * Tracking the color of each node requires only 1 bit of information per node because there are only two colors. The
 * tree does not contain any other data specific to its being a red–black tree so its memory footprint is almost
 * identical to a classic (uncolored) binary search tree. In many cases, the additional bit of information can be
 * stored at no additional memory cost.
 * </p>
 *
 * <table>
 *  Red–black tree
 *   <tr><td>Invented in 1972</td><td>By Rudolf Bayer</td></tr>
 *   <tr><td>Algorithm</td><td>Average Case</td><td>Worst case</td>
 *   <tr><td>Space</td><td>O(n)</td><td>O(n)</td>
 *   <tr><td>Search</td><td>O(log n)</td><td>O(log n)</td>
 *   <tr><td>Insert</td><td>O(log n)</td><td>O(log n)</td>
 *   <tr><td>Delete</td><td>O(log n)</td><td>O(log n)</td>
 * </table>
 */
public class RedBlackTree {

    private RedBlackNode root;

    public void insert (Object value) {
        RedBlackNode newNode = new RedBlackNode(value);
        if (root == null) {
            root = newNode;
            root.color = BLACK;
        } else {
            //INSERTION
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
            // update parent
            newNode.parent = current;
            // color RED upon insertion
            newNode.color = RED;
            // RE-BALANCING
            // start re-balancing and make our way up to th root
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
            RED, BLACK
        }

        Object value;
        RedBlackNode left, right, parent;
        COLOR color;

        RedBlackNode (Object value) {
            this.value = value;
        }
    }
}
