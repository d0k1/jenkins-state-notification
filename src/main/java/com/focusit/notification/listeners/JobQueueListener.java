package com.focusit.notification.listeners;

import com.focusit.notification.phases.ExecutorPhase;
import com.focusit.notification.phases.QueuePhase;

import hudson.Extension;
import hudson.model.Queue;
import hudson.model.queue.QueueListener;

/**
 * Created by doki on 11.10.17.
 */
@Extension
public class JobQueueListener extends QueueListener {
    @Override
    public void onEnterWaiting(Queue.WaitingItem wi) {
        QueuePhase.WAITING_IN.handle(wi);

        // Check executors usage
        ExecutorPhase.CHECK_USAGE.handle(System.currentTimeMillis(), null, null);
    }

    @Override
    public void onLeaveWaiting(Queue.WaitingItem wi) {
        QueuePhase.WAITING_OUT.handle(wi);
    }

    @Override
    public void onEnterBlocked(Queue.BlockedItem bi) {
        QueuePhase.BLOCK_IN.handle(bi);
    }

    @Override
    public void onLeaveBlocked(Queue.BlockedItem bi) {
        QueuePhase.BLOCK_OUT.handle(bi);
    }

    @Override
    public void onEnterBuildable(Queue.BuildableItem bi) {
        QueuePhase.BUILD_IN.handle(bi);
    }

    @Override
    public void onLeaveBuildable(Queue.BuildableItem bi) {
        QueuePhase.BUILD_OUT.handle(bi);
    }

    @Override
    public void onLeft(Queue.LeftItem li) {
        QueuePhase.LEAVE.handle(li);

        // Check executors usage
        ExecutorPhase.CHECK_USAGE.handle(System.currentTimeMillis(), null, null);
    }
}
