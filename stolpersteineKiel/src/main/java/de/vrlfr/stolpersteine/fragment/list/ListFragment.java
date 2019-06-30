package de.vrlfr.stolpersteine.fragment.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.StolpersteinActivity;
import de.vrlfr.stolpersteine.database.Stolperstein;

public class ListFragment extends Fragment implements OnQueryTextListener {

	private static final String STOLPERSTEINE = "stolpersteine";
	private List<Stolperstein> stolpersteine;
	private StolpersteinListAdapter adapter;

	public static ListFragment newInstance(ArrayList<Stolperstein> stolpersteine) {
		ListFragment fragment = new ListFragment();

		Bundle arguments = new Bundle();
		arguments.putParcelableArrayList(STOLPERSTEINE, stolpersteine);
		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			stolpersteine = arguments.getParcelableArrayList(STOLPERSTEINE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list, container, false);

		SearchView searchView = rootView.findViewById(R.id.fragment_list_searchbar);
		ListView stolpersteinList = rootView.findViewById(R.id.stolperstein_liste);

		adapter = new StolpersteinListAdapter(getActivity(), stolpersteine);
		searchView.setOnQueryTextListener(this);
		stolpersteinList.setAdapter(adapter);

		stolpersteinList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Stolperstein stolperstein = adapter.getItem(position);

				LatLng latLng = new LatLng(stolperstein.latitude, stolperstein.longitude);
				ArrayList<Stolperstein> arrayList = new ArrayList<>(Collections
						.singletonList(stolperstein));
				Intent intent = StolpersteinActivity.newIntent(getActivity(), arrayList, latLng);
				startActivity(intent);
			}
		});

		return rootView;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		adapter.getFilter().filter(newText);
		return false;
	}
}
