package hse.java.lectures.lecture6.tasks.synchronizer;

import lombok.Getter;

import java.io.PrintStream;

@Getter
public class StreamWriter implements Runnable {

    private final String message;
    private final int id;
    private final PrintStream output;
    private final Runnable onTick;
    private volatile StreamingMonitor monitor;

    public StreamWriter(int id, String message, PrintStream output, Runnable onTick) {
        this.message = message;
        this.id = id;
        this.output = output;
        this.onTick = onTick;
    }

    public void attachMonitor(StreamingMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                StreamingMonitor monitor = this.monitor;
                if (monitor == null) {
                    Thread.yield();
                    continue;
                }
                boolean shouldRun = monitor.waitForTurn(id);
                if (!shouldRun) {
                    return;
                }
                output.print(message);
                onTick.run();
                monitor.doneTick(id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
