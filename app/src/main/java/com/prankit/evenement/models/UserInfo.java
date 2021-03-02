package com.prankit.evenement.models;

import org.bson.Document;
import org.bson.types.ObjectId;

import io.realm.annotations.PrimaryKey;

public class UserInfo extends Document {

    private ObjectId objectId;
    @PrimaryKey
    private String userId;
    private String name;
    private String number;
    private String email;
    private String image;

    public UserInfo(){}

    public UserInfo(ObjectId objectId, String userId, String name, String number, String email) {
        this.objectId = objectId;
        this.userId = userId;
        this.name = name;
        this.number = number;
        this.email = email;
    }
    public UserInfo(ObjectId objectId, String userId, String name, String number, String email, String image) {
        this.objectId = objectId;
        this.userId = userId;
        this.name = name;
        this.number = number;
        this.email = email;
        this.image = image;
    }


    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
