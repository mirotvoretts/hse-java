package hse.java.lectures.lecture6.tasks.synchronizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamingMonitor {
    private final List<Integer> orderedIds;
    private final Map<Integer, Integer> idToIndex;
    private final int ticksPerWriter;
    private final int totalTicks;
    private final int[] ticksDone;
    private int currentIndex = 0;
    private int totalDone = 0;
    private boolean isFinished = false;

    public StreamingMonitor(List<Integer> orderedIds, int ticksPerWriter) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new IllegalArgumentException("orderedIds must be non-empty");
        }
        if (ticksPerWriter < 0) {
            throw new IllegalArgumentException("ticksPerWriter must be non-negative");
        }
        this.orderedIds = new ArrayList<>(orderedIds);
        this.ticksPerWriter = ticksPerWriter;
        this.totalTicks = ticksPerWriter * orderedIds.size();
        this.ticksDone = new int[orderedIds.size()];
        this.idToIndex = new HashMap<>();
        for (int i = 0; i < orderedIds.size(); i++) {
            idToIndex.put(orderedIds.get(i), i);
        }
    }

    public synchronized boolean waitForTurn(int id) throws InterruptedException {
        Integer index = idToIndex.get(id);
        if (index == null) {
            throw new IllegalArgumentException("Unknown writer id: " + id);
        }
        while (true) {
            if (isFinished) return false;
            if (currentIndex == index && ticksDone[index] < ticksPerWriter) {
                return true;
            }
            wait();
        }
    }

    public synchronized void doneTick(int id) {
        Integer index = idToIndex.get(id);
        if (index == null) {
            throw new IllegalArgumentException("Unknown writer id: " + id);
        }
        ticksDone[index]++;
        totalDone++;
        if (totalDone >= totalTicks) {
            isFinished = true;
            notifyAll();
            return;
        }

        int n = ticksDone.length;
        for (int i = 1; i <= n; i++) {
            int candidate = (currentIndex + i) % n;
            if (ticksDone[candidate] < ticksPerWriter) {
                currentIndex = candidate;
                break;
            }
        }
        notifyAll();
    }

    public synchronized void awaitFinish() throws InterruptedException {
        while (!isFinished) {
            wait();
        }
    }
}
