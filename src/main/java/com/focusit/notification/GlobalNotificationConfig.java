package com.focusit.notification;

import com.focusit.notification.notification.Endpoint;
import com.focusit.notification.notification.HostnamePort;
import hudson.Extension;
import hudson.model.Descriptor;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.util.logging.Logger;

/**
 * Created by doki on 10.10.17.
 */
@Extension
public class GlobalNotificationConfig extends GlobalConfiguration {
    private static final Logger LOGGER = Logger.getLogger(Descriptor.class.getName());
    private String url;
    private Integer timeout;
    private Integer retries;
    private Boolean enabled = Boolean.FALSE;

    @DataBoundConstructor
    public GlobalNotificationConfig(String url, Integer timeout, Integer retries, Boolean enabled) {
        this.url = url;
        this.timeout = timeout;
        this.retries = retries;
        this.enabled = enabled;
    }

    public GlobalNotificationConfig() {
        load();
    }

    public static GlobalNotificationConfig get() {
        return GlobalConfiguration.all().get(GlobalNotificationConfig.class);
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this,json);
        if(HostnamePort.parseUrl(this.getUrl())==null)
        {
            throw new FormException("Global notification host / port is not valid", "url");
        }
        if(this.timeout==null){
            throw new FormException("Global notification timeout is not valid", "timeout");
        }
        if(this.retries==null){
            throw new FormException("Global notification retry count is not valid", "retries");
        }
        save();
        return true;
    }

    public Endpoint getEndpoint() {
        return new Endpoint(url, timeout, retries);
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }
}
