package com.akshay.minglishmantra_beta.Modal;

public class PrepareModal {


    String buycourse_enddate,freetrial_enddate,is_freetrial;

    public PrepareModal() {
    }


    public PrepareModal(String buycourse_enddate, String freetrial_enddate, String is_freetrial) {
        this.buycourse_enddate = buycourse_enddate;
        this.freetrial_enddate = freetrial_enddate;
        this.is_freetrial = is_freetrial;
    }

    public String getBuycourse_enddate() {
        return buycourse_enddate;
    }

    public void setBuycourse_enddate(String buycourse_enddate) {
        this.buycourse_enddate = buycourse_enddate;
    }

    public String getFreetrial_enddate() {
        return freetrial_enddate;
    }

    public void setFreetrial_enddate(String freetrial_enddate) {
        this.freetrial_enddate = freetrial_enddate;
    }

    public String getIs_freetrial() {
        return is_freetrial;
    }

    public void setIs_freetrial(String is_freetrial) {
        this.is_freetrial = is_freetrial;
    }
}
