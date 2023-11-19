package de.vrlfr.stolpersteine.activity.stolperstein;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.BaseActivity;
import de.vrlfr.stolpersteine.database.Stolperstein;

/**
 * Anzeigen eines PDFs als Vollbild.
 */
public class FullscreenPdfActivity extends BaseActivity {

	private static final String INTENT_EXTRA_BIO_RESSOURCE_ID = "INTENT_EXTRA_BIO_RESSOURCE_ID";

	public static Intent newIntent(Context context, int bioId) {
		Intent intent = new Intent(context, FullscreenPdfActivity.class);

		// extras
		intent.putExtra(INTENT_EXTRA_BIO_RESSOURCE_ID, bioId);

		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_pdf);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			handleExtras(extras);
		}
	}

	private void handleExtras(Bundle extras) {
		getSupportActionBar().setTitle("Biografie");

		int bioId = extras.getInt(INTENT_EXTRA_BIO_RESSOURCE_ID);
		PDFView pdfView = findViewById(R.id.pdfView);
		pdfView.fromAsset("bio/" + Stolperstein.getImageAssetName(bioId)).load();
	}
}