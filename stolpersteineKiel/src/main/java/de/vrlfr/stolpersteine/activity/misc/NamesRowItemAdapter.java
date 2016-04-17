package de.vrlfr.stolpersteine.activity.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.vrlfr.stolpersteine.R;
import de.vrlfr.stolpersteine.activity.FullscreenImageActivity;
import de.vrlfr.stolpersteine.database.StolpersteinBo;

public final class NamesRowItemAdapter extends ArrayAdapter<String> {

	private static final String TAG = NamesRowItemAdapter.class.getSimpleName();
	private static final int ROW_ID = R.layout.stolperstein_listview_item;

	private final List<Collection<StolpersteinBo>> stolpersteineList;

	public NamesRowItemAdapter(Activity context, Collection<Collection<StolpersteinBo>> stolpersteine) {
		super(context, ROW_ID);
		this.stolpersteineList = new ArrayList<>(stolpersteine);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MotionRowViewHolder viewHolder;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(ROW_ID, parent, false);

			viewHolder = new MotionRowViewHolder();
			convertView.setTag(viewHolder);

			viewHolder.textViewNamen = (TextView) convertView.findViewById(R.id.stolpersteinNamen);

		} else {
			viewHolder = (MotionRowViewHolder) convertView.getTag();
		}

		Collection<StolpersteinBo> stolpersteine = stolpersteineList.get(position);
		String alleNamenString = "";
		for (StolpersteinBo stolperstein : stolpersteine) {
			String name = stolperstein.getName();
			String geboren = stolperstein.getGeboren();
			String tod = stolperstein.getTod();
			String daten = geboren + " - " + tod;

			if (alleNamenString.length() > 0) {
				alleNamenString += "<br/>";
			}
			alleNamenString += "<b>" + name + "</b> " + daten;
		}
		viewHolder.textViewNamen.setText(Html.fromHtml(alleNamenString));

		final StolpersteinBo stolpersteinBo = stolpersteine.iterator().next();
		ImageView stolpersteinBildImageView = (ImageView) convertView.findViewById(R.id.stolpersteinBild);
		String uri = "@drawable/id" + stolpersteinBo.getImageId();
		final int imageResource = getContext().getResources().getIdentifier(uri, null, getContext().getPackageName());

		if (imageResource > 0) {
			Picasso.with(getContext()).load(imageResource).error(android.R.drawable.ic_delete)
					.into(stolpersteinBildImageView);
			stolpersteinBildImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = FullscreenImageActivity.newIntent(v.getContext(), imageResource,
							stolpersteinBo.getAdresse());
					NamesRowItemAdapter.this.getContext().startActivity(intent);
				}
			});
		}

		ImageView biografieImageViewPdf = (ImageView) convertView.findViewById(R.id.biografieBild);
		if (stolpersteinBo.getBioId() == -1) {
			biografieImageViewPdf.setVisibility(View.GONE);
		} else {
			biografieImageViewPdf.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openBiografiePdf(stolpersteinBo.getBioId());
				}
			});
		}

		TextView verlegedatumCopyrightTf = (TextView) convertView.findViewById(R.id.detail_verlegedatum_copyright);
		verlegedatumCopyrightTf.setText(getContext().getString(R.string.image_verlegt_am)
				+ stolpersteinBo.getVerlegedatum());

		return convertView;
	}

	private void openBiografiePdf(int bioId) {
		FileOutputStream openFileOutput = null;
		try {
			openFileOutput = getContext().openFileOutput("biografie.pdf", Context.MODE_WORLD_READABLE);
			copyFile(getContext().getAssets().open("id" + bioId + ".pdf"), openFileOutput);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (openFileOutput != null) {
				try {
					openFileOutput.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		File pdfFile = new File(getContext().getFilesDir(), "/biografie.pdf");
		Uri path = Uri.fromFile(pdfFile);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setDataAndType(path, "application/pdf");

		getContext().startActivity(intent);
		pdfFile.deleteOnExit();
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	@Override
	public int getCount() {
		return stolpersteineList.size();
	}

	private final class MotionRowViewHolder {
		public TextView textViewNamen;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
