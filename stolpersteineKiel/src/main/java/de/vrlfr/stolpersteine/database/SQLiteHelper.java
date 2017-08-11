package de.vrlfr.stolpersteine.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;

public final class SQLiteHelper extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHelper.class.getSimpleName();
	private static final String DB_NAME = "steine.db";
	private static final int DB_VERSION = 18;
	private static SQLiteHelper instance;
	private final Context context;

	public static SQLiteHelper getInstance(Context ctx) {
		if (instance == null) {
			instance = new SQLiteHelper(ctx.getApplicationContext());
		}
		return instance;
	}

	@Override
	public synchronized void close() {
		super.close();
		instance = null;
	}

	private SQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	public SQLiteDatabase openDatabase(int newDatabaseVersion) {
		Log.i(TAG, "openDatabase()");
		File dbFile = context.getDatabasePath(DB_NAME);

		SQLiteDatabase database;
		if (!dbFile.exists()) {
			try {
				Log.d(TAG, "Copying database initially");
				copyDatabase(dbFile);
				database = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS
						| SQLiteDatabase.OPEN_READWRITE);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
				throw new RuntimeException("Error creating source database", e);
			}
		} else {
			Log.d(TAG, "Using existing database");
			database = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS
					| SQLiteDatabase.OPEN_READWRITE);

			if (newDatabaseVersion > 0) {
				database.setVersion(newDatabaseVersion);
			} else {

				int installedDbVersion = database.getVersion();
				String message = MessageFormat.format("DB version check: app version: {0}, installed version: {1}",
						DB_VERSION, installedDbVersion);
				Log.d(TAG, message);

				boolean dbVersionChanged = DB_VERSION > installedDbVersion;
				Log.d(TAG, "dbVersionChanged: " + dbVersionChanged);
				if (dbVersionChanged) {
					database.close();
					try {
						Log.d(TAG, "try copying database");
						copyDatabase(dbFile);
						Log.d(TAG, "done copying database, opening");
						database = SQLiteDatabase.openDatabase(dbFile.getPath(), null,
								SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
						Log.d(TAG, "done opening database");
					} catch (IOException e) {
						Log.e(TAG, e.getMessage(), e);
						throw new RuntimeException("Error creating source database", e);
					}
				}
			}
		}
		return database;
	}

	private void copyDatabase(File dbFile) throws IOException {
		getWritableDatabase();

		InputStream is = null;
		OutputStream os = null;
		try {
			is = context.getAssets().open(DB_NAME);
			os = new FileOutputStream(dbFile);

			byte[] buffer = new byte[1024];
			while (is.read(buffer) > 0) {
				os.write(buffer);
			}
		} finally {
			if (os != null) {
				os.flush();
				os.close();
			}
			if (is != null) {
				is.close();
			}
		}

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
