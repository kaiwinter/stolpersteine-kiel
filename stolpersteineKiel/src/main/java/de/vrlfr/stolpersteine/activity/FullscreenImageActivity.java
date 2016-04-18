package de.vrlfr.stolpersteine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ortiz.touch.TouchImageView;
import com.squareup.picasso.Picasso;

import de.vrlfr.stolpersteine.R;

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
		TouchImageView imageView = (TouchImageView) findViewById(R.id.full_image);
		imageView.setMaxZoom(6);

		Picasso.with(this).load(imageId).error(android.R.drawable.ic_delete).into(imageView);

		String adresse = extras.getString(INTENT_EXTRA_ADRESSE);
		getSupportActionBar().setTitle(adresse);
	}
}