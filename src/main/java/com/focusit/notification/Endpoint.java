package com.focusit.notification;

/**
 * Created by doki on 11.10.17.
 */
public class Endpoint {
    private String url;
    private String timeout;
    private String retries;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

}
