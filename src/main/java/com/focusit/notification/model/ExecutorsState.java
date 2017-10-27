package com.focusit.notification.model;

/**
 * Created by doki on 14.10.17.
 */
public class ExecutorsState {
    private String type="ExecutorsState";
    private Integer executors;
    private Integer executorsBusy;
    private long timestamp;
    private String state;
    private String buildName;
    private Integer buildId;
    private String nodeName;
    private String nodeLabel;
    private String nodeHost;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    public String getNodeHost() {
        return nodeHost;
    }

    public void setNodeHost(String nodeHost) {
        this.nodeHost = nodeHost;
    }
}
