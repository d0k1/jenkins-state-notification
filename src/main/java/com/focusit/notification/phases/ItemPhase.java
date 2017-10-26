package com.focusit.notification.phases;

import com.focusit.notification.GlobalNotificationConfig;
import com.focusit.notification.model.ItemState;
import com.focusit.notification.notification.Endpoint;
import com.focusit.notification.notification.Format;
import com.focusit.notification.notification.Protocol;
import hudson.model.AbstractItem;
import hudson.model.Item;

import java.io.IOException;

public enum ItemPhase {
    LOCATION_CHANGED, CREATED, COPIED, DELETED, RENAMED, UPDATED;

    public void handle(long timestamp, Item item, String oldName) {
        GlobalNotificationConfig cfg = GlobalNotificationConfig.get();
        if (cfg.getEnabled() && item instanceof AbstractItem) {
            AbstractItem aItem = (AbstractItem) item;
            Endpoint target = cfg.getEndpoint();

            ItemState state = new ItemState();
            state.setTimestamp(timestamp);
            state.setFullName(aItem.getFullName());
            state.setName(aItem.getName());
            state.setState(this.toString());

            try {
                state.setConfiguration(aItem.getConfigFile().asString());
            } catch (IOException e){
                state.setConfiguration("Error reading configuration: "+e.toString());
            }

            if(oldName!=null && !oldName.trim().isEmpty()){
                state.setOldName(oldName);
            }

            int triesRemaining = target.getRetries();
            boolean failed = false;
            do {
                try {
                    Protocol.HTTP.send(target.getUrl(),
                            Format.JSON.serialize(state),
                            target.getTimeout(),
                            true);
                } catch (Throwable error) {
                    failed = true;
                    if (triesRemaining > 0) {
                        //listener.getLogger().println( String.format( "Reattempting to notify endpoint with %s (%d tries remaining)", urlIdString, triesRemaining));
                    }
                }
            }while(failed && --triesRemaining >= 0);
        }
    }

}
