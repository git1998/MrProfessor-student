package com.akshay.minglishmantra_beta.Modal;

public class LiveLecturesModal {


    String paper,subject,topic,sub_topic,url,date,start_time,end_time,type,topic_no,date_time,isLive,quiz;
    public String postId,course_name;
    long timestamp;
    String sender_name ,sender_image;
    String pdfUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic_no() {
        return topic_no;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public void setTopic_no(String topic_no) {
        this.topic_no = topic_no;
    }

    public String getIsLive() {
        return isLive;
    }

    public void setIsLive(String isLive) {
        this.isLive = isLive;
    }

    public LiveLecturesModal() {
    }

    public LiveLecturesModal(String paper, String subject, String topic, String sub_topic, String url, String date, String start_time, String end_time,String type,String topic_no,String date_time,String isLive,String quiz,long timestamp ,String sender_name,String sender_image,String pdfUrl) {
        this.paper = paper;
        this.subject = subject;
        this.topic = topic;
        this.sub_topic = sub_topic;
        this.url = url;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.type =type;
        this.topic_no =topic_no;
        this.date_time =date_time;
        this.isLive =isLive;
        this.quiz =quiz;

        this.timestamp =timestamp;

        this.sender_name =sender_name;
        this.sender_image =sender_image;

        this.pdfUrl=pdfUrl;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSub_topic() {
        return sub_topic;
    }

    public void setSub_topic(String sub_topic) {
        this.sub_topic = sub_topic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }


    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}
