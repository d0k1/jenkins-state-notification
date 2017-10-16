package com.focusit.notification.listeners;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import javax.annotation.Nonnull;

/**
 * Created by doki on 14.10.17.
 */
@Extension
public class ExecutorsListener extends RunListener<Run> {
    @Override
    public void onCompleted(Run run, @Nonnull TaskListener listener) {
        super.onCompleted(run, listener);
    }

    @Override
    public void onFinalized(Run run) {
        super.onFinalized(run);
    }

    @Override
    public void onStarted(Run run, TaskListener listener) {
        super.onStarted(run, listener);
    }

    @Override
    public void onDeleted(Run run) {
        super.onDeleted(run);
    }
}
