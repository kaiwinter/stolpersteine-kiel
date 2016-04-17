package de.vrlfr.stolpersteine.fragment.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.StolpersteinActivity;
import de.vrlfr.stolpersteine.database.StolpersteinBo;

public class MapFragment extends Fragment {
	private static final String STOLPERSTEINE_EXTRA = "de.vrlfr.stolpersteine.StolpersteinList";
	private static final LatLng KIEL = new LatLng(54.323396, 10.120184);
	private List<StolpersteinBo> stolpersteine;
	private View view;

	public static MapFragment newInstance(ArrayList<StolpersteinBo> stolpersteine) {
		MapFragment fragment = new MapFragment();

		Bundle arguments = new Bundle();
		arguments.putParcelableArrayList(STOLPERSTEINE_EXTRA, stolpersteine);
		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			stolpersteine = arguments.getParcelableArrayList(STOLPERSTEINE_EXTRA);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_map, container, false);

			SupportMapFragment mapFragment = SupportMapFragment.newInstance();
			FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
			beginTransaction.replace(R.id.map_container, mapFragment);
			beginTransaction.commit();
			mapFragment.getMapAsync(new OnMapReadyCallback() {

				@Override
				public void onMapReady(GoogleMap map) {
					map.setMyLocationEnabled(true);
					map.getUiSettings().setRotateGesturesEnabled(false);
					map.getUiSettings().setTiltGesturesEnabled(false);
					CameraPosition cp = CameraPosition.builder().target(KIEL).zoom(12).build();
					map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
					initMarkers(map);
				}
			});
		}
		return view;
	}

	private void initMarkers(GoogleMap map) {
		Map<String, ArrayList<StolpersteinBo>> adresse2Stolpersteine = sortStolperteineByAdresse(stolpersteine);
		addMapListeners(map, adresse2Stolpersteine);
		new AddMarkerToMapAsync(getActivity(), map).execute(adresse2Stolpersteine);
	}

	private void addMapListeners(final GoogleMap map, final Map<String, ArrayList<StolpersteinBo>> adresse2Stolpersteine) {
		final boolean[] firstTime = { true };
		map.setInfoWindowAdapter(new InfoWindowAdapter() {
			@Override
			public View getInfoContents(Marker marker) {
				Activity activity = getActivity();
				LayoutInflater inflater = LayoutInflater.from(activity);
				View myContentsView = inflater.inflate(R.layout.marker, null);
				TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.marker_title));
				tvTitle.setText(marker.getTitle());
				TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.marker_snippet));
				tvSnippet.setText(marker.getSnippet());

				StolpersteinBo stolperstein = adresse2Stolpersteine.get(marker.getTitle()).iterator().next();
				ImageView imageView = ((ImageView) myContentsView.findViewById(R.id.marker_image));
				if (stolperstein.getImageId() > -1) {

					String uri = "@drawable/id" + stolperstein.getImageId();

					int imageResource = activity.getResources().getIdentifier(uri, null, activity.getPackageName());
					if (imageResource != 0) {
						if (firstTime[0]) {
							// if it's the first time, load the image with the callback set
							firstTime[0] = false;
							Picasso.with(activity).load(imageResource).resize(128, 128).centerCrop()
									.into(imageView, new InfoWindowRefresher(marker));
						} else {

							Picasso.with(activity).load(imageResource).resize(128, 128).centerCrop().into(imageView);
							firstTime[0] = true;
						}
					}
				} else {
					imageView.setVisibility(View.GONE);
				}

				return myContentsView;
			}

			@Override
			public View getInfoWindow(Marker marker) {
				return null;
			}
		});

		OnInfoWindowClickListener onInfoWindowClickListener = new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				LatLng latLng = marker.getPosition();
				ArrayList<StolpersteinBo> arrayList = adresse2Stolpersteine.get(marker.getTitle());
				Intent intent = StolpersteinActivity.newIntent(getActivity(), arrayList, latLng);
				startActivity(intent);
			}
		};
		map.setOnInfoWindowClickListener(onInfoWindowClickListener);

		final Marker[] lastOpened = { null };
		OnMarkerClickListener onMarkerClickListener = new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				// Check if there is an open info window
				if (lastOpened[0] != null) {
					// Is the marker the same marker that was already open
					if (lastOpened.equals(marker)) {
						// Return so that the info window isn't opened again
						return true;
					}

					// Close the info window
					lastOpened[0].hideInfoWindow();
				}

				// Open the info window for the marker
				marker.showInfoWindow();

				// Re-assign the last opened such that we can close it later
				lastOpened[0] = marker;

				float zoom = map.getCameraPosition().zoom;
				LatLng lastPosition = new LatLng(marker.getPosition().latitude + 90 / Math.pow(2, zoom),
						marker.getPosition().longitude);
				CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(lastPosition, zoom);
				map.animateCamera(cu);

				return true;
			}
		};

		map.setOnMarkerClickListener(onMarkerClickListener);
	}

	/**
	 * Gibt eine Liste zurück Adresse -> Liste<Stolperstein>.
	 */
	private Map<String, ArrayList<StolpersteinBo>> sortStolperteineByAdresse(Collection<StolpersteinBo> stolpersteine) {

		Map<String, ArrayList<StolpersteinBo>> adresse2Stolpersteine = new HashMap<>();
		for (StolpersteinBo stolperstein : stolpersteine) {
			String adresse = stolperstein.getAdresse();
			ArrayList<StolpersteinBo> stolpersteineAnAdresse = adresse2Stolpersteine.get(adresse);
			if (stolpersteineAnAdresse == null) {
				stolpersteineAnAdresse = new ArrayList<StolpersteinBo>();
				adresse2Stolpersteine.put(adresse, stolpersteineAnAdresse);
			}
			stolpersteineAnAdresse.add(stolperstein);
		}

		return adresse2Stolpersteine;
	}

	private class InfoWindowRefresher implements Callback {
		private Marker markerToRefresh;

		private InfoWindowRefresher(Marker markerToRefresh) {
			this.markerToRefresh = markerToRefresh;
		}

		@Override
		public void onSuccess() {
			markerToRefresh.showInfoWindow();
		}

		@Override
		public void onError() {
		}
	}
}
