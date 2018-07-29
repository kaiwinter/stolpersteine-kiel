package de.vrlfr.stolpersteine.fragment.about;

import android.content.Context;

import de.psdev.licensesdialog.licenses.License;

@SuppressWarnings("serial")
public final class GoogleMapLicense extends License {

	@Override
	public String getName() {
		return "Google Maps Android API v2";
	}

	@Override
	public String readSummaryTextFromResources(Context context) {
		return "";
	}

	@Override
	public String readFullTextFromResources(Context context) {
		return "";
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public String getUrl() {
		return "https://www.google.com";
	}

}
