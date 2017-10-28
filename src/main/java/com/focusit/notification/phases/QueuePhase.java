package com.focusit.notification.phases;

import com.focusit.notification.model.JobState;
import com.focusit.notification.notification.Endpoint;
import com.focusit.notification.GlobalNotificationConfig;
import com.focusit.notification.model.QueueState;
import com.focusit.notification.notification.Format;
import com.focusit.notification.notification.Protocol;
import hudson.EnvVars;
import hudson.model.Project;
import hudson.model.Queue;
import jenkins.model.Jenkins;

import java.util.Date;

/**
 * Created by doki on 14.10.17.
 */
public enum QueuePhase {
    WAITING_IN, WAITING_OUT, BLOCK_IN, BLOCK_OUT, BUILD_IN, BUILD_OUT, LEAVE;

    public void handle(Queue.Item queueItem){
        GlobalNotificationConfig cfg = GlobalNotificationConfig.get();
        if(cfg.getEnabled()){
            Endpoint target = cfg.getEndpoint();
            QueueState state = new QueueState();
            state.setState(this .toString());
            state.setTimestamp(new Date().getTime());
            state.setQueueId(queueItem.getId());
            state.setBuildName(queueItem.task.getName());
            state.setQueueLength(Jenkins.getInstance().getQueue().getItems().length);

            if(queueItem.task instanceof Project){
                Project prj = (Project) queueItem.task;
//                state.setName(prj.getName());
                state.setBuildId(prj.getNextBuildNumber());
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
