package de.vrlfr.stolpersteine.database;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class Stolperstein extends RealmObject implements Parcelable {

	public String adresse;
	public String verlegedatum;
	public String name;
	public String geboren;
	public String tod;
	public Double longitude;
	public Double latitude;
	public Integer imageId;
	public Integer bioId;

	public Stolperstein() {

	}

	public Stolperstein(Parcel orig) {
		adresse = orig.readString();
		verlegedatum = orig.readString();
		name = orig.readString();
		geboren = orig.readString();
		tod = orig.readString();
		longitude = orig.readDouble();
		latitude = orig.readDouble();
		imageId = orig.readInt();
		bioId = orig.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(adresse);
		dest.writeString(verlegedatum);
		dest.writeString(name);
		dest.writeString(geboren);
		dest.writeString(tod);
		dest.writeDouble(longitude);
		dest.writeDouble(latitude);
		dest.writeInt(imageId);
		dest.writeInt(bioId);
	}

	public static final Parcelable.Creator<Stolperstein> CREATOR = new Parcelable.Creator<>() {

		@Override
		public Stolperstein createFromParcel(Parcel source) {
			return new Stolperstein(source);
		}

		@Override
		public Stolperstein[] newArray(int size) {
			return new Stolperstein[size];
		}
	};

	@Override
	public String toString() {
		return name;
	}

	public static String getImageAssetName(int bioId) {
		return "id" + bioId + ".pdf";
	}

	public static String getBioTxtAssetName(int bioId) {
		return "id" + bioId + ".md";
	}

	/**
	 * Gibt die Resource ID (in @drawable) für eine Image ID eines Stolpersteins zurück.
	 * @param context Context für den Zugriff auf die Resources
	 * @return Resource ID des Fotos
	 */
	public int getResourceIdForImageId(Context context) {
		String uri = "@drawable/id" + imageId;
		return context.getResources().getIdentifier(uri, null, context.getPackageName());
	}
}
