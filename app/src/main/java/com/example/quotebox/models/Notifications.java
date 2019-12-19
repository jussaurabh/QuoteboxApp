package com.example.quotebox.models;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notifications {

    // types of notifications
    public static final String FOLLOWING_TYPE_NOTIFY = "following";
    public static final String POST_UPDATE_TYPE_NOTIFY = "postUpdate";
    public static final String REPORT_TYPE_NOTIFY = "report";

    public static final String NOTIFY_TYPE = "notifyType";
    public static final String NOTIFY_ID = "notifyId";
    public static final String NOTIFIER_ID = "notifierId";
    public static final String NOTIFY_Date = "notifyDate";
    public static final String NOTIFY_CONTENT = "notifyContent";

    private String notifyType;
    private Timestamp notifyId;
    private String userId;
    private String notifierId;
    private Timestamp notifyDate;
    private String notifyContent;

    public Notifications() {}

    public Notifications(
            String uid,
            Timestamp notifyDate,
            String notifyContent,
            String notifyType,
            String notifierId
    ) {
        this.userId = uid;
        this.notifyDate = notifyDate;
        this.notifyContent = notifyContent;
        this.notifyType = notifyType;
        this.notifierId = notifierId;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public Timestamp _getNotifyId() {
        return notifyId;
    }

    public void _setNotifyId(Timestamp notifyId) {
        this.notifyId = notifyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Timestamp notifyDate) {
        this.notifyDate = notifyDate;
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public String _getNotifyDate() {
        Date d = new Date(getNotifyDate().getSeconds() * 1000);
        DateFormat dateFormat = new SimpleDateFormat("MMM d");
        return dateFormat.format(d);
    }

    public String _getNotifyTime() {
        Date d = new Date(getNotifyDate().getSeconds() * 1000);
        DateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(d);
    }

    public String getNotifierId() {
        return notifierId;
    }

    public void setNotifierId(String notifierId) {
        this.notifierId = notifierId;
    }
}
