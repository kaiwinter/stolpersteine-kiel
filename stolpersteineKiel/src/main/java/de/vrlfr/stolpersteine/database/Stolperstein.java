package de.vrlfr.stolpersteine.database;

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
}
