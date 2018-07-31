package de.vrlfr.stolpersteine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import de.vrlfr.stolpersteine.R;

/**
 * Anzeigen eines PDFs als Vollbild.
 */
public class FullscreenPDFActivity extends BaseActivity {

	private static final String INTENT_EXTRA_BIO_RESSOURCE_ID = "INTENT_EXTRA_BIO_RESSOURCE_ID";

	public static Intent newIntent(Context context, int pdfResource) {
		Intent intent = new Intent(context, FullscreenPDFActivity.class);

		// extras
		intent.putExtra(INTENT_EXTRA_BIO_RESSOURCE_ID, pdfResource);

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
		int bioId = extras.getInt(INTENT_EXTRA_BIO_RESSOURCE_ID);
		PDFView pdfView = findViewById(R.id.pdfView);
		pdfView.fromAsset("bio/id" + bioId + ".pdf").load();

		getSupportActionBar().setTitle("Biografie");
	}
}