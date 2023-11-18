package de.vrlfr.stolpersteine;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.vrlfr.stolpersteine.database.Stolperstein;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ResourcesTest {

    @Test
    public void imagesExist() {
        int imagesMissing = 0;
        for (Stolperstein stolperstein: getStolpersteine()) {
            try {
                int resourceIdForImageId = stolperstein.getResourceIdForImageId(Realm.getApplicationContext());
                Drawable drawable = Realm.getApplicationContext().getDrawable(resourceIdForImageId);
            } catch (Resources.NotFoundException e) {
                imagesMissing++;
                Log.e("TEST", "Foto mit der ID " + stolperstein.imageId + " fehlt");
            }

        }
        Assert.assertEquals("Es fehlen Fotos", 0, imagesMissing);
    }

    @Test
    public void textsExist() {
        int textsMissing = 0;
        for (Stolperstein stolperstein: getStolpersteine()) {
            try {
                String path = "bio/" + Stolperstein.getBioTxtAssetName(stolperstein.bioId);
                InputStream inputStream = Realm.getApplicationContext().getAssets().open(path);
                if (inputStream.available() == 0) {
                    textsMissing++;
                    Log.e("TEST", "Text mit der ID " + stolperstein.bioId + " fehlt");
                }
            } catch (IOException e) {
                textsMissing++;
                Log.e("TEST", "Text mit der ID " + stolperstein.bioId + " fehlt");
            }
        }
        Assert.assertEquals("Es fehlen Texte", 0, textsMissing);
    }

    @Test
    public void pdfsExist() {
        int pdfsMissing = 0;
        for (Stolperstein stolperstein: getStolpersteine()) {
            try {
                String path = "bio/" + Stolperstein.getImageAssetName(stolperstein.bioId);
                InputStream inputStream = Realm.getApplicationContext().getAssets().open(path);
            } catch (IOException e) {
                pdfsMissing++;
                Log.e("TEST", "PDF mit der ID " + stolperstein.bioId + " fehlt");
            }
        }
        Assert.assertEquals("Es fehlen PDFs", 0, pdfsMissing);
    }

    private List<Stolperstein> getStolpersteine() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .assetFile("stolperstein.realm")
                .readOnly()
                .build();

        Realm realm = Realm.getInstance(realmConfiguration);
        RealmResults<Stolperstein> stolpersteine = realm.where(Stolperstein.class).findAll();
        return realm.copyFromRealm(stolpersteine);
    }

}
