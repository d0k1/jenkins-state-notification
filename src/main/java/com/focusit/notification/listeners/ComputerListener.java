package com.focusit.notification.listeners;

import com.focusit.notification.phases.ExecutorPhase;
import hudson.Extension;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.slaves.OfflineCause;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;

@Extension
public class ComputerListener extends hudson.slaves.ComputerListener {
    @Override
    public void onOnline(Computer c, TaskListener listener) throws IOException, InterruptedException {
        ExecutorPhase.ONLINE.handle(System.currentTimeMillis(), null);
    }

    @Override
    public void onOffline(@Nonnull Computer c, @CheckForNull OfflineCause cause) {
        ExecutorPhase.OFFLINE.handle(System.currentTimeMillis(), null);
    }

    @Override
    public void onTemporarilyOnline(Computer c) {
        ExecutorPhase.TEMP_ONLINE.handle(System.currentTimeMillis(), null);
    }

    @Override
    public void onTemporarilyOffline(Computer c, OfflineCause cause) {
        ExecutorPhase.TEMP_OFFLINE.handle(System.currentTimeMillis(), null);
    }

    @Override
    public void onConfigurationChange() {
        ExecutorPhase.CONFIG_CHANGES.handle(System.currentTimeMillis(), null);
    }
}
