package com.focusit.notification;

import hudson.Extension;
import hudson.model.Descriptor;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.util.logging.Logger;

/**
 * Created by doki on 10.10.17.
 */
@Extension
public class GlobalNotificationConfig extends GlobalConfiguration {
    private static final Logger LOGGER = Logger.getLogger(Descriptor.class.getName());
    private Endpoint endpoint;

    public GlobalNotificationConfig() {
        load();
    }

    public static GlobalNotificationConfig get() {
        return GlobalConfiguration.all().get(GlobalNotificationConfig.class);
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this,json);
        save();
        return true;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }
}
