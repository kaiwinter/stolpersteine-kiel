package de.vrlfr.stolpersteine.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import de.vrlfr.stolpersteine.activity.misc.ImageBioId;
import de.vrlfr.stolpersteine.activity.misc.NamesRowItemAdapter;
import de.vrlfr.stolpersteine.database.StolpersteinBo;

public class StolpersteinActivity extends BaseActivity {

    private static final String STOLPERSTEINE_EXTRA = "stolpersteine";
    private static final String STOLPERSTEINE_LATLNG_EXTRA = "stolperstein_latlng";

    public static Intent newIntent(Context context, ArrayList<StolpersteinBo> arrayList, LatLng latLng) {
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
        List<StolpersteinBo> parcelableArrayList = extras.getParcelableArrayList(STOLPERSTEINE_EXTRA);
        Map<ImageBioId, Collection<StolpersteinBo>> imageBio2Stolperstein = new HashMap<>();
        final boolean[] hasBiografie = {false};
        for (StolpersteinBo stolpersteinBo : parcelableArrayList) {
            int imageId = stolpersteinBo.imageId;
            int bioId = stolpersteinBo.bioId;
            ImageBioId imageBioId = new ImageBioId(imageId, bioId);

            Collection<StolpersteinBo> stolpersteine = imageBio2Stolperstein.get(imageBioId);
            if (stolpersteine == null) {
                stolpersteine = new ArrayList<>();
                imageBio2Stolperstein.put(imageBioId, stolpersteine);
            }
            stolpersteine.add(stolpersteinBo);

            if (stolpersteinBo.bioId > -1) {
                hasBiografie[0] = true;
            }
        }

        StolpersteinBo stolperstein = parcelableArrayList.iterator().next();
        final String adresse = stolperstein.adresse;
        getSupportActionBar().setTitle(adresse);

        LinearLayout stolpersteinListView = (LinearLayout) findViewById(R.id.listViewFix);
        NamesRowItemAdapter adapter = new NamesRowItemAdapter(this, imageBio2Stolperstein.values());
        for (int i = 0; i < adapter.getCount(); i++) {
            View convertView = adapter.getView(i, null, stolpersteinListView);
            stolpersteinListView.addView(convertView);
        }

        OnMapReadyCallback onMapReady = new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (ActivityCompat.checkSelfPermission(StolpersteinActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(StolpersteinActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(false);
                }

                googleMap.getUiSettings().setAllGesturesEnabled(false);

                LatLng latLon = extras.getParcelable(STOLPERSTEINE_LATLNG_EXTRA);
                CameraPosition cp = CameraPosition.builder().target(latLon).zoom(15).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));

                int drawable = hasBiografie[0] ? R.drawable.stolperstein_bio : R.drawable.stolperstein;

                MarkerOptions marker = new MarkerOptions() //
                        .position(latLon) //
                        .title(adresse) //
                        .icon(BitmapDescriptorFactory.fromResource(drawable));

                googleMap.addMarker(marker);
            }
        };
        ((MapFragment) getFragmentManager().findFragmentById(R.id.staticmap)).getMapAsync(onMapReady);
    }
}
