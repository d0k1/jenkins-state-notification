package com.focusit.notification.listeners;

import com.focusit.notification.phases.ItemPhase;
import hudson.Extension;
import hudson.model.Item;

@Extension
public class ItemListener extends hudson.model.listeners.ItemListener {

    @Override
    public void onLocationChanged(Item item, String oldFullName, String newFullName) {
        ItemPhase.LOCATION_CHANGED.handle(System.currentTimeMillis(), item, oldFullName);
    }

    @Override
    public void onCreated(Item item) {
        ItemPhase.CREATED.handle(System.currentTimeMillis(), item, null);
    }

    @Override
    public void onCopied(Item src, Item item) {
        ItemPhase.COPIED.handle(System.currentTimeMillis(), item, null);
    }

    @Override
    public void onDeleted(Item item) {
        ItemPhase.DELETED.handle(System.currentTimeMillis(), item, null);
    }

    @Override
    public void onRenamed(Item item, String oldName, String newName) {
        ItemPhase.RENAMED.handle(System.currentTimeMillis(), item, oldName);
    }

    @Override
    public void onUpdated(Item item) {
        ItemPhase.UPDATED.handle(System.currentTimeMillis(), item, null);
    }
}
