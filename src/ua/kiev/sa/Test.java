package ua.kiev.sa;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        MyExecutorService myExecutorService = MyExecutors.newFixedThreadPool(2, 25);
        List<String> nonExecutedTasks = new ArrayList<>();

        for (int x = 0; x < 10; x++) {
            executeTask(myExecutorService, nonExecutedTasks, x);
        }
        Thread.sleep(2000);
        for (int x = 10; x < 15; x++) {
            executeTask(myExecutorService, nonExecutedTasks, x);
        }
        Thread.sleep(2000);
        for (int x = 15; x < 20; x++) {
            executeTask(myExecutorService, nonExecutedTasks, x);
        }
        Thread.sleep(2000);
        for (int x = 20; x < 30; x++) {
            executeTask(myExecutorService, nonExecutedTasks, x);
        }

        for (int x = 30; x < 60; x++) {
            executeTask(myExecutorService, nonExecutedTasks, x);
        }
        Thread.sleep(3000);
        myExecutorService.shutdownNow();
        System.out.println("Skipped tasks: "+ String.join(",", nonExecutedTasks));
    }

    private static void executeTask(MyExecutorService myExecutorService, List<String> nonExecutedTasks, int x) {
        try {
            myExecutorService.execute(new Task(x));
        } catch (WorkQueueIsFullException e) {
            System.out.println(e.getMessage().replace("{x}", String.valueOf(x)));
            nonExecutedTasks.add(String.valueOf(x));
        }
    }
}
