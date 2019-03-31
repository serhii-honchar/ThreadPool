package ua.kiev.sa;

import lombok.Getter;

@Getter
public class Task implements Runnable {
    private Integer taskNumber;

    @Override
    public void run() {
        System.out.println("\nRESULT ="+taskNumber+" was obtained by "+Thread.currentThread().getName());

    }

    public Task(Integer taskNumber) {
        this.taskNumber = taskNumber;
    }


}
