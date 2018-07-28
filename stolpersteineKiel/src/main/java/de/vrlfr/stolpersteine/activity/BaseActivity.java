package de.vrlfr.stolpersteine.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class BaseActivity extends AppCompatActivity {

    // Das zurückkehren zur vorherigen Activity würde auch ohne das hier funktionieren, allerdings muss dann immer die
    // Karte neu geladen werden. Mit dem finish() sieht das flüssiger aus, so als ob die Karte der MainActivity erhalten
    // bleibt
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
