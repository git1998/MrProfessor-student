package com.akshay.minglishmantra_beta.Modal;

public class BrowseCourseModal {

    String is_freetrial,freetrial_enddate,buycourse_enddate;

    public BrowseCourseModal() {
    }

    public BrowseCourseModal(String is_freetrial, String freetrial_enddate, String buycourse_enddate) {
        this.is_freetrial = is_freetrial;
        this.freetrial_enddate = freetrial_enddate;
        this.buycourse_enddate = buycourse_enddate;
    }

    public String getIs_freetrial() {
        return is_freetrial;
    }

    public void setIs_freetrial(String is_freetrial) {
        this.is_freetrial = is_freetrial;
    }

    public String getFreetrial_enddate() {
        return freetrial_enddate;
    }

    public void setFreetrial_enddate(String freetrial_enddate) {
        this.freetrial_enddate = freetrial_enddate;
    }

    public String getBuycourse_enddate() {
        return buycourse_enddate;
    }

    public void setBuycourse_enddate(String buycourse_enddate) {
        this.buycourse_enddate = buycourse_enddate;
    }
}
