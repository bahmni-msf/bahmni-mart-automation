package org.bahmni.mart.automation.pollers;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class MethodPoller<T> {

    private int interval;

    private Supplier<T> pollMethod = null;
    private Predicate<T> pollResultPredicate = null;

    public MethodPoller<T> poll(int pollIntervalMillis) {
        this.interval = pollIntervalMillis;
        return this;
    }

    public MethodPoller<T> method(Supplier<T> supplier) {
        pollMethod = supplier;
        return this;
    }

    public MethodPoller<T> until(Predicate<T> predicate) {
        pollResultPredicate = predicate;
        return this;
    }

    public T execute() throws InterruptedException {
        T result = null;
        boolean pollSucceeded = false;
        while (!pollSucceeded) {
            result = pollMethod.get();
            pollSucceeded = pollResultPredicate.test(result);
            if (!pollSucceeded)
                Thread.sleep(interval);
        }

        return result;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}