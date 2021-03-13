package com.prankit.evenement.models;

public class EventInfo {

    private String _id, userId, createrName, eventName, startDate, endDate, fee, email, number;

    public EventInfo(){}

    public EventInfo(String _id, String userId, String createrName, String eventName, String startDate, String endDate, String fee, String email, String number) {
        this._id = _id;
        this.userId = userId;
        this.createrName = createrName;
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fee = fee;
        this.email = email;
        this.number = number;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
