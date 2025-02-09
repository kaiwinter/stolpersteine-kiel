package de.vrlfr.stolpersteine.activity.main.fragment.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.stolperstein.StolpersteinActivity;
import de.vrlfr.stolpersteine.database.Stolperstein;

public class MapFragment extends Fragment {
    private static final String STOLPERSTEINE_EXTRA = "de.vrlfr.stolpersteine.StolpersteinList";
    private static final LatLng KIEL = new LatLng(54.323396, 10.120184);
    private List<Stolperstein> stolpersteine;
    private View view;
    private GoogleMap map;

    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (Boolean.TRUE.equals(isGranted.get(Manifest.permission.ACCESS_FINE_LOCATION))
                        && Boolean.TRUE.equals(isGranted.get(Manifest.permission.ACCESS_COARSE_LOCATION))) {
                    map.setMyLocationEnabled(true);
                }
            });

    public static MapFragment newInstance(ArrayList<Stolperstein> stolpersteine) {
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
            FragmentTransaction beginTransaction = getParentFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.map_container, mapFragment);
            beginTransaction.commit();
            mapFragment.getMapAsync(map -> {
                MapFragment.this.map = map;
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION });
                } else {
                    map.setMyLocationEnabled(true);
                }
                map.getUiSettings().setRotateGesturesEnabled(false);
                map.getUiSettings().setTiltGesturesEnabled(false);
                map.getUiSettings().setZoomControlsEnabled(true);
                CameraPosition cp = CameraPosition.builder().target(KIEL).zoom(12).build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
                initMarkers(map);
            });
        }
        return view;
    }

    private void initMarkers(GoogleMap map) {
        Map<String, ArrayList<Stolperstein>> adresse2Stolpersteine = sortStolpersteineByAdresse(stolpersteine);
        addMapListeners(map, adresse2Stolpersteine);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            for (Map.Entry<String, ArrayList<Stolperstein>> adresse2Stolperstein : adresse2Stolpersteine.entrySet()) {
                StringBuilder namen = new StringBuilder();
                String adresse = null;
                LatLng latLon = null;
                for (Stolperstein stolperstein : adresse2Stolperstein.getValue()) {
                    if (adresse == null) {
                        adresse = stolperstein.adresse;
                        latLon = new LatLng(stolperstein.latitude, stolperstein.longitude);
                    }
                    if (namen.length() > 0) {
                        namen.append("\n");
                    }
                    namen.append(stolperstein.name);
                }

                MarkerOptions marker = new MarkerOptions()
                        .position(latLon)
                        .title(adresse)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.stolperstein))
                        .snippet(namen.toString());
                handler.post(() -> map.addMarker(marker));
            }
        });
    }

    private void addMapListeners(final GoogleMap map, final Map<String, ArrayList<Stolperstein>> adresse2Stolpersteine) {
        map.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoContents(Marker marker) {
                Activity activity = getActivity();
                LayoutInflater inflater = LayoutInflater.from(activity);
                View myContentsView = inflater.inflate(R.layout.marker, null);
                TextView tvTitle = myContentsView.findViewById(R.id.marker_title);
                tvTitle.setText(marker.getTitle());
                TextView tvSnippet = myContentsView.findViewById(R.id.marker_snippet);
                tvSnippet.setText(marker.getSnippet());

                Stolperstein stolperstein = adresse2Stolpersteine.get(marker.getTitle()).iterator().next();
                ImageView imageView = myContentsView.findViewById(R.id.marker_image);
                if (stolperstein.imageId > -1) {
                    int imageResource = stolperstein.getResourceIdForImageId(activity);
                    if (imageResource != 0) {
                        imageView.setImageResource(imageResource);
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

        OnInfoWindowClickListener onInfoWindowClickListener = marker -> {
            LatLng latLng = marker.getPosition();
            ArrayList<Stolperstein> arrayList = adresse2Stolpersteine.get(marker.getTitle());
            Intent intent = StolpersteinActivity.newIntent(getActivity(), arrayList, latLng);
            startActivity(intent);
        };
        map.setOnInfoWindowClickListener(onInfoWindowClickListener);

        final Marker[] lastOpened = {null};
        OnMarkerClickListener onMarkerClickListener = marker -> {
            // Check if there is an open info window
            if (lastOpened[0] != null) {
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
        };

        map.setOnMarkerClickListener(onMarkerClickListener);
    }

    /**
     * Gibt eine Liste zurück Adresse -> Liste<Stolperstein>.
     */
    private Map<String, ArrayList<Stolperstein>> sortStolpersteineByAdresse(Collection<Stolperstein> stolpersteine) {

        Map<String, ArrayList<Stolperstein>> adresse2Stolpersteine = new HashMap<>();
        for (Stolperstein stolperstein : stolpersteine) {
            String adresse = stolperstein.adresse;
            ArrayList<Stolperstein> stolpersteineAnAdresse = adresse2Stolpersteine.get(adresse);
            if (stolpersteineAnAdresse == null) {
                stolpersteineAnAdresse = new ArrayList<>();
                adresse2Stolpersteine.put(adresse, stolpersteineAnAdresse);
            }
            stolpersteineAnAdresse.add(stolperstein);
        }

        return adresse2Stolpersteine;
    }
}
