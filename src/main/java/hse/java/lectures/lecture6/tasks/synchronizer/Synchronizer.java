package hse.java.lectures.lecture6.tasks.synchronizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Synchronizer {

    public static final int DEFAULT_TICKS_PER_WRITER = 10;
    private final List<StreamWriter> tasks;
    private final int ticksPerWriter;

    public Synchronizer(List<StreamWriter> tasks) {
        this(tasks, DEFAULT_TICKS_PER_WRITER);
    }

    public Synchronizer(List<StreamWriter> tasks, int ticksPerWriter) {
        this.tasks = tasks;
        this.ticksPerWriter = ticksPerWriter;
    }

    public void execute() {
        List<Integer> orderedIds = tasks.stream().map(StreamWriter::getId).sorted().collect(Collectors.toList());

        StreamingMonitor monitor = new StreamingMonitor(orderedIds, ticksPerWriter);

        List<Thread> workers = new ArrayList<>();
        for (StreamWriter writer : tasks) {
            writer.attachMonitor(monitor);
            Thread worker = new Thread(writer);
            worker.start();
            workers.add(worker);
        }

        try {
            monitor.awaitFinish();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
