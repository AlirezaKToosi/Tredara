package com.novare.tredara.models;

public enum EActionType {
    SUCCESSFUL_LOGIN(1),
    FAILED_LOGIN(2),
    CREATE_ITEM(3),
    CREATE_BID(4),;
    private final int actionTypeId;

    EActionType(int actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    public int getActionTypeId() {
        return actionTypeId;
    }
}
