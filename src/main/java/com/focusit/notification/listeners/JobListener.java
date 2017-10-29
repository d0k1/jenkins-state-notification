package com.focusit.notification.listeners;

import javax.annotation.Nonnull;

import com.focusit.notification.phases.BuildPhase;
import com.focusit.notification.phases.QueuePhase;

import hudson.Extension;
import hudson.model.Executor;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

/**
 * Created by doki on 11.10.17.
 */
@Extension
public class JobListener extends RunListener<Run> {

    public JobListener() {
        super(Run.class);
    }

    @Override
    public void onCompleted(Run run, @Nonnull TaskListener listener) {
        BuildPhase.COMPLETED.handle(run, listener, run.getTimeInMillis() + run.getDuration());
    }

    @Override
    public void onFinalized(Run run) {
        BuildPhase.FINALIZED.handle(run, TaskListener.NULL, System.currentTimeMillis());

        // Check queue length
        QueuePhase.CHECK_LENGTH.handle(null);
    }

    @Override
    public void onStarted(Run run, TaskListener listener) {
        Executor e = run.getExecutor();
        BuildPhase.QUEUED.handle(run, TaskListener.NULL, e != null ? System.currentTimeMillis() - e.getTimeSpentInQueue() : 0L);
        BuildPhase.STARTED.handle(run, listener, run.getTimeInMillis());

        // Check queue length
        QueuePhase.CHECK_LENGTH.handle(null);
    }

    @Override
    public void onDeleted(Run run) {
        super.onDeleted(run);
    }
}
