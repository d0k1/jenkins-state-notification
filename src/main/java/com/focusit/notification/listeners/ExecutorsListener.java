package com.focusit.notification.listeners;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import com.focusit.notification.phases.ExecutorPhase;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import hudson.triggers.SafeTimerTask;
import jenkins.util.Timer;

/**
 * Created by doki on 14.10.17.
 */
@Extension
public class ExecutorsListener extends RunListener<Run> {

    @Override
    public void onFinalized(Run run)
    {
        ExecutorPhase.RELEASED.handle(System.currentTimeMillis(), run, null);

        // schedule check usage
        Timer.get().schedule(new CheckUsageTimerTask(), 30, TimeUnit.SECONDS);
    }

    @Override
    public void onCompleted(Run run, @Nonnull TaskListener listener) {
        super.onCompleted(run, listener);
    }

    @Override
    public void onStarted(Run run, TaskListener listener) {
        ExecutorPhase.ACQUIRED.handle(System.currentTimeMillis(), run, null);
    }

    @Override
    public void onDeleted(Run run) {
        ExecutorPhase.CHECK.handle(System.currentTimeMillis(), null, null);
    }

    // Task to check executors usage after executor has finished a task
    private static class CheckUsageTimerTask extends SafeTimerTask
    {
        @Override
        protected void doRun() throws Exception
        {
            ExecutorPhase.CHECK.handle(System.currentTimeMillis(), null, null);
        }
    }
}
