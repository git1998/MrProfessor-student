package com.akshay.minglishmantra_beta.Modal;

public class SimpleTestModal {

    String text1;
    String text2;
    String questionUrl;
    String isMcq;



    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getQuestionUrl() {
        return questionUrl;
    }

    public void setQuestionUrl(String questionUrl) {
        this.questionUrl = questionUrl;
    }


    public String getIsMcq() {
        return isMcq;
    }

    public void setIsMcq(String isMcq) {
        this.isMcq = isMcq;
    }

    public SimpleTestModal() {
    }

    public SimpleTestModal(String text1, String text2, String questionUrl, String isMcq) {
        this.text1 = text1;
        this.text2 = text2;
        this.questionUrl = questionUrl;
        this.isMcq =isMcq;
    }


}
