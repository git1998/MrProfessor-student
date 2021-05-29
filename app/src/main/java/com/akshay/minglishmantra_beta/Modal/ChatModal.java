package com.akshay.minglishmantra_beta.Modal;

public class ChatModal {

    String doubtText,sender,time,profileUrl;

    public ChatModal() {
    }

    public ChatModal(String doubtText, String sender, String time,String profileUrl) {
        this.doubtText = doubtText;
        this.sender = sender;
        this.time = time;
        this.profileUrl =profileUrl;
    }

    public String getDoubtText() {
        return doubtText;
    }

    public void setDoubtText(String doubtText) {
        this.doubtText = doubtText;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
