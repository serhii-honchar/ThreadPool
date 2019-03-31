package ua.kiev.sa;

public class WorkQueueIsFullException extends Exception {
    public WorkQueueIsFullException(String s) {
        super(s);
    }
}
