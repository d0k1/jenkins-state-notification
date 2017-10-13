package com.focusit.notification;

/**
 * Created by doki on 11.10.17.
 */
public class Endpoint {
    private String url;
    private Integer timeout;
    private Integer retries;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = Integer.parseInt(timeout);
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = Integer.parseInt(retries);
    }

}
