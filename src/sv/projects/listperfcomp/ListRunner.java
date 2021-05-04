package sv.projects.listperfcomp;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ListRunner implements Callable<Long> {
    private List<Integer> list;
    private int start;
    private int end;
    private CountDownLatch latch;
    private PerformanceComparator.Action action;

    public ListRunner(List<Integer> list, int start, int end, CountDownLatch latch, PerformanceComparator.Action action) {
        this.list = list;
        this.start = start;
        this.end = end;
        this.latch = latch;
        this.action = action;
    }

    public Long call() throws InterruptedException {
        latch.await();
        long startTime = System.nanoTime();
        for (int i = start; i < end; i++) {
            switch (action) {
                case ACCESS_MEMBER:
                    list.get(i);
                    break;
                case ADD_MEMBER:
                    list.add(i);
            }
        }
        return System.nanoTime() - startTime;
    }
}
