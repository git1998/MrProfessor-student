package com.akshay.minglishmantra_beta.Modal;

public class GroupModal {

    String name,noOfQue,marks;

    public GroupModal() {
    }

    public GroupModal(String name, String noOfQue, String marks) {
        this.name = name;
        this.noOfQue = noOfQue;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoOfQue() {
        return noOfQue;
    }

    public void setNoOfQue(String noOfQue) {
        this.noOfQue = noOfQue;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
