package com.novare.tredara.models;

public enum EItemStatus {
    STATUS_OPEN(1),
    STATUS_CLOSE(2);
    private final int itemStatusId;

    EItemStatus(int itemStatusId) {
        this.itemStatusId = itemStatusId;
    }

    public int getItemStatusId() {
        return itemStatusId;
    }
}
