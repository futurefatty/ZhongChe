package com.neusoft.zcapplication.entity;

public class GetNotice {


    private String NOTICE_TITLE;
    private String FILENAME;
    private String PUBLISH_TIME;
    private int FILE_ID;
    private String SAVEPATH;
    private int ID;
    private String URL;

    public String getNOTICE_TITLE() {
        return NOTICE_TITLE;
    }

    public void setNOTICE_TITLE(String NOTICE_TITLE) {
        this.NOTICE_TITLE = NOTICE_TITLE;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getPUBLISH_TIME() {
        return PUBLISH_TIME;
    }

    public void setPUBLISH_TIME(String PUBLISH_TIME) {
        this.PUBLISH_TIME = PUBLISH_TIME;
    }

    public int getFILE_ID() {
        return FILE_ID;
    }

    public void setFILE_ID(int FILE_ID) {
        this.FILE_ID = FILE_ID;
    }

    public String getSAVEPATH() {
        return SAVEPATH;
    }

    public void setSAVEPATH(String SAVEPATH) {
        this.SAVEPATH = SAVEPATH;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
