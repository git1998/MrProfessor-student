package com.akshay.minglishmantra_beta.Modal;

public class TestListModal {

    String testNanme,topics,paper,subject,questions,marks,end_time,start_time,available_date;
    String postId,courseName;
    String testType;
    long timestamp;

    public TestListModal() {
    }

    public TestListModal(String testNanme, String topics, String paper, String subject, String questions, String marks, String end_time, String start_time, String available_date,String postId,String courseName,long timestamp,String testType) {
        this.testNanme = testNanme;
        this.topics = topics;
        this.paper = paper;
        this.subject = subject;
        this.questions = questions;
        this.marks = marks;
        this.end_time = end_time;
        this.start_time = start_time;
        this.available_date = available_date;

        this.postId =postId;
        this.courseName =courseName;

        this.timestamp =timestamp;

        this.testType=testType;
    }


    public String getTestNanme() {
        return testNanme;
    }

    public void setTestNanme(String testNanme) {
        this.testNanme = testNanme;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
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

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getAvailable_date() {
        return available_date;
    }

    public void setAvailable_date(String available_date) {
        this.available_date = available_date;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }
}
