package hse.java.lectures.lecture3.practice.randomSet;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Node<T> {
    @Setter
    private T value;
    @Setter
    private Node<T> left;
    @Setter
    private Node<T> right;
    private int size;

    public Node(T value) {
        size = 1;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public void recalculateSize() {
        size = 1;
        if (left != null) {
            size += left.getSize();
        }
        if (right != null) {
            size += right.getSize();
        }
    }
}
