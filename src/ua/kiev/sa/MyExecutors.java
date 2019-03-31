package ua.kiev.sa;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.IntStream;

public class MyExecutors implements MyExecutorService {
    final private Queue<Runnable> workingQueue = new ConcurrentLinkedDeque<>();
    final private List<Thread> threadPool = new ArrayList<>();
    private int workQueueSize;
    private int poolSize;
    private boolean isAlive = true;

    private MyExecutors(int poolSize, int workQueueSize) {
        this.workQueueSize = workQueueSize;
        this.poolSize = poolSize;
        initiatePool();
    }

    /**
     * Creates a new ThreadPool with the given initial number of threads and work queue size
     *
     * @param poolSize      the number of threads to keep in the pool, even
     *                      if they are idle
     * @param workQueueSize the queue to use for holding tasks before they are
     *                      executed.  This queue will hold only the {@code Runnable}
     *                      tasks submitted by the {@code execute} method.
     */

    public static MyExecutorService newFixedThreadPool(int poolSize, int workQueueSize) {
        return new MyExecutors(poolSize, workQueueSize);
    }

    private void initiatePool() {
        IntStream.range(0, poolSize)
                .forEach(i -> {
                    Worker thread = new Worker();
                    thread.setName("Thread#" + i);
                    thread.start();
                    threadPool.add(thread);
                });
        printThreadPoolStatus();
    }

    public static MyExecutorService newFixedThreadPool(int poolSize) {
        return newFixedThreadPool(poolSize, 10);
    }

    @Override
    public void execute(Runnable command) throws WorkQueueIsFullException {
        synchronized (workingQueue) {
            if (workingQueue.size() >= workQueueSize) {
                throw new WorkQueueIsFullException("Task#{x} cannot be accepted for execution due to workQueue is full\n");
            }
            workingQueue.add(command);
            workingQueue.notify();
        }
    }

    @Override
    public void shutdownNow() {
        System.err.println("Shutting down");
        threadPool.forEach(Thread::interrupt);
        isAlive = false;
        printThreadPoolStatus();
    }


    private class Worker extends Thread {
        @Override
        public void run() {
            Runnable task = null;
            while (!isAlive) {
                synchronized (workingQueue) {
                    if (workingQueue.isEmpty()) {
                        System.err.println("Work queue is empty," + this.getName() + " is inside synchronized block. POOL STATE: ");
                        printThreadPoolStatus();
                        try {
                            System.err.println(this.getName() + " is waiting");
                            workingQueue.wait();
                        } catch (InterruptedException e) {
                            System.err.println(this.getName()+" "+e.getClass());
                        }
                    }
                    System.err.println(this.getName() + " getting task");
                    task = workingQueue.poll();

                }
                if (task != null) {
                    System.err.println(this.getName() + " executing task");
                    task.run();
                }
            }
        }
    }

    private void printThreadPoolStatus() {
        for (Thread thread : threadPool) {
            System.err.println(thread.getName() + "-----> " + thread.getState());
        }
        System.err.println("\n");
    }

}
