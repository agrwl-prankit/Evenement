package com.prankit.evenement.Utils;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class AppInfo {

    private String APP_ID = "evenement-msvpz";
    private App app = new App(new AppConfiguration.Builder(APP_ID).build());

    public AppInfo(){}

    public AppInfo(String APP_ID) {
        this.APP_ID = APP_ID;
    }

    public String getAPP_ID() {
        return APP_ID;
    }

    public void setAPP_ID(String APP_ID) {
        this.APP_ID = APP_ID;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }
}
