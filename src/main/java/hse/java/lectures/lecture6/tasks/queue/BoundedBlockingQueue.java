package hse.java.lectures.lecture6.tasks.queue;

import java.util.ArrayDeque;
import java.util.Deque;

public class BoundedBlockingQueue<T> {
    private final Deque<T> queue;
    private final int capacity;

    public BoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        this.queue = new ArrayDeque<>();
        this.capacity = capacity;
    }

    public void put(T item) throws InterruptedException {
        if (item == null) {
            throw new IllegalArgumentException("Item must be non null");
        }

        synchronized (queue) {
            while (queue.size() == capacity) {
                queue.wait();
            }
            queue.addLast(item);
            queue.notifyAll();
        }
    }

    public T take() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            T item = queue.removeFirst();
            queue.notifyAll();
            return item;
        }
    }

    public int size() {
        synchronized (queue) {
            return queue.size();
        }
    }

    public int capacity() {
        return this.capacity;
    }
}
