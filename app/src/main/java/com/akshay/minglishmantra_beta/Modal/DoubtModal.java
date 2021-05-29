package com.akshay.minglishmantra_beta.Modal;

public class DoubtModal {


     String course,paper,subject,text1,imageUrl,date;
     String senderName,senderProfileUrl;
     public String postId,courseName;


    public DoubtModal() {
    }

    public DoubtModal(String course, String paper, String subject, String text1, String imageUrl, String date,String senderName,String senderProfileUrl,String postId,String courseName) {
        this.course = course;
        this.paper = paper;
        this.subject = subject;
        this.text1 = text1;
        this.imageUrl = imageUrl;
        this.date = date;

        this.senderName =senderName;
        this.senderProfileUrl =senderProfileUrl;

        this.postId =postId;
        this.courseName =courseName;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
