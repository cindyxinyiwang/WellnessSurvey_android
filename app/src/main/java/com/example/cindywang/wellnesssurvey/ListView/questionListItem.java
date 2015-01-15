package com.example.cindywang.wellnesssurvey.ListView;

import java.util.List;

/**
 * Created by cindywang on 11/19/14.
 */
public class questionListItem {
    private String qType;
    private String qPrompt;
    private List<String> qTime;

    private boolean clickable;
    private int timeIndex;  //defaulted as -1; show index of time list qTime if clickable is true

    private String qCategory = "";
    private String qExpire = "";

    public questionListItem(String type, String prompt, List<String> time){
        this.qType = type;
        this.qPrompt = prompt;
        this.qTime = time;
        clickable = false;
        timeIndex = -1;
    }

    public String getqType() {
        return qType;
    }
    public String getqPrompt() {return qPrompt;}
    public int getTimeIndex() {return timeIndex;}
    public List<String> getqTime() {return qTime;}
    public boolean getClickable() {return clickable;}

    //TODO: input validity checking might needed
    public void setClickable(boolean b) {this.clickable = b;}
    public void setTimeIndex(int i) {this.timeIndex = i;}
    public void setqType(String qType) {
        this.qType = qType;
    }
    public void setqCategory(String qType) {
        this.qCategory = qType;
    }
    public void setqExpire(String qExpire) {
        this.qExpire = qExpire;
    }
    public String getqCategory() {
        return qCategory;
    }
    public String getqExpire() {
        return qExpire;
    }
}
