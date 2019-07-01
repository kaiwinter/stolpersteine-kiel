package de.vrlfr.stolpersteine.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {

    private static final String SETTINGS_PREF_FILE = "stngs";
    private static final  String LAST_REALM_DB_UPDATE_VERSION = "lrduv";

    /**
     * @return Die App-Version, in der zuletzt die Realm-Datenbank upgedatet wurde, -1, wenn kein vorheriges Update statt fand.
     */
    public static int getLastRealmDbUpdateVersion(Context context) {
        return getNullsafe(context, SETTINGS_PREF_FILE, LAST_REALM_DB_UPDATE_VERSION, -1);
    }

    public static void setLastRealmDbUpdateVersion(Context context, int level) {
        setNullsafe(context, SETTINGS_PREF_FILE, LAST_REALM_DB_UPDATE_VERSION, level);
    }

    private static int getNullsafe(Context context, String preferencesFile, String key, int defaultValue) {
        if (context == null || context.getApplicationContext() == null) {
            return defaultValue;
        }

        return context.getApplicationContext().getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
                .getInt(key, defaultValue);
    }

    private static void setNullsafe(Context context, String preferencesFile, String key, int value) {
        if (context == null || context.getApplicationContext() == null) {
            return;
        }

        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(preferencesFile,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
