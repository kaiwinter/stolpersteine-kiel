package de.vrlfr.stolpersteine;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import de.vrlfr.stolpersteine.preferences.SharedPrefUtil;
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

        int lastRealmDbUpdateVersion = SharedPrefUtil.getLastRealmDbUpdateVersion(this);
        int versionCode = getVersionCode(this);
        if (lastRealmDbUpdateVersion == versionCode) {
            // nichts tun
        } else {
            Realm.deleteRealm(realmConfiguration); // existiert eventuell nicht, Methode wirft keine Exception in diesem Fall
            SharedPrefUtil.setLastRealmDbUpdateVersion(this, versionCode);
        }

        Realm.setDefaultConfiguration(realmConfiguration);
    }

    /**
     * Gets the version code from the Manifest
     *
     * @param context an instance to get the PackageManager.
     * @return the version code.
     */
    public static int getVersionCode(Context context) {
        int versionCode;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e1) {
            versionCode = -1;
        }
        return versionCode;
    }
}
