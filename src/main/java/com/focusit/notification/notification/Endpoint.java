package com.focusit.notification.notification;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created by doki on 11.10.17.
 */
public class Endpoint {
    private String url;
    private Integer timeout;
    private Integer retries;

    public Endpoint(){

    }

    @DataBoundConstructor
    public Endpoint(String url, Integer timeout, Integer retries) {
        this.url = url;
        this.timeout = timeout;
        this.retries = retries;
    }

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
