package com.prankit.evenement.models;

public class ParticipantInfo {

    private String pName, pNumber, pEmail;

    public  ParticipantInfo(){}

    public ParticipantInfo(String pName, String pNumber, String pEmail) {
        this.pName = pName;
        this.pNumber = pNumber;
        this.pEmail = pEmail;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public String getpEmail() {
        return pEmail;
    }

    public void setpEmail(String pEmail) {
        this.pEmail = pEmail;
    }
}
