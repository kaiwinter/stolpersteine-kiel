package de.vrlfr.stolpersteine.activity.stolperstein;

import static android.content.res.AssetManager.ACCESS_BUFFER;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.BaseActivity;
import de.vrlfr.stolpersteine.database.Stolperstein;
import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonConfiguration;
import io.noties.markwon.image.AsyncDrawable;
import io.noties.markwon.image.ImageSize;
import io.noties.markwon.image.ImageSizeResolverDef;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.image.file.FileSchemeHandler;

/**
 * Anzeigen eines Texts als Vollbild.
 */
public class FullscreenTxtActivity extends BaseActivity {

    private static final String INTENT_EXTRA_BIO_RESSOURCE_ID = "INTENT_EXTRA_BIO_RESSOURCE_ID";

    private float mScale = 1f;
    private ScaleGestureDetector mScaleGestureDetector;

    private float defaultSize;

    public static Intent newIntent(Context context, int bioId) {
        Intent intent = new Intent(context, FullscreenTxtActivity.class);

        // extras
        intent.putExtra(INTENT_EXTRA_BIO_RESSOURCE_ID, bioId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_txt);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        TextView markdownTextView = findViewById(R.id.markdown_text_view);
        defaultSize = markdownTextView.getTextSize();

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScale *= detector.getScaleFactor();
                mScale = Math.max(0.4f, Math.min(mScale, 3));

                markdownTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize * mScale);
                return true;
            }
        });
    }

    private void handleExtras(Bundle extras) {
        getSupportActionBar().setTitle("Biografie");

        int bioId = extras.getInt(INTENT_EXTRA_BIO_RESSOURCE_ID);

        String bioTxtFileContent = loadBioTxtFile("bio/" + Stolperstein.getBioTxtAssetName(bioId));

        Markwon markwon = Markwon
                .builder(this)
                .usePlugin(ImagesPlugin.create(plugin -> plugin.addSchemeHandler(FileSchemeHandler.createWithAssets(FullscreenTxtActivity.this))))
                .usePlugin(new ImageWidthPlugin())
                .build();

        TextView mardownTextView = findViewById(R.id.markdown_text_view);
        markwon.setMarkdown(mardownTextView, bioTxtFileContent);
    }

    private String loadBioTxtFile(String inFile) {
        try (InputStream stream = getAssets().open(inFile, ACCESS_BUFFER)) {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            Log.e("STOLPERSTEINE", Log.getStackTraceString(e));
            return "### Biografie nicht gefunden \uD83E\uDDD0";
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private static class ImageWidthPlugin extends AbstractMarkwonPlugin {

        private static final float IMAGE_WITH_EM = 10;

        @Override
        public void configureConfiguration(@NonNull MarkwonConfiguration.Builder builder) {
            builder.imageSizeResolver(new ImageSizeResolverDef() {
                @NonNull
                @Override
                public Rect resolveImageSize(@NonNull AsyncDrawable drawable) {
                    return resolveImageSize(
                            new ImageSize(
                                    new ImageSize.Dimension(IMAGE_WITH_EM, UNIT_EM), null
                            ),
                            drawable.getResult().getBounds(),
                            drawable.getLastKnownCanvasWidth(),
                            drawable.getLastKnowTextSize()
                    );
                }
            });
        }
    }

}