package hse.java.lectures.lecture3.practice.randomSet;

import java.util.Random;

public class RandomSet<T extends Comparable<T>> {
    private final Treap<T> treap;
    private final Random random = new Random();

    public RandomSet() {
        treap = new Treap<>();
    }

    public boolean insert(T value) {
        return treap.tryInsert(value);
    }

    public boolean remove(T value) {
        return treap.tryRemove(value);
    }

    public boolean contains(T value) {
        return treap.contains(value);
    }

    public T getRandom() {
        if (treap.getSize() == 0) {
            throw new EmptySetException("Set is empty");
        }
        int randomIndex = random.nextInt(treap.getSize());
        return treap.get(randomIndex);
    }
}
