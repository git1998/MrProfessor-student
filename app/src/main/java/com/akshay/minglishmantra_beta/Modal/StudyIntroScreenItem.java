package com.akshay.minglishmantra_beta.Modal;

public class StudyIntroScreenItem {

    String Title,Description,readMore;
    int ScreenImg;

    public StudyIntroScreenItem(String title, String description, String readMore , int screenImg) {
        Title = title;
        Description = description;
        ScreenImg = screenImg;
        this.readMore =readMore;
    }

    public String getReadMore() {
        return readMore;
    }

    public void setReadMore(String readMore) {
        this.readMore = readMore;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getScreenImg() {
        return ScreenImg;
    }
}
