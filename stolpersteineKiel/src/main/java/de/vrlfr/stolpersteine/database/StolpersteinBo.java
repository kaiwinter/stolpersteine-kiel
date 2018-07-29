package de.vrlfr.stolpersteine.database;

import android.os.Parcel;
import android.os.Parcelable;

public final class StolpersteinBo implements Parcelable {
	public static final String TABLE_NAME = "stolperstein";

	public static final String ADRESSE_COLUMN = "adresse";
	public static final String VERLEGEDATUM = "verlegedatum";
	public static final String NAME_COLUMN = "name";
	public static final String GEBOREN_COLUMN = "geboren";
	public static final String TOD_COLUMN = "tod";
	public static final String LONGITUDE_COLUMN = "longitude";
	public static final String LATITUDE_COLUMN = "latitude";
	public static final String IMAGE_ID_COLUMN = "image_id";
	public static final String BIO_ID_COLUMN = "bio_id";

	public final String adresse;
	public final String verlegedatum;
	public final String name;
	public final String geboren;
	public final String tod;
	public final double longitude;
	public final double latitude;
	public final int imageId;
	public final int bioId;

	public StolpersteinBo(Parcel orig) {
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

	public StolpersteinBo(String adresse, String verlegedatum, String name, String geboren, String tod,
			double longitude, double latitude, int imageId, int bioId) {
		this.adresse = adresse;
		this.verlegedatum = verlegedatum;
		this.name = name;
		this.geboren = geboren;
		this.tod = tod;
		this.longitude = longitude;
		this.latitude = latitude;
		this.imageId = imageId;
		this.bioId = bioId;
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

	public static final Parcelable.Creator<StolpersteinBo> CREATOR = new Parcelable.Creator<StolpersteinBo>() {

		@Override
		public StolpersteinBo createFromParcel(Parcel source) {
			return new StolpersteinBo(source);
		}

		@Override
		public StolpersteinBo[] newArray(int size) {
			return new StolpersteinBo[size];
		}
	};

	@Override
	public String toString() {
		return name;
	}
}
