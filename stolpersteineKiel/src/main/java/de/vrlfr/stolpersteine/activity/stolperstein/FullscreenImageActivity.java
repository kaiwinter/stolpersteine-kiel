package de.vrlfr.stolpersteine.activity.stolperstein;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ortiz.touchview.TouchImageView;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.BaseActivity;

/**
 * Anzeigen eines Stolperstein Bildes als Vollbild.
 */
public class FullscreenImageActivity extends BaseActivity {

    private static final String INTENT_EXTRA_IMAGE_RESSOURCE_ID = "INTENT_EXTRA_IMAGE_RESSOURCE_ID";
    private static final String INTENT_EXTRA_ADRESSE = "INTENT_EXTRA_ADRESSE";

    public static Intent newIntent(Context context, int imageResource, String adresse) {
        Intent intent = new Intent(context, FullscreenImageActivity.class);

        // extras
        intent.putExtra(INTENT_EXTRA_IMAGE_RESSOURCE_ID, imageResource);
        intent.putExtra(INTENT_EXTRA_ADRESSE, adresse);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
    }

    private void handleExtras(Bundle extras) {
        int imageId = extras.getInt(INTENT_EXTRA_IMAGE_RESSOURCE_ID);
        TouchImageView imageView = findViewById(R.id.full_image);
        imageView.setMaxZoom(6);

        imageView.setImageResource(imageId);

        String adresse = extras.getString(INTENT_EXTRA_ADRESSE);
        getSupportActionBar().setTitle(adresse);
    }
}