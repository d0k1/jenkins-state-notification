package com.focusit.notification.model;

/**
 * Created by doki on 14.10.17.
 */
public class ExecutorsState {
    private String type="ExecutorsState";
    private Integer executors;
    private Integer executorsBusy;
    private long timestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getExecutors() {
        return executors;
    }

    public void setExecutors(Integer executors) {
        this.executors = executors;
    }

    public Integer getExecutorsBusy() {
        return executorsBusy;
    }

    public void setExecutorsBusy(Integer executorsBusy) {
        this.executorsBusy = executorsBusy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
