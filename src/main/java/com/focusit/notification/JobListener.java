package com.focusit.notification;

import hudson.Extension;
import hudson.model.Executor;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import javax.annotation.Nonnull;

/**
 * Created by doki on 11.10.17.
 */
@Extension
public class JobListener extends RunListener {
    @Override
    public void onCompleted(Run run, @Nonnull TaskListener listener) {
        Phase.COMPLETED.handle(run, listener, run.getTimeInMillis() + run.getDuration());
    }

    @Override
    public void onFinalized(Run run) {
        Phase.FINALIZED.handle(run, TaskListener.NULL, System.currentTimeMillis());
    }

    @Override
    public void onStarted(Run run, TaskListener listener) {
        Executor e = run.getExecutor();
        Phase.QUEUED.handle(run, TaskListener.NULL, e != null ? System.currentTimeMillis() - e.getTimeSpentInQueue() : 0L);
        Phase.STARTED.handle(run, listener, run.getTimeInMillis());
    }

    @Override
    public void onDeleted(Run run) {
        super.onDeleted(run);
    }
}
