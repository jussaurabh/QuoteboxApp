package com.example.quotebox.models;

public class Notifications {

    // types of notifications
    public static final String FOLLOWING_TYPE_NOTIFY = "following";
    public static final String POST_UPDATE_TYPE_NOTIFY = "postUpdate";
    public static final String REPORT_TYPE_NOTIFY = "report";

    public static final String NOTIFY_TYPE = "notifyType";
    public static final String NOTIFY_ID = "notifyId";
    public static final String NOTIFY_Date = "notifyDate";

    private String notifyType;
    private String notifyId;
    private String userId;
    private String notifyDate;

    public Notifications() {}

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String _getNotifyId() {
        return notifyId;
    }

    public void _setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(String notifyDate) {
        this.notifyDate = notifyDate;
    }
}
