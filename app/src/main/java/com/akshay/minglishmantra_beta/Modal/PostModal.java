package com.akshay.minglishmantra_beta.Modal;

public class PostModal {

    public String type,text,imageUrl,date,subject,questions,duration;
    public String attempts,upvotes,comments,postId,courseName;
    public String sender_name ,sender_image;

    public PostModal() {
    }

    public PostModal(String type, String text, String imageUrl, String date, String subject, String questions, String duration, String attempts, String upvotes, String comments,String postId,String courseName,String sender_name,String sender_image) {
        this.type = type;
        this.text = text;
        this.imageUrl = imageUrl;
        this.date = date;
        this.subject = subject;
        this.questions = questions;
        this.duration = duration;
        this.attempts = attempts;
        this.upvotes = upvotes;
        this.comments = comments;
        this.postId =postId;
        this.courseName =courseName;

        this.sender_name=sender_name;
        this.sender_image =sender_image;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getAttempts() {
        return attempts;
    }

    public void setAttempts(String attempts) {
        this.attempts = attempts;
    }

    public String getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(String upvotes) {
        this.upvotes = upvotes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_image() {
        return sender_image;
    }

    public void setSender_image(String sender_image) {
        this.sender_image = sender_image;
    }
}
