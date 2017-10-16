package com.focusit.notification.listeners;

import com.focusit.notification.phases.BuildPhase;
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
    }

    @Override
    public void onStarted(Run run, TaskListener listener) {
        Executor e = run.getExecutor();
        BuildPhase.QUEUED.handle(run, TaskListener.NULL, e != null ? System.currentTimeMillis() - e.getTimeSpentInQueue() : 0L);
        BuildPhase.STARTED.handle(run, listener, run.getTimeInMillis());
    }

    @Override
    public void onDeleted(Run run) {
        super.onDeleted(run);
    }
}
