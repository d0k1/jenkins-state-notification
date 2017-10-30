package com.focusit.notification.phases;

import javax.annotation.Nullable;

import com.focusit.notification.GlobalNotificationConfig;
import com.focusit.notification.model.ExecutorsState;
import com.focusit.notification.notification.Endpoint;
import com.focusit.notification.notification.Format;
import com.focusit.notification.notification.Protocol;

import hudson.model.Computer;
import hudson.model.Run;
import jenkins.model.Jenkins;

/**
 * Created by doki on 14.10.17.
 */
public enum ExecutorPhase {
    ACQUIRED, RELEASED, ONLINE, OFFLINE, CONFIG_CHANGES, TEMP_ONLINE, TEMP_OFFLINE, CHECK;

    public void handle(long timestamp, @Nullable Run run, @Nullable Computer computer)
    {
        GlobalNotificationConfig cfg = GlobalNotificationConfig.get();
        if (cfg.getEnabled()) {
            Endpoint target = cfg.getEndpoint();

            ExecutorsState state = new ExecutorsState();
            state.setTimestamp(timestamp);
            state.setState(this.toString());

            if(run!=null) {
                state.setNodeName(run.getExecutor().getOwner().getNode().getDisplayName());
                state.setNodeLabel(run.getExecutor().getOwner().getNode().getLabelString());
                try {
                    state.setNodeHost(run.getExecutor().getOwner().getHostName());
                } catch (Exception e) {
                }

                state.setBuildName(run.getParent().getName());
                state.setBuildId(run.number);
            }
            else if (computer != null)
            {
                state.setNodeName(computer.getNode().getDisplayName());
                state.setNodeLabel(computer.getNode().getLabelString());
                try
                {
                    state.setNodeHost(computer.getHostName());
                }
                catch (Exception e)
                {
                }
            }

            final int[] executors = {0};
            final int[] executorsBusy = {0};

            for (Computer item : Jenkins.getInstance().getComputers())
            {
                item.getExecutors().forEach(executor -> {
                    if (item.isOnline() && executor.isActive())
                    {
                        executors[0]++;
                        if (executor.isBusy()) {
                            executorsBusy[0]++;
                        }
                    }
                });
            }

            state.setExecutorsBusy(executorsBusy[0]);
            state.setExecutors(executors[0]);

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
