package com.focusit.notification;

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
        super.onEnterWaiting(wi);
    }

    @Override
    public void onLeaveWaiting(Queue.WaitingItem wi) {
        super.onLeaveWaiting(wi);
    }

    @Override
    public void onEnterBlocked(Queue.BlockedItem bi) {
        super.onEnterBlocked(bi);
    }

    @Override
    public void onLeaveBlocked(Queue.BlockedItem bi) {
        super.onLeaveBlocked(bi);
    }

    @Override
    public void onEnterBuildable(Queue.BuildableItem bi) {
        super.onEnterBuildable(bi);
    }

    @Override
    public void onLeaveBuildable(Queue.BuildableItem bi) {
        super.onLeaveBuildable(bi);
    }

    @Override
    public void onLeft(Queue.LeftItem li) {
        super.onLeft(li);
    }
}
