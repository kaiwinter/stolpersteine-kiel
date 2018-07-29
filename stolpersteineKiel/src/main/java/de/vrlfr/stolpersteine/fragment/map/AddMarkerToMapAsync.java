package de.vrlfr.stolpersteine.fragment.map;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.database.StolpersteinBo;

public class AddMarkerToMapAsync extends
		AsyncTask<Map<String, ArrayList<StolpersteinBo>>, ArrayList<StolpersteinBo>, Boolean> {
	private final WeakReference<Activity> context;
	private final GoogleMap map;

	public AddMarkerToMapAsync(Activity activity, GoogleMap map) {
		this.context = new WeakReference<>(activity);
		this.map = map;
	}

	@Override
	protected Boolean doInBackground(Map<String, ArrayList<StolpersteinBo>>... adresse2Stolpersteine) {
		// Fügt alle Stolpersteine zur GoogleMap hinzu.
		for (Entry<String, ArrayList<StolpersteinBo>> adresse2Stolperstein : adresse2Stolpersteine[0].entrySet()) {
			publishProgress(adresse2Stolperstein.getValue());
		}
		return true;
	}

	@Override
	protected void onProgressUpdate(ArrayList<StolpersteinBo>... stolpersteine) {
		String namen = "";
		String adresse = null;
		LatLng latLon = null;
		for (StolpersteinBo stolperstein : stolpersteine[0]) {
			if (adresse == null) {
				adresse = stolperstein.adresse;
				latLon = new LatLng(stolperstein.latitude, stolperstein.longitude);
			}
			if (!namen.isEmpty()) {
				namen += "\n";
			}
			namen += stolperstein.name;
		}

		final MarkerOptions marker = new MarkerOptions() //
				.position(latLon) //
				.title(adresse) //
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.stolperstein)) //
				.snippet(namen);

		Activity activity = context.get();
		if (activity != null) {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// Muss im UI Thread ausgeführt werden
					map.addMarker(marker);
				}
			});
		}
	}
}