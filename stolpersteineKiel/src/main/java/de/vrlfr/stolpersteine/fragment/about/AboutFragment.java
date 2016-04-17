package de.vrlfr.stolpersteine.fragment.about;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import de.psdev.licensesdialog.LicenseResolver;
import de.psdev.licensesdialog.LicensesDialog;
import de.vrlfr.stolpersteine.R;

public class AboutFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about, container, false);

		rootView.findViewById(R.id.librariesButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LicenseResolver.registerLicense(new GoogleMapLicense());
				new LicensesDialog.Builder(getActivity()).setNotices(R.raw.notices)
						.setTitle(R.string.software_lizenzen).build().show();
			}
		});
		return rootView;
	}
}
