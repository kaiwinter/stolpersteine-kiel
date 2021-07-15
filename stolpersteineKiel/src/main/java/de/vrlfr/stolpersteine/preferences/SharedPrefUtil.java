package de.vrlfr.stolpersteine.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {

    private static final String SETTINGS_PREF_FILE = "stngs";
    private static final String LAST_REALM_DB_UPDATE_VERSION = "lrduv";

    /**
     * @return Die App-Version, in der zuletzt die Realm-Datenbank upgedatet wurde, -1, wenn kein vorheriges Update statt fand.
     */
    public static int getLastRealmDbUpdateVersion(Context context) {
        return getNullsafe(context);
    }

    public static void setLastRealmDbUpdateVersion(Context context, int level) {
        setNullsafe(context, level);
    }

    private static int getNullsafe(Context context) {
        if (context == null || context.getApplicationContext() == null) {
            return -1;
        }

        return context
                .getApplicationContext()
                .getSharedPreferences(SharedPrefUtil.SETTINGS_PREF_FILE, Context.MODE_PRIVATE)
                .getInt(SharedPrefUtil.LAST_REALM_DB_UPDATE_VERSION, -1);
    }

    private static void setNullsafe(Context context, int value) {
        if (context == null || context.getApplicationContext() == null) {
            return;
        }

        SharedPreferences preferences = context
                .getApplicationContext()
                .getSharedPreferences(SharedPrefUtil.SETTINGS_PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SharedPrefUtil.LAST_REALM_DB_UPDATE_VERSION, value);
        editor.apply();
    }
}
