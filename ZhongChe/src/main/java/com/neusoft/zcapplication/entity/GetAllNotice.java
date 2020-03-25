package com.neusoft.zcapplication.entity;

import java.io.Serializable;

public class GetAllNotice implements Serializable {
    private String noticeTitle;
    private long noticeDate;

    public long getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(long noticeDate) {
        this.noticeDate = noticeDate;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }
}
