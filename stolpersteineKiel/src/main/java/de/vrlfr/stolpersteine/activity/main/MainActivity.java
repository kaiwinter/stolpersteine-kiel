package de.vrlfr.stolpersteine.activity.main;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.BaseActivity;
import de.vrlfr.stolpersteine.activity.main.fragment.about.AboutFragment;
import de.vrlfr.stolpersteine.activity.main.fragment.list.ListFragment;
import de.vrlfr.stolpersteine.activity.main.fragment.map.MapFragment;
import de.vrlfr.stolpersteine.database.Stolperstein;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity {

    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private LinearLayout drawerLinearLayout;

    private ActionBarDrawerToggle drawerToggle;
    private Fragment mapFragment;
    private ListFragment listFragment;
    private ArrayList<Stolperstein> stolpersteine;
    private BaseAdapter drawerListAdapter;

    private final AtomicInteger currentlySelectedFragment = new AtomicInteger(-1);

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        drawerList = findViewById(R.id.navList);
        drawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLinearLayout = findViewById(R.id.left_drawer);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        RealmResults<Stolperstein> all = realm.where(Stolperstein.class).findAll();
        stolpersteine = new ArrayList<>(realm.copyFromRealm(all));

        if (savedInstanceState == null) {
            // Selektion initial programmatisch setzen
            drawerList.setItemChecked(0, true);
            selectItem(0);
        }

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Wenn in anderen als dem Karten-Fragment zurück navigiert wird, erst zur Karte springen, bevor die App
                // geschlossen wird.
                if (drawerLayout.isDrawerOpen(drawerLinearLayout)) {
                    drawerLayout.closeDrawer(drawerLinearLayout);
                } else if (drawerList.getCheckedItemPosition() != 0) {
                    drawerList.setItemChecked(0, true);
                    selectItem(0);
                } else {
                    finish();
                }
            }
        });
    }

    private void selectItem(int position) {
        if (currentlySelectedFragment.getAndSet(position) == position) {
            // bereits selektiert -> ignorieren
            drawerLayout.closeDrawer(drawerLinearLayout);
            return;
        }
        drawerListAdapter.notifyDataSetChanged();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (position == 0) {
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance(stolpersteine);
            }
            ft.replace(R.id.content_frame, mapFragment);
        } else if (position == 1) {
            if (listFragment == null) {
                listFragment = ListFragment.newInstance(stolpersteine);
            }
            ft.replace(R.id.content_frame, listFragment);
        } else if (position == 2) {
            Fragment fragment = new AboutFragment();
            ft.replace(R.id.content_frame, fragment);
        }
        ft.commit();
        drawerLayout.closeDrawer(drawerLinearLayout);
    }

    private void addDrawerItems() {
        String[] entries = {"Karte", "Personenregister", "Informationen"};
        Integer[] imageId = {R.drawable.ic_map, R.drawable.ic_persons, R.drawable.ic_information};
        drawerListAdapter = new NavigationDrawerAdapter(MainActivity.this, entries, imageId) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View renderer = super.getView(position, convertView, parent);
                if (position == drawerList.getCheckedItemPosition()) {
                    renderer.setBackgroundResource(android.R.color.darker_gray);
                } else {
                    renderer.setBackgroundResource(android.R.color.transparent);
                }
                return renderer;
            }
        };
        drawerList.setAdapter(drawerListAdapter);
        drawerList.setOnItemClickListener((parent, view, position, id) -> selectItem(position));
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Keyboard ausblenden
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputManager != null) {
                    View currentFocus = getCurrentFocus();
                    if (currentFocus != null) {
                        inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Activate the navigation drawer toggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            try {
                realm.close();
            } catch (IllegalStateException e) {
                // ignore
            }
        }
    }
}
