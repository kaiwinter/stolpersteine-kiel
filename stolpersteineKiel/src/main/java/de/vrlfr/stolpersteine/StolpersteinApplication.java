package de.vrlfr.stolpersteine;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class StolpersteinApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .assetFile("stolperstein.realm")
                .readOnly()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
