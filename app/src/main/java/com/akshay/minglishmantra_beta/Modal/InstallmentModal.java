package com.akshay.minglishmantra_beta.Modal;

public class InstallmentModal {

    String name,month,offer,savedAmount,amountPerMonth,totalFees;

    public InstallmentModal() {
    }

    public InstallmentModal(String name, String month, String offer, String savedAmount, String amountPerMonth, String totalFees) {
        this.name = name;
        this.month = month;
        this.offer = offer;
        this.savedAmount = savedAmount;
        this.amountPerMonth = amountPerMonth;
        this.totalFees = totalFees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(String savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getAmountPerMonth() {
        return amountPerMonth;
    }

    public void setAmountPerMonth(String amountPerMonth) {
        this.amountPerMonth = amountPerMonth;
    }

    public String getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(String totalFees) {
        this.totalFees = totalFees;
    }
}
