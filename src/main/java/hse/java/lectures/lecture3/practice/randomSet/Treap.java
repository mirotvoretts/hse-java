package hse.java.lectures.lecture3.practice.randomSet;

import java.util.Optional;
import java.util.Random;

public class Treap<T extends  Comparable<T>> {
    private Node<T> root;
    private final Random random;

    public Treap() {
        root = null;
        random = new Random();
    }

    private boolean findByValue(Node<T> node, T value) {
        if (node == null) {
            return false;
        }
        int compareResult = node.getValue().compareTo(value);
        if (compareResult == 0) {
            return true;
        }
        if (compareResult > 0) {
            return findByValue(node.getLeft(), value);
        }
        return findByValue(node.getRight(), value);
    }

    private Optional<T> findByIndex(Node<T> currentNode, int index) {
        if (currentNode == null) {
            return Optional.empty();
        }
        int leftSize = currentNode.getLeft() != null ? currentNode.getLeft().getSize() : 0;
        if (index < leftSize) {
            return findByIndex(currentNode.getLeft(), index);
        } else if (index == leftSize) {
            return Optional.of(currentNode.getValue());
        } else {
            return findByIndex(currentNode.getRight(), index - leftSize - 1);
        }
    }

    private Pair<Node<T>, Node<T>> split(Node<T> currentNode, T value) {
        if (currentNode == null) {
            return new Pair<>(null, null);
        }
        int compareResult = currentNode.getValue().compareTo(value);
        if (compareResult == 0) {
            return new Pair<>(currentNode.getLeft(), currentNode.getRight());
        } else if (compareResult < 0) {
            var splitResult = split(currentNode.getRight(), value);
            currentNode.setRight(splitResult.left());
            currentNode.recalculateSize();
            return new Pair<>(currentNode, splitResult.right());
        } else {
            var splitResult = split(currentNode.getLeft(), value);
            currentNode.setLeft(splitResult.right());
            currentNode.recalculateSize();
            return new Pair<>(splitResult.left(), currentNode);
        }
    }

    private Node<T> merge(Node<T> leftNode, Node<T> rightNode) {
        if (leftNode == null) {
            return rightNode;
        }
        if (rightNode == null) {
            return leftNode;
        }
        if (random.nextInt(leftNode.getSize() + rightNode.getSize()) < leftNode.getSize()) {
            leftNode.setRight(merge(leftNode.getRight(), rightNode));
            leftNode.recalculateSize();
            return leftNode;
        } else {
            rightNode.setLeft(merge(leftNode, rightNode.getLeft()));
            rightNode.recalculateSize();
            return rightNode;
        }
    }

    public int getSize() {
        return root != null ? root.getSize() : 0;
    }

    public boolean contains(T value) {
        return findByValue(root, value);
    }

    public boolean tryInsert(T value) {
        if (contains(value)) {
            return false;
        }
        var splitResult = split(root, value);
        root = merge(merge(splitResult.left(), new Node<>(value)), splitResult.right());
        return true;
    }

    public boolean tryRemove(T value) {
        if (!contains(value)) {
            return false;
        }
        var splitResult = split(root, value);
        root = merge(splitResult.left(), splitResult.right());
        return true;
    }

    public T get(int index) {
        return findByIndex(root, index).orElseThrow(() -> new IllegalArgumentException("Index out of bounds"));
    }
}
