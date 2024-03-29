package de.vrlfr.stolpersteine.activity.stolperstein;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.BaseActivity;
import de.vrlfr.stolpersteine.database.Stolperstein;

public class StolpersteinActivity extends BaseActivity {

    private static final String STOLPERSTEINE_EXTRA = "stolpersteine";
    private static final String STOLPERSTEINE_LATLNG_EXTRA = "stolperstein_latlng";

    public static Intent newIntent(Context context, ArrayList<Stolperstein> arrayList, LatLng latLng) {
        Intent intent = new Intent(context, StolpersteinActivity.class);

        // extras
        intent.putExtra(STOLPERSTEINE_EXTRA, arrayList);
        intent.putExtra(STOLPERSTEINE_LATLNG_EXTRA, latLng);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stolperstein);

        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
    }

    private void handleExtras(final Bundle extras) {
        List<Stolperstein> parcelableArrayList = extras.getParcelableArrayList(STOLPERSTEINE_EXTRA);
        Map<ImageBioId, Collection<Stolperstein>> imageBio2Stolperstein = new HashMap<>();
        for (Stolperstein stolperstein : parcelableArrayList) {
            int imageId = stolperstein.imageId;
            int bioId = stolperstein.bioId;
            ImageBioId imageBioId = new ImageBioId(imageId, bioId);

            Collection<Stolperstein> stolpersteine = imageBio2Stolperstein.get(imageBioId);
            if (stolpersteine == null) {
                stolpersteine = new ArrayList<>();
                imageBio2Stolperstein.put(imageBioId, stolpersteine);
            }
            stolpersteine.add(stolperstein);
        }

        Stolperstein stolperstein = parcelableArrayList.iterator().next();
        String adresse = stolperstein.adresse;
        getSupportActionBar().setTitle(adresse);

        LinearLayout stolpersteinListView = findViewById(R.id.listViewFix);
        NamesRowItemAdapter adapter = new NamesRowItemAdapter(this, imageBio2Stolperstein.values());
        for (int i = 0; i < adapter.getCount(); i++) {
            View convertView = adapter.getView(i, null, stolpersteinListView);
            stolpersteinListView.addView(convertView);
        }

        OnMapReadyCallback onMapReady = googleMap -> {
            if (ActivityCompat.checkSelfPermission(StolpersteinActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(StolpersteinActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(false);
            }

            googleMap.getUiSettings().setAllGesturesEnabled(false);

            LatLng latLon = extras.getParcelable(STOLPERSTEINE_LATLNG_EXTRA);
            CameraPosition cp = CameraPosition.builder().target(latLon).zoom(15).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));

            MarkerOptions marker = new MarkerOptions() //
                    .position(latLon) //
                    .title(adresse) //
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.stolperstein));

            googleMap.addMarker(marker);
        };
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.staticmap)).getMapAsync(onMapReady);
    }
}
