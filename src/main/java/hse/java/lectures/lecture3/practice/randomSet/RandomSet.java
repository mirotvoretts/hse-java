package hse.java.lectures.lecture3.practice.randomSet;

import java.util.Random;

public class RandomSet<T> {
    private Node<T> root;
    private final Random random = new Random();

    private Node<T> recursiveInsert(Node<T> currentNode, T value) {
        if (currentNode == null) {
            return new Node<>(value);
        }
        if (currentNode.getValue().equals(value)) {
            return null;
        }
        if (currentNode.getValue().hashCode() > value.hashCode()) {
            currentNode.setLeft(recursiveInsert(currentNode.getLeft(), value));
        } else  {
            currentNode.setRight(recursiveInsert(currentNode.getRight(), value));
        }
        currentNode.recalculateSize();
        return currentNode;
    }

    private Node<T> findMin(Node<T> node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    private Node<T> recursiveDelete(Node<T> currentNode, T value) {
        if (currentNode == null) {
            return null;
        }
        if (currentNode.getValue().equals(value)) {
            if (currentNode.getLeft() == null) {
                return currentNode.getRight();
            }
            if (currentNode.getRight() == null) {
                return currentNode.getLeft();
            }
            Node<T> minNode = findMin(currentNode.getRight());
            currentNode.setValue(minNode.getValue());
            currentNode.setRight(recursiveDelete(currentNode.getRight(), minNode.getValue()));
        } else if (currentNode.getValue().hashCode() > value.hashCode()) {
            currentNode.setLeft(recursiveDelete(currentNode.getLeft(), value));
        } else {
            currentNode.setRight(recursiveDelete(currentNode.getRight(), value));
        }
        currentNode.recalculateSize();
        return currentNode;
    }

    private boolean find(Node<T> node, T value) {
        if (node == null) {
            return false;
        }
        if (node.getValue().equals(value)) {
            return true;
        }
        return find(node.getLeft(), value) || find(node.getRight(), value);
    }

    private Node<T> getRandomNode(Node<T> currentNode) {
        int leftSize = currentNode.getLeft() != null ? currentNode.getLeft().getSize() : 0;
        int rightSize = currentNode.getRight() != null ? currentNode.getRight().getSize() : 0;
        int totalSize = leftSize + rightSize + 1;
        int randomIndex = random.nextInt(totalSize);
        if (randomIndex < leftSize) {
            return getRandomNode(currentNode.getLeft());
        } else if (randomIndex == leftSize) {
            return currentNode;
        } else {
            return getRandomNode(currentNode.getRight());
        }
    }

    public RandomSet() {
        root = null;
    }

    public boolean insert(T value) {
        if (root == null) {
            throw new EmptySetException("Set is empty");
        }
        var result = recursiveInsert(root, value);
        if (result == null) {
            return false;
        }
        root = result;
        return true;
    }

    public boolean remove(T value) {
        if (root == null) {
            throw new EmptySetException("Set is empty");
        }
        if (!contains(value)) {
            return false;
        }
        root = recursiveDelete(root, value);
        return true;
    }

    public boolean contains(T value) {
        if (root == null) {
            throw new EmptySetException("Set is empty");
        }
        return find(root, value);
    }

    public T getRandom() {
        if (root == null) {
            throw new EmptySetException("Set is empty");
        }
        return getRandomNode(root).getValue();
    }
}
