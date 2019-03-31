package ua.kiev.sa;

public interface MyExecutorService {
    /**
     * Executes the given command in a thread from thread pool
     *
     * @param command the runnable task
     *
     * @throws WorkQueueIsFullException if this task cannot be accepted for execution due
     *                                  to workQueue is full
     */
    void execute(Runnable command) throws WorkQueueIsFullException;

    /**
     * Attempts to stop all actively executing tasks.
     */
    void shutdownNow();

}
