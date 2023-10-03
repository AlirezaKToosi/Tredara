package com.novare.tredara.models;

public enum ENotificationStatus {
    STATUS_PENDING(1)
    ,
    STATUS_SENT(2);
    private final int notificationStatusId;

    ENotificationStatus(int notificationStatusId) {
        this.notificationStatusId = notificationStatusId;
    }

    public int getNotificationStatusId() {
        return notificationStatusId;
    }
}
