package com.focusit.notification.phases;

import java.util.Date;

import javax.annotation.Nullable;

import com.focusit.notification.GlobalNotificationConfig;
import com.focusit.notification.model.QueueState;
import com.focusit.notification.notification.Endpoint;
import com.focusit.notification.notification.Format;
import com.focusit.notification.notification.Protocol;

import hudson.model.Project;
import hudson.model.Queue;
import jenkins.model.Jenkins;

/**
 * Created by doki on 14.10.17.
 */
public enum QueuePhase {
    WAITING_IN, WAITING_OUT, BLOCK_IN, BLOCK_OUT, BUILD_IN, BUILD_OUT, LEAVE, CHECK;

    public void handle(@Nullable Queue.Item queueItem)
    {
        GlobalNotificationConfig cfg = GlobalNotificationConfig.get();
        if(cfg.getEnabled()){
            Endpoint target = cfg.getEndpoint();
            QueueState state = new QueueState();
            state.setState(this .toString());
            state.setTimestamp(new Date().getTime());

            if (queueItem == null)
            {
                Jenkins.getInstance().getQueue().maintain();
            }

            Queue.Item items[] = Jenkins.getInstance().getQueue().getItems();
            state.setQueueLength(items.length);
            int waiting = 0;
            int blocked = 0;
            int buildables = 0;

            for(Queue.Item item:items){
                if(item instanceof Queue.WaitingItem){
                    waiting++;
                }
                if(item instanceof Queue.BlockedItem){
                    blocked++;
                }
                if(item instanceof Queue.BuildableItem){
                    buildables++;
                }
            }
            state.setBlockedLength(blocked);
            state.setWaitingLength(waiting);
            state.setBuildableLength(buildables);

            if (queueItem != null)
            {
                state.setQueueId(queueItem.getId());
                state.setBuildName(queueItem.task.getName());
                if (queueItem.task instanceof Project)
                {
                    Project prj = (Project)queueItem.task;
                    state.setBuildId(prj.getNextBuildNumber());
                }
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
