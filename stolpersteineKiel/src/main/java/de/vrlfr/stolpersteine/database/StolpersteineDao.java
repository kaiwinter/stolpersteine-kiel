package de.vrlfr.stolpersteine.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public final class StolpersteineDao {

	private static final String[] STOLPERSTEINE_COLUMNS = { StolpersteinBo.ADRESSE_COLUMN, StolpersteinBo.VERLEGEDATUM,
			StolpersteinBo.NAME_COLUMN, StolpersteinBo.GEBOREN_COLUMN, StolpersteinBo.TOD_COLUMN,
			StolpersteinBo.LONGITUDE_COLUMN, StolpersteinBo.LATITUDE_COLUMN, StolpersteinBo.IMAGE_ID_COLUMN,
			StolpersteinBo.BIO_ID_COLUMN };

	public static ArrayList<StolpersteinBo> getStolpersteine(Context context) {
		SQLiteHelper dbHelper = null;
		SQLiteDatabase database = null;
		try {
			dbHelper = SQLiteHelper.getInstance(context);
			database = dbHelper.openDatabase(-1);

			return getStolpersteineInternal(database);
		} finally {
			if (dbHelper != null) {
				dbHelper.close();
			}
			if (database != null) {
				database.close();
			}
		}

	}

	private static ArrayList<StolpersteinBo> getStolpersteineInternal(SQLiteDatabase database) {
		ArrayList<StolpersteinBo> stolperteine = new ArrayList<StolpersteinBo>();

		Cursor cursor = null;
		try {
			cursor = database.query(StolpersteinBo.TABLE_NAME, STOLPERSTEINE_COLUMNS, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				stolperteine.add(new StolpersteinBo(cursor.getString(0), // adresse
						cursor.getString(1), // verlegedatum
						cursor.getString(2), // name
						cursor.getString(3), // geboren
						cursor.getString(4), // tod
						cursor.getDouble(5), // longitude
						cursor.getDouble(6), // latitude
						cursor.getInt(7), // image ID
						cursor.getInt(8)) // bio id
						);
				cursor.moveToNext();
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return stolperteine;
	}
}
